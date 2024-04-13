package com.example.democassandra.repository;

import com.example.democassandra.entities.Vet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface VetRepository extends CrudRepository<Vet, Integer> {
    Vet findByName(String name);

    Optional<Vet> findAllById(UUID uuid);
}
