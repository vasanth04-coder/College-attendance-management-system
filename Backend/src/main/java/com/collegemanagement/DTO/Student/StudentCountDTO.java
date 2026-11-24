package com.collegemanagement.DTO.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCountDTO
{
    private int totalStudents;

    private List<ViewStudentDTO> students;
}
