package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 **/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    // 1. 엔티티를 직접 노출
    // 2. 자원 낭비 (필요 없는 쿼리까지 수행)
    // 3. Lazy Loading으로 인한 성능 이슈
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 로딩 강제 초기화
            order.getDelivery().getAddress(); // Lazy 로딩 강제 초기화
        }
        return all;
    }

    // 1. Lazy Loading으로 인한 성능 이슈
    // 2. N + 1 문제 발생
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        
        // 1. ORDER 2개 조회됨
        // 2. N + 1 문제 발생
        // - result를 만들면서 order이 2개이기 때문에 member 2번, delivery 2번씩 조회한다.
        // -> 어차피 member, delivery를 조회할거면 1번씩만 조회해도 되는데 불필요하게 조회 횟수가 늘어난다.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // 1. fetch join을 통해 필요한 엔티티를 한번에 가져옴.
    // 2. Lazy Loading이 발생하지 않음. orderRepository.findAllWithMemberDelivery()에서 이미 다 가져왔기 때문.
    // 3. 다만 join은 되었지만 join된 테이블의 필요 없는 필드도 다 가져옴.
    // 4. 자원 낭비 but, 재사용성은 높음
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        // fetch join을 통한 성능 최적화
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // 1. dto에서 필요한 필드만 가져올 수 있음.
    // 2. 자원 절약 -> 성능 향상 but, fit하게 만들었기 때문에 재사용성은 낮음.
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
}
