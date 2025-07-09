package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter // 실무에서는 Setter는 꼭 필요할 때만 열어 줌
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    // 여기는 예시지만 실무에서는 엔티티를 건드는 것이 아니라 DTO를 만들어서 API 요청 스펙에 맞춰서 데이터를 제공하는 것이 맞다.
    // 그게 아니면 엔티티가 변경되었을 시 API 스펙이 변해버려 작동하던 API들이 고장날 수 있음.
    // 즉, API를 만들 때는 엔티티를 분리하여야 한다.
    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
