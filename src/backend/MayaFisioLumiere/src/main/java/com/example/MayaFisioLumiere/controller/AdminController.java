package com.example.MayaFisioLumiere.controller;

import com.example.MayaFisioLumiere.Domain.Admin.AdminRequestDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseRequestDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;
import com.example.MayaFisioLumiere.Services.AdminService;
import com.example.MayaFisioLumiere.entity.AdminEntity;
import com.example.MayaFisioLumiere.entity.ExerciseEntity;
import com.example.MayaFisioLumiere.Services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminAccess")

public class AdminController {
    @Autowired
    private AdminService adminService;


    //Criar Administradores/Profissionais novos
    @PostMapping("/create-admin")
    public ResponseEntity<AdminEntity> create(@RequestBody AdminRequestDTO body){
        AdminEntity newAdmin = this.adminService.createAdmin(body);
        return ResponseEntity.ok(newAdmin);
    }


}