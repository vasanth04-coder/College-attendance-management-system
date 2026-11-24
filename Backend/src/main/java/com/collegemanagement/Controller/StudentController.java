package com.collegemanagement.Controller;

import com.collegemanagement.DTO.Student.StudentLoginDTO;
import com.collegemanagement.DTO.Student.StudentRegisterDTO;
import com.collegemanagement.Model.Enum.AttendanceStatus;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Service.AttendanceService;
import com.collegemanagement.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.LinkOption;

@RestController
@RequestMapping("users")

public class StudentController
{
    @Autowired
    StudentService studentService;
    AttendanceService attendanceService;

    public StudentController(StudentService studentService ,AttendanceService attendanceService)
    {
        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("student/register")
    public ResponseEntity<String>register(@Valid @RequestBody StudentRegisterDTO studentRegisterDTO)
    {
        return new ResponseEntity<>(studentService.studentRegister(studentRegisterDTO), HttpStatus.CREATED);
    }

    @PostMapping("student/login")
    public ResponseEntity<String>login(@Valid @RequestBody StudentLoginDTO studentLoginDTO)
    {
        return new ResponseEntity<>(studentService.studentLogin(studentLoginDTO),HttpStatus.OK);
    }

     @GetMapping("student/getCurrentPercentage")
     public ResponseEntity<Double>getCurrentPercentage()
     {
         return new ResponseEntity<>(studentService.getAttendancePercentage(),HttpStatus.OK);
     }

     @GetMapping("student/getTodayStatus")
     public ResponseEntity<AttendanceStatus>getTodayStatus()
     {
         return new ResponseEntity<>(studentService.getTodayStatus(),HttpStatus.OK);
     }



}
