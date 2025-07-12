package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // 주문 조회 V1: 엔티티 직접 노출
    @GetMapping("/api/v1/orders")
    private List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        // intellij [iter] 입력하면 for문 만들어줌
        //
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return all;
    }

    // 주문 조회 V2: 엔티티를 DTO로 변환
    // 1. 엔티티를 노출하지 않고 DTO로 반환.
    // 2. N + 1 문제 발생
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                // o -> new OrderDto(o) 원래 이거였음, ALT + ENTER로 최적화 가능
                .map(OrderDto::new)
                // 이것도 Collectors.toList() 였음
                .collect(toList());

        return result;
    }

    // 주문 조회 V3: 엔티티를 DTO로 변환 - 페치 조인 최적화
    // 페이징이 불가능
    // 컬렉션 fetch join은 하나만 가능
    // XToOne 관계는 fetch join 해도 됨 (row 수가 많아지지 않음)
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        // ALT + ENTER로 바로 매서드 create 가능. (Mac -> Opt + Enter)
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref= " + order + " id= " + order.getId());
        }

        List<OrderDto> result = orders.stream()
                // o -> new OrderDto(o) 원래 이거였음, ALT + ENTER로 최적화 가능
                .map(OrderDto::new)
                // 이것도 Collectors.toList() 였음
                .collect(toList());

        return result;
    }

    // 주문 조회 V3.1: 엔티티를 DTO로 변환 - 페이징과 한계 돌파
    // xToOne 관계만 fetch join
    // v3에 비해 쿼리 횟수는 늘어나지만, DB 데이터 전송량이 줄어든다.
    // 페이징 가능
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @Getter // 또는 @Data -> 안쓰는게 나은 경우도 있음.
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            // 조회결과를 DTO로 반환했다 하더라도. DTO 내부에 엔티티가 있다면
            // 해당 엔티티도 DTO로 변환해서 반환해야 한다. (엔티티 외부 노출 금지)
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
