package com.registration.repository;

import com.registration.repository.domain.Registrations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registrations, Long> {

    @Modifying
    @Query("update Registrations r set r.status = 'DEACTIVE' where r.registrationId = :regId")
    int softDeleteRegistrationById(Long regId);
}
