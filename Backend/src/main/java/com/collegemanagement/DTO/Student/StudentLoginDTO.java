package com.collegemanagement.DTO.Student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StudentLoginDTO
{
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
