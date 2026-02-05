package com.mathew.subscribo.subscription.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        String cpf,
        LocalDateTime createdAt
) {
}
