package com.collegemanagement.DTO;

import com.collegemanagement.Model.Enum.SemName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcademicSemDTO
{

    @NotBlank
    private String academicYear;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private SemName semName;

}
