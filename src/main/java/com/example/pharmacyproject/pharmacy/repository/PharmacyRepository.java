package com.example.pharmacyproject.pharmacy.repository;

import com.example.pharmacyproject.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
