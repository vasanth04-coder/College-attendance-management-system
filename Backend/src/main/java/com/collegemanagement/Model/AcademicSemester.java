package com.collegemanagement.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "academic_year")
public class AcademicSemester
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String AcademicYear;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;
}
