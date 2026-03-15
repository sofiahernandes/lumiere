package com.example.MayaFisioLumiere.controller;


import com.example.MayaFisioLumiere.Domain.WorkoutSession.WorkoutSesRequestDTO;
import com.example.MayaFisioLumiere.entity.WorkoutSessionEntity;
import com.example.MayaFisioLumiere.Services.WorkoutSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout")
public class WorkoutSessionController {

    @Autowired
    private WorkoutSessionService workoutSessionService;

    //Criar o workout quando cria a ExerciseSession
    //ROTA http://localhost:8081/workout/create-workout
    @PostMapping("/create-workout")
    public WorkoutSessionEntity createWorkout(@RequestBody WorkoutSesRequestDTO data){
        return workoutSessionService.createWorkout(data);
    }

    //Paciente dar check no workout do dia
    //ROTA http://localhost:8081/workout/check/2
    @PutMapping("/check/{id}")
    public ResponseEntity<?> checkWorkout(@PathVariable Long id){
        try{
            WorkoutSessionEntity workout = workoutSessionService.checkWorkout(id);
            return ResponseEntity.ok(workout);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Erro ao marcar treino como concluído");
        }
    }


    //Buscar se o paciente está ativo

    //Buscar todos os pacientes ativos

    //Buscar todas as workout sessions de um paciente (patient id)

    //Buscar todas as workout sessions de todos os pacientes

    //Deletar a workout Session de um paciente
}