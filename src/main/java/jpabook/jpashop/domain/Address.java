package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address{

    private String city;
    private String street;
    private String zipcode;

    // JPA 스펙에서는 protected 까진 허용해 줌
    protected Address() {
    }

    // constructor 단축키 [Win : ALT + Ins, Mac : Cmd + N]
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
