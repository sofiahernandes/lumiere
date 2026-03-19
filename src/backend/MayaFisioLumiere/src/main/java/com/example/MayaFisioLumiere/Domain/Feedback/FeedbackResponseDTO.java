package com.example.MayaFisioLumiere.Domain.WeeklyFeedback;

import java.time.LocalDate;
import java.util.UUID;

public record WeeklyFeedbackResponseDTO(
        Long registry_ID,
        UUID patient_id,
        String comment,
        LocalDate executionDate
) {
}
