package com.collegemanagement.Model;


import com.collegemanagement.Model.Enum.AttendanceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"student_id","date"}
                )
        }
)

public class Attendance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id",nullable = false)
    private User student;

    @NotNull
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AttendanceStatus attendanceStatus;
}
