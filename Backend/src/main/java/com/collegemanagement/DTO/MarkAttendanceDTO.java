package com.collegemanagement.DTO;


import com.collegemanagement.Model.Enum.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MarkAttendanceDTO
{
    @NotNull
    private Integer studentId;

    @NotNull
    private AttendanceStatus attendanceStatus;
}
