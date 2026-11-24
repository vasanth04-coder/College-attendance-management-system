package com.collegemanagement.Exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String message)
    {
        super(message);
    }
}
