package com.example.pharmacyproject.direction.repository;

import com.example.pharmacyproject.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
