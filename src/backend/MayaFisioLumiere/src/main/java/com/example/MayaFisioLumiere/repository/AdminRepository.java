package com.example.MayaFisioLumiere.repository;

import com.example.MayaFisioLumiere.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long>{

    Optional<AdminEntity> findByName(String adminName);

    List<AdminEntity> findByEmailContainingIgnoreCase(String adminEmail);
}