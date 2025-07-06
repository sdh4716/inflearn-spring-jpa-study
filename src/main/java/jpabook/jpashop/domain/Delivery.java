package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // 상태 정보를 EnumType.ORDINAL로 해놓으면 추후 상태값이 추가될 때
    // 순서가 뒤바뀜 따라서 STRING으로 문자로 상태값을 표현
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, CAMP
}
