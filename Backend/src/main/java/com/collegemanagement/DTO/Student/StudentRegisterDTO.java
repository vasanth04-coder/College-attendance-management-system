package com.collegemanagement.DTO.Student;
import com.collegemanagement.Model.Enum.SemName;
import com.collegemanagement.Model.Enum.Year;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentRegisterDTO
{
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min=10)
    private String registerNumber;

    @NotBlank
    private String department;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Year year;

    @NotBlank
    private String studentBatch;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SemName semName;
}
