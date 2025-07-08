package org.demo.persistance.repository;

import org.demo.persistance.entities.MovieSpaceShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieSpaceShipRepository extends CrudRepository<MovieSpaceShip, Long> {
    Page<MovieSpaceShip> findAll(Pageable pageable);
    Set<MovieSpaceShip> findByNameContaining(String name);
    Optional<MovieSpaceShip> findById(Long integer);
    boolean existsById(Long id);
}
