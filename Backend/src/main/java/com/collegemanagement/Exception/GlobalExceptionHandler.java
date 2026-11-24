package com.collegemanagement.Exception;

import com.collegemanagement.Model.Attendance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String>handleWrongEmails(UsernameNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String>handleWrongPassword(BadCredentialsException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Password");
    }

    @ExceptionHandler (IllegalStateException.class)
    public ResponseEntity<String>handleIllegalState(IllegalStateException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User has no Authority");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String>handleUserNotFound(UserNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getLocalizedMessage());
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<String>handleDepartmentNotfound(DepartmentNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department Not Found");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String>handleAccessDenied(AccessDeniedException e)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only access users from your department");
    }

    @ExceptionHandler(InactiveAccException.class)
    public ResponseEntity<String>handleAccInactive(InactiveAccException e)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your Account is Inactive");
    }

    @ExceptionHandler(AttendanceAlreadyMarkedException.class)
    public ResponseEntity<String>handleDuplicateAttendance(AttendanceAlreadyMarkedException e)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Attendance Already Marked..");
    }

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<String>handleDuplicateEmails(EmailAlreadyExists e)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email Already Exists");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>handlesValidation(MethodArgumentNotValidException e)
    {
        List<ErrorDetails> errors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(error->
                {
                  ErrorDetails errorDetails = new ErrorDetails(error.getField(),error.getDefaultMessage());
                  errors.add(errorDetails);
                }
        );

        ErrorResponse errorResponse = new ErrorResponse(404,"Not Valid",errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}

