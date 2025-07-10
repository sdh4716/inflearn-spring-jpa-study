package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     **/
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 실제로는 배송지 정보를 입력함.

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        // 현재 예제에서는 order > orderItem, orderItem > delivery 이렇게만 사용하기 때문에
        // order에서 CASCADE=ALL을 설정해서 save() 매서드를 호출하면 orderItem, delivery가 모두 저장이 되지만,
        // 실무에서는 만약 orderItem, delivery가 쓰이는 곳이 많고, 중요한 엔티티라면 사용하지 않는 것이 좋다.
        // 1. 서로 1개의 관계를 가지고 있음. 2. persist 라이프 사이클이 완전히 동일함.
        // 위 두 가지 경우에는 CASCADE=ALL을 사용해도 좋다.

        return order.getId();
    }

    /**
     * 주문 취소
     **/
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();

        // JPA의 장점이 여기서 나옴.
        // 주문 취소를 예시로 들면 실제 데이터베이서의 update 쿼리를 날려서 주문 상태를 변경하지 않더라도
        // JPA가 알아서 dirty checking을 진행. 변경된 데이터를 데이터베이스에 반영해 줌.
        // 즉, 그 동안 이런 상태 변경을 위해 작성했던 trigger, procedure, update문 등이 필요없이 JPA가 알아서 처리해 줌. (대박..)
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
