package com.example.MayaFisioLumiere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "weeklyFeedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WeeklyFeedbackEntity {
    @GeneratedValue
    @Id
    private Long registry_ID;

    @Column(nullable = false)
    private Boolean painLevel;

    //A data do feedback ficará registrada no banco, e a cada 7 dias será mandada a weekly.
    @Column
    private LocalDate executionDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;
}
