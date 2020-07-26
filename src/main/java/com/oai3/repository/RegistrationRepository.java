package com.oai3.repository;

import com.oai3.repository.domain.Registrations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registrations, Long> {
}
