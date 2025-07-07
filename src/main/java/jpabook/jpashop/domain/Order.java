package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // fetch = FetchType.EAGER를 하면 연관된 모든 데이터를 다 가져오기 때문에
    // N+1 문제 발생할 수 있음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 원래는 persist(orderItemA), persist(orderItemB), persist(orderItemC)
    // 이렇게 orderItem을 저장하게 되지만 CascadeType.ALL을 해두면 order를 저장할 때 한번에 orderItem들도 저장.
    // 삭제도 마찬가지
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1. 1대1 매핑일 경우에는 주인을 자주 쓰는 테이블로 해주는게 좋다. 여기서의 경우는 Order
    // 2. 여기도 마찬가지로 CascadeType.ALL을 해두면 order를 저장할 때 delivery도 같이 저장.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송정보

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // 연관관계 (편의) 메소드 --------------------------------------
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 매서드 --------------------------------------
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    
    // 비즈니스 로직 --------------------------------------
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    
    // 조회 로직 --------------------------------------

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            int price = orderItem.getTotalPrice();
            totalPrice += price;
        }

        return totalPrice;
    }
}