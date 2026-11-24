package com.collegemanagement.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class ErrorResponse
{
    private int status;

    private String message;

    private List<ErrorDetails>errorDetails;
}
