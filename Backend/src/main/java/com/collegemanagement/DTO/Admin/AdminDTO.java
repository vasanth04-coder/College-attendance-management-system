package com.collegemanagement.DTO.Admin;


import com.collegemanagement.Model.Enum.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDTO
{
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    private String department;

    @NotBlank
    private String qualification;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}
