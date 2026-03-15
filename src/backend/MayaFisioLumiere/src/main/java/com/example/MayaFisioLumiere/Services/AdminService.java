package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.Admin.AdminRequestDTO;
import com.example.MayaFisioLumiere.Domain.Admin.AdminResponseDTO;
import com.example.MayaFisioLumiere.entity.AdminEntity;
import com.example.MayaFisioLumiere.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    //Criar novos perfis de Administradores, tanto da Maya quanto ela criar de outros profissionais
    @Autowired
    private AdminRepository adminRepository;
    public AdminEntity createAdmin(AdminRequestDTO data){
        AdminEntity newAdmin = new AdminEntity();
        newAdmin.setAdminName(data.adminName());
        newAdmin.setAdminEmail(data.adminEmail());
        newAdmin.setAdminPassword(data.adminPassword());

        return adminRepository.save(newAdmin);
    }

    //Atualizar email, nome ou senha do administrador, procurando o admin pelo ID dele

    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO data){
        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil administrador não encontrado"));
        if(data.adminName() != null){
            admin.setAdminName(data.adminName());
        }

        if (data.adminEmail() != null){
            admin.setAdminEmail((data.adminEmail()));
        }

        if(data.adminPassword() != null) {
            admin.setAdminPassword(data.adminPassword());
        }

        adminRepository.save(admin);
        return new AdminResponseDTO(
                admin.getAdminUser_ID(),
                admin.getAdminName(),
                admin.getAdminEmail(),
                admin.getAdminPassword()
        );
    }

    //Buscar todos os administradores/profissionais e seus dados

    public List<AdminResponseDTO> getAllAdmins(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<AdminEntity> adminsPage = this.adminRepository.findAll(pageable);
        return adminsPage.map(admin -> new AdminResponseDTO(
                admin.getAdminUser_ID(),
                admin.getAdminName(),
                admin.getAdminEmail(),
                admin.getAdminPassword() //REMOVER DEPOIS DA AUTENTICAÇÃO E HASH
            )
        ).stream().toList();
    }

    //Buscar Administradores/Profissionais por ID (util para o login?)

    public AdminResponseDTO findById(Long id) {
        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));
        return new AdminResponseDTO(
                admin.getAdminUser_ID(),
                admin.getAdminName(),
                admin.getAdminEmail(),
                admin.getAdminPassword() //REMOVER DEPOIS DA AUTENTICAÇÃO E HASH
        );
    }

    //Buscar por email

    public List<AdminResponseDTO> findByEmail(String adminEmail){
        List<AdminEntity> admins = adminRepository.findByEmailContainingIgnoreCase(adminEmail);

        return admins.stream().map(admin ->
                new AdminResponseDTO(
                        admin.getAdminUser_ID(),
                        admin.getAdminName(),
                        admin.getAdminEmail(),
                        admin.getAdminPassword() //REMOVER DEPOIS DA AUTENTICAÇÃO E HASH
                )
        ).toList();
    }

    //Deletar administrador por id
    public void deleteAdminById(Long id){
        if(!adminRepository.existsById(id)){
            throw new RuntimeException("Admin não encontrado");
        }
        adminRepository.deleteById(id);
    }


    //LOGIN do administrador ----TESTAR

    public AdminEntity loginAdmin(String adminEmail, String adminPassword){
        Optional<AdminEntity> adminLogin = adminRepository.findByEmail(adminEmail);
            if (adminLogin.isPresent()) {
                AdminEntity admin = adminLogin.get();
                if(admin.getAdminPassword().equals(adminPassword)){
                    return admin;
                }
            }
        throw new RuntimeException("Email ou senha inválidos");
    }


}
