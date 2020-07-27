package com.registration.repository.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="consumer")
@Builder
@Getter
@Setter
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "consumer_id")
    private long consumerId;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "physicalDisability")
    private Boolean physicalDisability;

    @Column(name = "address_id")
    private long addressId;

    public Consumer() {
    }

    public Consumer(long consumerId, String name, int age, Boolean physicalDisability, long addressId) {
        this.consumerId = consumerId;
        this.name = name;
        this.age = age;
        this.physicalDisability = physicalDisability;
        this.addressId = addressId;
    }
}
