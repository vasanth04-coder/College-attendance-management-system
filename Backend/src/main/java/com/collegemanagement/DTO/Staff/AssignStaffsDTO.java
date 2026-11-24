package com.collegemanagement.DTO.Staff;

import com.collegemanagement.Model.Enum.Year;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class AssignStaffsDTO
{
    @NotNull
    @Enumerated(EnumType.STRING)
    private Year year;
}
