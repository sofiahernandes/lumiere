package com.example.projeto8.api.patient;

import com.example.projeto8.model.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PatientService {
    //para pegar as rotas do backend de patients (dados do patient)!
    //Buscar os dados do paciente pelo nome (para o profile)
    @GET("api/patient/getByName/{name}")
    Call<List<Patient>> getPatientByFullName(
            @Path("name") String name,
            @Query("surname") String surname
    );

}
