package org.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SpaceShipRepository extends CrudRepository<SpaceShip, Integer> {
    Page<SpaceShip> findAll(Pageable pageable);
    Set<SpaceShip> findByNameContaining(String name);
    @Override
    Optional<SpaceShip> findById(Integer integer);
}
