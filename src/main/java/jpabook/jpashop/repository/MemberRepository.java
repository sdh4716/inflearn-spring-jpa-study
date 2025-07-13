package jpabook.jpashop.repository;

import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA를 사용. JPARepository를 의존하면
// 기본으로 제공되는 매서드들을 사용할 수 있음.
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(@NotEmpty String name);
}
