package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.Exception.NotEnoughStockException;
import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직 --------------------------------------
    // 객체 지향적으로 데이터를 가지고 있는 곳에 비즈니스 로직이 있는 것이 바람직함. (응집력 ↑)
    // 바깥에서 계산을 해서 setStock을 하는 것은 바람직하지 못함. (객체 지향적이지 못함.)

    /**
    * stock 증가
    */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    }

}
