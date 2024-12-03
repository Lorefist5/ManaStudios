package com.manastudio.Features.Users.Dtos.Edit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangeUsernameRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String newUsername;
}
