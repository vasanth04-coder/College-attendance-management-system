package com.collegemanagement.Controller;

import com.collegemanagement.DTO.MarkAttendanceDTO;
import com.collegemanagement.DTO.Staff.StaffLoginDTO;
import com.collegemanagement.DTO.Student.StudentAttendancePercentageDTO;
import com.collegemanagement.DTO.Student.ViewStudentDTO;
import com.collegemanagement.Service.AttendanceService;
import com.collegemanagement.Service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class StaffController
{
    @Autowired
    StaffService staffService;
    AttendanceService attendanceService;

    public StaffController(StaffService staffService,AttendanceService attendanceService)
    {
        this.staffService = staffService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("staff/login")
    public ResponseEntity<String>login(@Valid @RequestBody StaffLoginDTO staffLoginDTO)
    {
        return new ResponseEntity<>(staffService.login(staffLoginDTO), HttpStatus.OK);
    }

    @GetMapping("staff/manageStudents/viewPendingStudents")
    public ResponseEntity<List<ViewStudentDTO>>viewPendingStudents()
    {
       return new ResponseEntity<>(staffService.viewPendingStudents(),HttpStatus.OK);
    }

    @GetMapping("staff/manageStudents/viewPendingStudentById/{id}")
    public ResponseEntity<ViewStudentDTO>viewPendingStudentsById(@PathVariable int id)
    {
        return new ResponseEntity<>(staffService.viewPendingStudentById(id),HttpStatus.OK);
    }

    @PatchMapping("staff/manageStudents/approveStudentsById/{id}")
    public ResponseEntity<String>approveStudentsById(@Valid @PathVariable int id)
    {
        return new ResponseEntity<>(staffService.approveStudentsById(id),HttpStatus.OK);
    }

    @GetMapping("staff/takeAttendance")
    public ResponseEntity<List<ViewStudentDTO>>viewStudents()
    {
       return new ResponseEntity<>(attendanceService.getStudentsForAttendance(),HttpStatus.OK);
    }

    @PostMapping("staff/markAttendance")
    public ResponseEntity<String>markAttendance(@Valid @RequestBody List<MarkAttendanceDTO> markAttendanceDTO)
    {
        return new ResponseEntity<>(attendanceService.markAttendance(markAttendanceDTO),HttpStatus.OK);
    }

    @GetMapping("staff/viewStudentsAttendancePercetage")
    public ResponseEntity<List<StudentAttendancePercentageDTO>>viewStudentsAttendance()
    {
        return new ResponseEntity<>(staffService.viewStudentsAttendancePercentage(),HttpStatus.OK);
    }

    @GetMapping("staff/viewStudentsBelow75Percentage")
    public ResponseEntity<List<StudentAttendancePercentageDTO>>viewStudentsBelow75Percentage()
    {
        return new ResponseEntity<>(staffService.viewStudentsBelow75Percentage(),HttpStatus.OK);
    }

}
