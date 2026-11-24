package com.collegemanagement.Exception;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDetails
{
    private String field;

    private String message;
}
