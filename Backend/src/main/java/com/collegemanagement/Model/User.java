package com.collegemanagement.Model;

import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.Enum.Year;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min=8)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String registerNumber;

    @Enumerated(EnumType.STRING)
    private Year year;

    private String qualification;

    private String studentBatch;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name="academic_semester_id")
    private AcademicSemester academicSemester;
}
