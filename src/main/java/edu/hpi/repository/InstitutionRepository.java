package edu.hpi.repository;

import edu.hpi.domain.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstitutionRepository extends JpaRepository<Institution, UUID> {

    Optional<Institution> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
