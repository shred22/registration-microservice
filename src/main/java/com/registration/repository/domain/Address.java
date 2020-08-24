package com.registration.repository.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

@Entity
@Table(name="address")
@Builder
@Getter
@Setter
@RedisHash("address")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "addressLine1")
    private String addressLine1;

    @Column(name = "addressLine2")
    private String addressLine2;

    @Column(name = "pincode")
    private long pincode;

    @Column(name = "landmark")
    private String landmark;

    public Address() {
    }

    public Address(Long addressId, String addressLine1, String addressLine2, long pincode, String landmark) {
        this.addressId = addressId;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.pincode = pincode;
        this.landmark = landmark;
    }
}
