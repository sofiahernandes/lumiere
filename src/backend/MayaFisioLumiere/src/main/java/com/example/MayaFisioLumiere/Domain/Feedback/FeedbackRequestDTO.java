package com.example.MayaFisioLumiere.Domain.WeeklyFeedback;

import java.util.UUID;

public record WeeklyfeedbackRequestDTO(
        UUID patient_id,
        Long painLevel,
        String comment
) {
}
