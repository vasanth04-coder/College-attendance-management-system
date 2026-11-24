package com.collegemanagement.Service;

import com.collegemanagement.DTO.Staff.StaffLoginDTO;
import com.collegemanagement.DTO.Student.StudentAttendancePercentageDTO;
import com.collegemanagement.DTO.Student.ViewStudentDTO;
import com.collegemanagement.Exception.AccessDeniedException;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.Department;
import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffService
{
    @Autowired
    UserRepo userRepo;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    AttendanceService attendanceService;


    public StaffService(UserRepo userRepo,AuthenticationManager authenticationManager,JwtService jwtService,AttendanceService attendanceService)
    {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.attendanceService = attendanceService;
    }

    public String login(StaffLoginDTO staffLoginDTO)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        staffLoginDTO.getEmail(),
                        staffLoginDTO.getPassword()
                )
        );

          boolean isStaff =  authentication.getAuthorities()
                   .stream()
                   .anyMatch(auth->auth.getAuthority().equals("ROLE_STAFF"));


          if(!isStaff)
          {
              throw new BadCredentialsException("Staff only Login here");
          }

         return  jwtService.generateToken(authentication);
    }

    private User getLoggedInStaff()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User staff =  userRepo.findByEmail(authentication.getName()).orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(staff.getRole()!=Role.ROLE_STAFF)
        {
            throw new AccessDeniedException("Only Staff can Access");
        }
        return staff;
    }

    public List<ViewStudentDTO> viewPendingStudents()
    {
        User staff = getLoggedInStaff();
        Department department = staff.getDepartment();
        Year year = staff.getYear();

        List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,department,Status.INACTIVE,Role.ROLE_STUDENT);

        if(users.isEmpty())
        {
            throw new UserNotFoundException("No Pending Students..");
        }

        ArrayList<ViewStudentDTO> pendingStudents = new ArrayList<>();

        for(User user:users)
        {
              ViewStudentDTO student = new ViewStudentDTO();

                student.setId(user.getId());
                student.setRegisterNumber(user.getRegisterNumber());
                student.setName(user.getName());
                student.setDepartment(user.getDepartment().getName());
                student.setYear(user.getYear());
                student.setStudentBatch(user.getStudentBatch());

               pendingStudents.add(student);

        }

        return pendingStudents;
    }

    public ViewStudentDTO viewPendingStudentById(int id)
    {
        User staff =  getLoggedInStaff();

        Department department = staff.getDepartment();
        Year year = staff.getYear();

        User user = userRepo.findByIdAndDepartmentAndYearAndStatusAndRole(id,department,year,Status.INACTIVE, Role.ROLE_STUDENT).orElseThrow(()->new UserNotFoundException("User Not found"));

        ViewStudentDTO student = new ViewStudentDTO();

        student.setId(user.getId());
        student.setRegisterNumber(user.getRegisterNumber());
        student.setName(user.getName());
        student.setDepartment(user.getDepartment().getName());
        student.setYear(user.getYear());
        student.setStudentBatch(user.getStudentBatch());

        return student;
    }

    @Transactional
    public String approveStudentsById(int id)
    {
       User staff = getLoggedInStaff();
       Year year = staff.getYear();
       Department department = staff.getDepartment();

       User user = userRepo.findByIdAndDepartmentAndYearAndStatusAndRole(id,department,year,Status.INACTIVE,Role.ROLE_STUDENT).orElseThrow(()->new UserNotFoundException("User Not Found.."));

        user.setStatus(Status.ACTIVE);

        userRepo.save(user);

        return "Approvel Successfully..";
    }

    public List<StudentAttendancePercentageDTO>viewStudentsAttendancePercentage()
    {
        User staff = getLoggedInStaff();

        Department department = staff.getDepartment();
        Year year = staff.getYear();

        List<StudentAttendancePercentageDTO>students = new ArrayList<>();

        List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,department,Status.ACTIVE,Role.ROLE_STUDENT);

        for(User student:users)
        {
          StudentAttendancePercentageDTO studentAttendancePercentageDTO  = new StudentAttendancePercentageDTO();

          double percentage = attendanceService.calculateAttendancePercentage(student,student.getAcademicSemester());

          studentAttendancePercentageDTO.setId(student.getId());
          studentAttendancePercentageDTO.setName(student.getName());
          studentAttendancePercentageDTO.setRegisterNumber(student.getRegisterNumber());
          studentAttendancePercentageDTO.setAttendancePercentage(percentage);

          students.add(studentAttendancePercentageDTO);
        }
          return students;
    }

    public List<StudentAttendancePercentageDTO> viewStudentsBelow75Percentage()
    {
       User staff = getLoggedInStaff();

       Department department = staff.getDepartment();
       Year year = staff.getYear();

       List<StudentAttendancePercentageDTO> students = new ArrayList<>();

       List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,department,Status.ACTIVE,Role.ROLE_STUDENT);

       for(User student : users)
       {
           double percentage = attendanceService.calculateAttendancePercentage(student,student.getAcademicSemester());

           if(percentage<75.0)
           {
               StudentAttendancePercentageDTO studentAttendancePercentageDTO = new StudentAttendancePercentageDTO();

               studentAttendancePercentageDTO.setId(student.getId());
               studentAttendancePercentageDTO.setName(student.getName());
               studentAttendancePercentageDTO.setRegisterNumber(student.getRegisterNumber());
               studentAttendancePercentageDTO.setAttendancePercentage(percentage);

               students.add(studentAttendancePercentageDTO);
           }
       }
       return students;
    }

}
