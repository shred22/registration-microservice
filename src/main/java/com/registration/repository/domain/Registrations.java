package com.registration.repository.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="registrations")
@Builder
@Getter
@Setter
@RedisHash("registrations")
public class Registrations implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "registration_id")
    private Long registrationId;

    @Column(name = "consumer_id")
    private Long consumerId;

    @Column(name = "status")
    private String status;

    public Registrations() {
    }

    public Registrations(Long registrationId, Long consumerId, String status) {
        this.registrationId = registrationId;
        this.consumerId = consumerId;
        this.status = status;
    }
}
