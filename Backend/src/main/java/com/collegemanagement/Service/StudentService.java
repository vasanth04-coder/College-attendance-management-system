package com.collegemanagement.Service;

import com.collegemanagement.DTO.Student.StudentLoginDTO;
import com.collegemanagement.DTO.Student.StudentRegisterDTO;
import com.collegemanagement.Exception.AccessDeniedException;
import com.collegemanagement.Exception.DepartmentNotFoundException;
import com.collegemanagement.Exception.InactiveAccException;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.AcademicSemester;
import com.collegemanagement.Model.Attendance;
import com.collegemanagement.Model.Department;
import com.collegemanagement.Model.Enum.AttendanceStatus;
import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class StudentService
{
    @Autowired
    UserRepo userRepo;
    DepartmentRepo departmentRepo;
    SemesterRepo semesterRepo;
    PasswordEncoder passwordEncoder;
    AcademicSemRepo academicSemRepo;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    AttendanceService attendanceService;
    AttendanceRepo attendanceRepo;

    public StudentService(UserRepo userRepo,DepartmentRepo departmentRepo,SemesterRepo semesterRepo,PasswordEncoder passwordEncoder,AcademicSemRepo academicSemRepo,AuthenticationManager authenticationManager,JwtService jwtService,AttendanceService attendanceService,AttendanceRepo attendanceRepo)
    {
       this.userRepo = userRepo;
       this.departmentRepo = departmentRepo;
       this.semesterRepo = semesterRepo;
       this.passwordEncoder = passwordEncoder;
       this.academicSemRepo = academicSemRepo;
       this.authenticationManager = authenticationManager;
       this.jwtService = jwtService;
       this.attendanceService = attendanceService;
       this.attendanceRepo = attendanceRepo;
    }

    public String studentRegister(StudentRegisterDTO studentRegisterDTO)
    {
        Department department = departmentRepo.findByName(studentRegisterDTO.getDepartment()).orElseThrow(()->new DepartmentNotFoundException("Department Not found"));

        AcademicSemester academicSemester = academicSemRepo.findCurrentSemester
                (
                studentRegisterDTO.getSemName(),
                studentRegisterDTO.getYear(),
                LocalDate.now()
                ).orElseThrow(()->new RuntimeException("Academic sem not found"));


        User user = new User();

        user.setEmail(studentRegisterDTO.getEmail());
        user.setName(studentRegisterDTO.getName());
        user.setPassword(passwordEncoder.encode(studentRegisterDTO.getPassword()));
        user.setRegisterNumber(studentRegisterDTO.getRegisterNumber());
        user.setDepartment(department);
        user.setYear(studentRegisterDTO.getYear());
        user.setRole(Role.ROLE_STUDENT);
        user.setStatus(Status.INACTIVE);
        user.setStudentBatch(studentRegisterDTO.getStudentBatch());
        user.setAcademicSemester(academicSemester);

       userRepo.save(user);

       return "Registerd Successfully You need to Apporovel From Your Staff side";
    }

    public String studentLogin(StudentLoginDTO studentLoginDTO)
    {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                     studentLoginDTO.getEmail(),
                     studentLoginDTO.getPassword()
                )
        );


        boolean isStudent = authentication.getAuthorities()
                .stream()
                .anyMatch(auth->auth.getAuthority().equals("ROLE_STUDENT"));


        if(!isStudent)
        {
            throw new BadCredentialsException("Student only login here");
        }

        User user = userRepo.findByEmail(authentication.getName()).orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(user.getStatus() != Status.ACTIVE)
        {
            throw new InactiveAccException("Your Account is InActive..");
        }

        return jwtService.generateToken(authentication);
    }

    public User getLoggedInStudent()
    {
       Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
       User student = userRepo.findByEmail(authentication.getName()).orElseThrow(()->new UserNotFoundException("User Not found.."));

       if(student.getRole()!=Role.ROLE_STUDENT)
       {
           throw new AccessDeniedException("Student only Access");
       }
       return student;
    }

    public double getAttendancePercentage()
    {
       User student = getLoggedInStudent();
       return attendanceService.calculateAttendancePercentage(student,student.getAcademicSemester());
    }

    public AttendanceStatus getTodayStatus()
    {
        User student = getLoggedInStudent();
        LocalDate today = LocalDate.now();

        return attendanceRepo.findByStudentIdAndDate(student.getId(), today)
                .orElseThrow(() -> new RuntimeException("Attendance not recorded yet"));
    }
}
