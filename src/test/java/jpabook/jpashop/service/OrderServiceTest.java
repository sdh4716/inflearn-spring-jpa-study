package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.Exception.NotEnoughStockException;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {

        //given
        Member member = createMember(); // CTRL + ALT + M (Win) 단축키로 생성 매서드를 바로 만들 수 있음.

        Book book = createBook("JPA 학습", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {

        //given
        Member member = createMember(); // Member member = 을 앞에 붙여주려면 CTRL + ALT + V
        Item item = createBook("JPA 학습", 10000, 10); //이름, 가격, 재고

        int orderCount = 11; //재고보다 많은 수량

        //When, Then
        assertThrows(
                NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));

        // 예제에서는 종합적으로 테스트를 했지만
        // 실무에서는 Item 엔티티의 removeStock()을 단위 테스트하는 것이 바람직하다.
    }

    @Test
    public void 주문취소() throws Exception {

        //given
        Member member = createMember();
        Item item = createBook("JPA 2", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소 시 상태는 CANCEL이어야 한다.");
        assertEquals(10, item.getStockQuantity(), "주문 취소된 상품은 그 만큼 재고가 증가해야 한다.");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price); // 커맨드 + 옵션 + P (Mac), CTRL + ALT + P (Win)으로 파라미터로 바로 올릴 수 있음.
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "어딘가", "123123"));
        em.persist(member);
        return member;
    }

}