package com.collegemanagement.Exception;

public class AttendanceAlreadyMarkedException extends RuntimeException
{
    public AttendanceAlreadyMarkedException(String message)
    {
        super(message);
    }
}
