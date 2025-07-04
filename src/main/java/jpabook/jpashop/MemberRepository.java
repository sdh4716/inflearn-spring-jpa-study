package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();

        // Member를 반환하지 않고 ID를 반환하는 이유:
        // 커맨드와 쿼리를 분리해라. 라는 원칙을 지키기 위해
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
