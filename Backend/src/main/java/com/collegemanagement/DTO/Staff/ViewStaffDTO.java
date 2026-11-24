package com.collegemanagement.DTO.Staff;

import com.collegemanagement.Model.Enum.Year;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ViewStaffDTO
{
    private Integer id;

    private  String department;

    private String name;

    private Year year;

    private String qualification;
}
