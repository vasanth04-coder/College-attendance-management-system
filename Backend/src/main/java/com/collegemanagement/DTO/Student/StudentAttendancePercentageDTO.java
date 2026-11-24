package com.collegemanagement.DTO.Student;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentAttendancePercentageDTO
{
    private Integer id;
    private String  registerNumber;
    private String name;
    private double AttendancePercentage;
}
