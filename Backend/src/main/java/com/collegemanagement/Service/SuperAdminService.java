package com.collegemanagement.Service;

import com.collegemanagement.DTO.AcademicSemDTO;
import com.collegemanagement.DTO.Admin.AdminDTO;
import com.collegemanagement.DTO.DeptDTO;
import com.collegemanagement.DTO.EndDateDTO;
import com.collegemanagement.DTO.Staff.ViewStaffDTO;
import com.collegemanagement.DTO.Student.StudentAttendancePercentageDTO;
import com.collegemanagement.DTO.Student.StudentCountDTO;
import com.collegemanagement.DTO.Student.ViewStudentDTO;
import com.collegemanagement.DTO.SuperAdmin.SuperAdminLoginDTO;
import com.collegemanagement.DTO.Admin.UpdateAdminDTO;
import com.collegemanagement.Exception.AccessDeniedException;
import com.collegemanagement.Exception.DepartmentNotFoundException;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.AcademicSemester;
import com.collegemanagement.Model.Department;
import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Model.Semester;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.AcademicSemRepo;
import com.collegemanagement.Repository.DepartmentRepo;
import com.collegemanagement.Repository.SemesterRepo;
import com.collegemanagement.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperAdminService
{
    @Autowired
    UserRepo userRepo;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    DepartmentRepo departmentRepo;
    SemesterRepo semesterRepo;
    AcademicSemRepo academicSemRepo;
    AttendanceService attendanceService;

    public SuperAdminService(UserRepo userRepo, AuthenticationManager authenticationManager,JwtService jwtService,PasswordEncoder passwordEncoder
            ,DepartmentRepo departmentRepo,SemesterRepo semesterRepo,AcademicSemRepo academicSemRepo,AttendanceService attendanceService)
    {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder= passwordEncoder;
        this.departmentRepo = departmentRepo;
        this.semesterRepo = semesterRepo;
        this.academicSemRepo = academicSemRepo;
        this.attendanceService = attendanceService;
    }

   public String login(SuperAdminLoginDTO superAdminLoginDTO)
   {
       System.out.println("Authentication reach");

       Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                       superAdminLoginDTO.getEmail(),
                       superAdminLoginDTO.getPassword()
               )
       );

      boolean isSuperAdmin = authentication.getAuthorities()
              .stream()
              .anyMatch(auth->auth.getAuthority().equals("ROLE_SUPER_ADMIN"));


      if(!isSuperAdmin)
      {
          throw new BadCredentialsException("Only SuperAdmin can Login");
      }

       return jwtService.generateToken(authentication);
   }

   public String createAdmin(AdminDTO adminDTO)
   {
       Department department = departmentRepo.findByName(adminDTO.getDepartment())
               .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));

       User user = new User();
       user.setName(adminDTO.getName());
       user.setEmail(adminDTO.getEmail());
       user.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
       user.setRole(Role.ROLE_ADMIN);
       user.setDepartment(department);
       user.setStatus(Status.ACTIVE);
       user.setQualification(adminDTO.getQualification());
       userRepo.save(user);
       return "Created Admin Successfully";
   }

    public String createDepartment(DeptDTO deptDTO)
    {
        Department department = new Department();
        department.setName(deptDTO.getName());
        departmentRepo.save(department);
        return "Created Department successfully";
    }

     public List<AdminDTO> viewAdmins()
     {
         List<User> users = userRepo.findByRole(Role.ROLE_ADMIN);

         List<AdminDTO> admins = new ArrayList<>();

         for(User user : users)
         {
             AdminDTO adminDTO = new AdminDTO();
             adminDTO.setId(user.getId());
             adminDTO.setName(user.getName());
             adminDTO.setEmail(user.getEmail());
             adminDTO.setDepartment(user.getDepartment().getName());
             adminDTO.setQualification(user.getQualification());
             adminDTO.setStatus(user.getStatus());

             admins.add(adminDTO);
         }
         return admins;
     }

     public AdminDTO viewAdminsById(int id)
     {
         User user = userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));

         if(user.getRole()!=Role.ROLE_ADMIN)
         {
             throw new AccessDeniedException("Access Denied");
         }

         AdminDTO adminDTO = new AdminDTO();

         adminDTO.setId(user.getId());
         adminDTO.setName(user.getName());
         adminDTO.setEmail(user.getEmail());
         adminDTO.setDepartment(user.getDepartment().getName());
         adminDTO.setQualification(user.getQualification());
         adminDTO.setStatus(user.getStatus());

         return adminDTO;
     }

    public String editAdmins(int id, UpdateAdminDTO adminDTO)
    {
       User user = userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));

       if(adminDTO.getDepartment()!=null)
       {
           Department department = departmentRepo.findByName(adminDTO.getDepartment()).orElseThrow(()->new DepartmentNotFoundException("Department not found"));
           user.setDepartment(department);
       }
       if(adminDTO.getStatus()!=null)
       {
           user.setStatus(adminDTO.getStatus());
       }
       userRepo.save(user);
       return "Updated Successfully";
    }

    public String createSemester(Semester semester)
    {
       Semester sem = new Semester();
       sem.setName(semester.getName());
       sem.setType(semester.getType());
       sem.setYear(semester.getYear());

       semesterRepo.save(sem);
       return "Created Semester Successfully";
    }

    public String createAcademicSem(AcademicSemDTO academicSemDTO)
    {
        Semester semester = semesterRepo.findByName(academicSemDTO.getSemName()).orElseThrow(()->new RuntimeException("Sem Not Found"));

        AcademicSemester academicSemester = new AcademicSemester();

        academicSemester.setAcademicYear(academicSemDTO.getAcademicYear());
        academicSemester.setSemester(semester);
        academicSemester.setStartDate(academicSemDTO.getStartDate());

         academicSemRepo.save(academicSemester);

         return "Academic Semester Created Successfully";
    }

    public String setEndDate(int id,EndDateDTO endDateDTO)
    {
       AcademicSemester academicSemester = academicSemRepo.findById(id).orElseThrow(()->new RuntimeException("Sem Not Found"));
       academicSemester.setEndDate(endDateDTO.getEndDate());
       academicSemRepo.save(academicSemester);
       return "End Date Updated Successfully";
    }

    public StudentCountDTO viewStudents(Year year, String department)
    {
        Department dept = departmentRepo.findByName(department).orElseThrow(()-> new DepartmentNotFoundException("Department Not Found"));

        List<User>users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,dept,Status.ACTIVE,Role.ROLE_STUDENT);

        ArrayList<ViewStudentDTO> students = new ArrayList<>();

        for(User user : users)
        {
            ViewStudentDTO student = new ViewStudentDTO();

             student.setId(user.getId());
             student.setRegisterNumber(user.getRegisterNumber());
             student.setName(user.getName());
             student.setDepartment(user.getDepartment().getName());
             student.setYear(user.getYear());
             student.setStudentBatch(user.getStudentBatch());

            students.add(student);
        }

        int totalStudent = students.size();

        return new StudentCountDTO(totalStudent,students);
    }

    public ViewStudentDTO viewStudentById(int id)
    {
      User user = userRepo.findByIdAndRoleAndStatus(id,Role.ROLE_STUDENT,Status.ACTIVE).orElseThrow(()->new UserNotFoundException("User Not Found.."));

       ViewStudentDTO student = new ViewStudentDTO();

        student.setId(user.getId());
        student.setRegisterNumber(user.getRegisterNumber());
        student.setName(user.getName());
        student.setDepartment(user.getDepartment().getName());
        student.setYear(user.getYear());
        student.setStudentBatch(user.getStudentBatch());

        return student;
    }

    public List<ViewStaffDTO>viewStaffs(String department)
    {
        Department dept = departmentRepo.findByName(department).orElseThrow(()->new DepartmentNotFoundException("Department Not found"));

        List<User> users = userRepo.findByRoleAndDepartmentAndStatus(Role.ROLE_STAFF,dept,Status.ACTIVE);

        ArrayList<ViewStaffDTO>staffs = new ArrayList<>();

        for(User user:users)
        {
            ViewStaffDTO staff = new ViewStaffDTO();

            staff.setId(user.getId());
            staff.setName(user.getName());
            staff.setDepartment(user.getDepartment().getName());
            staff.setYear(user.getYear());
            staff.setQualification(user.getQualification());

            staffs.add(staff);
        }
        return staffs;
    }

    public ViewStaffDTO viewStaffById(int id)
    {
       User user = userRepo.findByIdAndRoleAndStatus(id,Role.ROLE_STAFF,Status.ACTIVE).orElseThrow(()->new UserNotFoundException("User Not Found.."));

        ViewStaffDTO staff = new ViewStaffDTO();

        staff.setId(user.getId());
        staff.setName(user.getName());
        staff.setDepartment(user.getDepartment().getName());
        staff.setYear(user.getYear());
        staff.setQualification(user.getQualification());

        return staff;
    }

    public List<StudentAttendancePercentageDTO> viewStudentsAttendancePercentage(Year year,String department)
    {
        Department dept = departmentRepo.findByName(department).orElseThrow(()->new DepartmentNotFoundException("Department not Found"));

        List<StudentAttendancePercentageDTO> students = new ArrayList<>();

        List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,dept,Status.ACTIVE,Role.ROLE_STUDENT);

        for(User student:users)
        {
            StudentAttendancePercentageDTO studentAttendancePercentageDTO = new StudentAttendancePercentageDTO();
            double percentage = attendanceService.calculateAttendancePercentage(student,student.getAcademicSemester());

            studentAttendancePercentageDTO.setId(student.getId());
            studentAttendancePercentageDTO.setRegisterNumber(student.getRegisterNumber());
            studentAttendancePercentageDTO.setName(student.getName());
            studentAttendancePercentageDTO.setAttendancePercentage(percentage);

            students.add(studentAttendancePercentageDTO);
        }

        return students;
    }

    public List<StudentAttendancePercentageDTO> viewStudentsBelow75Percentage(Year year,String department)
    {
        Department dept= departmentRepo.findByName(department).orElseThrow(()->new DepartmentNotFoundException("Department Not Found"));

        List<StudentAttendancePercentageDTO> students = new ArrayList<>();

        List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,dept,Status.ACTIVE,Role.ROLE_STUDENT);

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
