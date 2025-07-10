package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


// [ 쿼리 방식 선택 권장 순서 ]
// 1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
// 2. 필요하면 페치 조인으로 성능을 최적화 한다.  대부분의 성능 이슈가 해결된다.
// 3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
// 4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // v1 : 엔티티의 모든 정보가 노출됨. 회원의 주문정보도 노출됨.
    // @JsonIgnore을 사용해 없앨 수 있지만, 엔티티를 활용하는 부분이 영향을 받음.
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    // 1번은 엔티티를 바로 받아옴, API 스펙이 바뀌어버림 (안씀)
    // 엔티티를 외부에 노출하면 안됨. DB구조 까지 드러나 버릴 수 있음.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 2번은 DTO 사용
    // 1번과 다르게 DTO를 보면 API 스펙에서 어떤 파라미터를 받는 지 바로 알 수 있음.
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PatchMapping("/api/v2/members/{id}") // 부분 수정에는 Patch
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        // 변경이 완료되면 다시 조회하여 변경 여부를 확인.
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
