package com.manastudio.Features.Users.Dtos.Edit;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeBirthdateRequest {
    @NotNull
    private LocalDate newBirthdate;
}