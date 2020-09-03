package com.redeaoba.api.repository;

import com.redeaoba.api.model.Comerciante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComercianteRepository extends JpaRepository<Comerciante, Long> {
    Optional<Comerciante> findByEmail(String email);

    Optional<Comerciante> findByCnpj(String cnpj);

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);
}
