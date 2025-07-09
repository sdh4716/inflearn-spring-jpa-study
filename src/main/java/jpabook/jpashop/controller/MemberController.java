package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/creatememberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        // Member 엔티티가 있음에도 MemberForm을 사용하는 이유
        // 1. 실무에서는 엔티티와 폼이 정확하게 일치하는 경우는 잘 없음.
        // 2. 엔티티는 엔티티의 역할만 순수하게 하도록 설계하는 것이 바람직 함.
        // 3. 엔티티에 이것 저것 넣다보면 유지보수가 너무 힘들어짐.

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";

    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        // 여기서는 Member를 바로 쓰지만
        // 실무에서는 DTO를 생성해서 쓰는게 낫다.
        // 특히 API를 만들때는 절대 엔티티 외부로 반환하면 안됨. (엔티티의 변화로 API 스펙이 변해버림, 불안정한 API가 됨.)
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
