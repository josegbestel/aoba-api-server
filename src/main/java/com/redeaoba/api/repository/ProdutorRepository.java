package com.redeaoba.api.repository;

import com.redeaoba.api.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutorRepository extends JpaRepository<Produtor, Long> {

    Optional<Produtor> findByEmail(String email);
    Optional<Produtor> findByCodigoRegistro(String codigoRegistro);

    boolean existsByEmail(String email);
    boolean existsByCodigoRegistro(String codigoRegistro);

}
