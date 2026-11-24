package com.collegemanagement.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EndDateDTO
{
    @NotNull
    private LocalDate endDate;
}
