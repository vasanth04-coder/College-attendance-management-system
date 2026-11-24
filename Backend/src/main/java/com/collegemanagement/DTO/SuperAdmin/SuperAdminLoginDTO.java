package com.collegemanagement.DTO.SuperAdmin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SuperAdminLoginDTO
{
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
