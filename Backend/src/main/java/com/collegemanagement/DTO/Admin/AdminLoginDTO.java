package com.collegemanagement.DTO.Admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AdminLoginDTO
{
   @NotBlank
   @Email
   private String email;

    @NotBlank
    @Size(min = 8)
    private  String password;
}
