package com.collegemanagement.DTO.Student;

import com.collegemanagement.Model.Enum.Year;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewStudentDTO
{
        private Integer id;

        private String name;

        private String registerNumber;

        private String department;

        private Year year;

        private String studentBatch;
}
