package com.example.MayaFisioLumiere.controller;

import com.example.MayaFisioLumiere.Domain.Admin.AdminRequestDTO;
import com.example.MayaFisioLumiere.Domain.Admin.AdminResponseDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseRequestDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;
import com.example.MayaFisioLumiere.Services.AdminService;
import com.example.MayaFisioLumiere.entity.AdminEntity;
import com.example.MayaFisioLumiere.entity.ExerciseEntity;
import com.example.MayaFisioLumiere.Services.ExerciseService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //Atualizar email, nome ou senha
    @PutMapping("/updateAdmin/{adminUser_ID}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(
            @PathVariable Long adminUser_ID,
            @RequestBody AdminRequestDTO data) {
        AdminResponseDTO admin = adminService.updateAdmin(adminUser_ID, data);
        return ResponseEntity.ok(admin);
    }

    //Buscar todos os Administradores/Profissionais

    @GetMapping("/all/admins")
    public ResponseEntity<List<AdminResponseDTO>> getAdmins(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<AdminResponseDTO> allAdmins = this.adminService.getAllAdmins(page, size);
        return ResponseEntity.ok(allAdmins);
    }

    //Buscar Administradores/Profissionais por Id

    @GetMapping("/ID/{adminUser_ID}")
    public ResponseEntity<AdminResponseDTO> searchById(
            @PathVariable Long adminUser_ID) {
        AdminResponseDTO admins = adminService.findById(adminUser_ID);
        return ResponseEntity.ok(admins);
    }

    //Buscar por email

    @GetMapping("/adminEmail/{adminEmail}")
    public ResponseEntity<List<AdminResponseDTO>> searchByEmail(
            @PathVariable String adminEmail) {
        List<AdminResponseDTO> admin = adminService.findByEmail(adminEmail);
        return ResponseEntity.ok(admin);
    }

    //Deletar administrador por id

    @DeleteMapping("/deleteAdminId/{adminUser_ID}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long adminUser_ID){
        adminService.deleteAdminById(adminUser_ID);
        return ResponseEntity.ok("Administrador deletado com sucesso");
    }

    //LOGIN do administrador ----TESTAR
}