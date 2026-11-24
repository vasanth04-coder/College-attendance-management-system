package com.collegemanagement.Model;

import com.collegemanagement.Model.Enum.SemType;
import com.collegemanagement.Model.Enum.SemName;
import com.collegemanagement.Model.Enum.Year;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Semester
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, unique = true)
    private SemName name;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Year year;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private SemType type;
}
