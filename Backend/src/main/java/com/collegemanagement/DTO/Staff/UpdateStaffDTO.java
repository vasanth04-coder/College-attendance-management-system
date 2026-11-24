package com.collegemanagement.DTO.Staff;


import com.collegemanagement.Model.Enum.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateStaffDTO
{
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotBlank
    private String department;
}
