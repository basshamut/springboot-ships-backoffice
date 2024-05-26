package org.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieSpaceShipRepository extends CrudRepository<MovieSpaceShip, Integer> {
    Page<MovieSpaceShip> findAll(Pageable pageable);
    Set<MovieSpaceShip> findByNameContaining(String name);
    @Override
    Optional<MovieSpaceShip> findById(Integer integer);
}
