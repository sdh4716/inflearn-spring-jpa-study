package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // 저장
    public void save(Item item) {
        if (item.getId() == null) {
            // 신규 등록
            em.persist(item);
        } else {
            // 업데이트
            em.merge(item);
        }
    }

    // 단건 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 전체 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
