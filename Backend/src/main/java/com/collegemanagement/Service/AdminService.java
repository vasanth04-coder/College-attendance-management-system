package com.collegemanagement.Service;

import com.collegemanagement.DTO.Admin.AdminLoginDTO;
import com.collegemanagement.DTO.Admin.EditAdminDTO;
import com.collegemanagement.DTO.Staff.AssignStaffsDTO;
import com.collegemanagement.DTO.Staff.StaffDTO;
import com.collegemanagement.DTO.Staff.UpdateStaffDTO;
import com.collegemanagement.DTO.Staff.ViewStaffDTO;
import com.collegemanagement.DTO.Student.StudentAttendancePercentageDTO;
import com.collegemanagement.Exception.AccessDeniedException;
import com.collegemanagement.Exception.DepartmentNotFoundException;
import com.collegemanagement.Exception.EmailAlreadyExists;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.Department;
import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.DepartmentRepo;
import com.collegemanagement.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService
{
   @Autowired
   UserRepo userRepo;
   AuthenticationManager authenticationManager;
   JwtService jwtService;
   DepartmentRepo departmentRepo;
   PasswordEncoder passwordEncoder;
   AttendanceService attendanceService;

   public AdminService(UserRepo userRepo, AuthenticationManager authenticationManager, JwtService jwtService,DepartmentRepo departmentRepo,PasswordEncoder passwordEncoder,AttendanceService attendanceService)
   {
     this.userRepo = userRepo;
     this.authenticationManager = authenticationManager;
     this.jwtService = jwtService;
     this.departmentRepo = departmentRepo;
     this.passwordEncoder = passwordEncoder;
     this.attendanceService = attendanceService;
   }

   public String login(AdminLoginDTO adminLoginDTO)
   {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      adminLoginDTO.getEmail(),
                      adminLoginDTO.getPassword()
              )
      );

      boolean isAdmin = authentication.getAuthorities()
              .stream()
              .anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN"));

      if(!isAdmin)
      {
         throw new BadCredentialsException("Admins only Login here");
      }

      return jwtService.generateToken(authentication);
   }

   private User getLoggedInAdmin()
   {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       User admin = userRepo.findByEmail(authentication.getName()).orElseThrow(()->new UserNotFoundException("User Not Found"));

       if(admin.getRole()!=Role.ROLE_ADMIN)
       {
           throw new AccessDeniedException("Admin only Can Access");
       }

       return admin;
   }

   public String createStaff(StaffDTO staffDTO)
   {
      Department department = departmentRepo.findByName(staffDTO.getDepartment())
              .orElseThrow(()->new DepartmentNotFoundException("Department Not Found"));

       User user = new User();
       user.setName(staffDTO.getName());
       user.setEmail(staffDTO.getEmail());
       user.setPassword(passwordEncoder.encode(staffDTO.getPassword()));
       user.setDepartment(department);
       user.setRole(Role.ROLE_STAFF);
       user.setQualification(staffDTO.getQualification());
       user.setStatus(Status.ACTIVE);

       userRepo.save(user);

       return "Staff Created Successfully";
   }

    public List<StaffDTO> viewStaffs()
    {
        User admin = getLoggedInAdmin();
        Department department = admin.getDepartment();

       List<User> users = userRepo.findByRoleAndDepartment(Role.ROLE_STAFF,department);

       List<StaffDTO> staffs = new ArrayList<>();

       for(User user :users)
       {
          StaffDTO staffDTO = new StaffDTO();

          staffDTO.setId(user.getId());
          staffDTO.setName(user.getName());
          staffDTO.setEmail(user.getEmail());
          staffDTO.setDepartment(user.getDepartment().getName());
          staffDTO.setStatus(user.getStatus());
          staffDTO.setQualification(user.getQualification());

          staffs.add(staffDTO);
       }
       return staffs;
    }

   public ViewStaffDTO viewStaffsById(int id)
   {
       User admin = getLoggedInAdmin();
       Department department = admin.getDepartment();

       User user = userRepo.findByIdAndRoleAndStatus(id,Role.ROLE_STAFF,Status.ACTIVE).orElseThrow(()->new UserNotFoundException("User Not Found"));

       if(user.getDepartment().getId()!=department.getId())
       {
         throw new AccessDeniedException("You can only access users from your department");
       }

       ViewStaffDTO staffDTO = new ViewStaffDTO();

       staffDTO.setId(user.getId());
       staffDTO.setName(user.getName());
       staffDTO.setDepartment(user.getDepartment().getName());
       staffDTO.setYear(user.getYear());
       staffDTO.setQualification(user.getQualification());

       return staffDTO;
   }

   public String editStaffs(int id, UpdateStaffDTO updateStaffDTO)
   {
       User user = userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));

       if(updateStaffDTO.getDepartment()!=null)
       {
           Department department = departmentRepo.findByName(updateStaffDTO.getDepartment())
                   .orElseThrow(()->new DepartmentNotFoundException("Department Not Found"));

           user.setDepartment(department);
       }
       if(updateStaffDTO.getStatus()!=null)
       {
           user.setStatus(updateStaffDTO.getStatus());
       }

       userRepo.save(user);
       return  "Updated Successfully";
   }

      public String assignStaffs(int id,AssignStaffsDTO assignStaffsDTO)
      {
          User admin = getLoggedInAdmin();
          Department department = admin.getDepartment();

         User user = userRepo.findByIdAndRoleAndStatus(id,Role.ROLE_STAFF,Status.ACTIVE).orElseThrow(()->new UserNotFoundException("User Not Found"));

         if(user.getDepartment().getId()!=department.getId())
         {
            throw new AccessDeniedException("You can only access users from your department");
         }

         user.setYear(assignStaffsDTO.getYear());
         userRepo.save(user);
         return "Staffs Assigned Successfully";
      }

      public List<StudentAttendancePercentageDTO> viewStudentsAttendancePercentage(Year year)
      {
          User admin = getLoggedInAdmin();
          Department department = admin.getDepartment();

          List<StudentAttendancePercentageDTO> students = new ArrayList<>();

          List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,department,Status.ACTIVE,Role.ROLE_STUDENT);

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

    public List<StudentAttendancePercentageDTO> viewStudentsBelow75Percentage(Year year)
    {
        User admin = getLoggedInAdmin();

        Department department = admin.getDepartment();

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

    public String editProfile(EditAdminDTO editAdminDTO)
    {
       User admin = getLoggedInAdmin();

       if(editAdminDTO.getName()!=null)
       {
           admin.setName(editAdminDTO.getName());
       }

       if(editAdminDTO.getEmail()!=null && !editAdminDTO.getEmail().equals( admin.getEmail()));
       {
           if(userRepo.existsByEmail(editAdminDTO.getEmail()))
           {
              throw new EmailAlreadyExists("Email Already Exists");
           }
           admin.setEmail(editAdminDTO.getEmail());
       }

       if(editAdminDTO.getQualification()!=null)
       {
           admin.setQualification(editAdminDTO.getQualification());
       }
       if(editAdminDTO.getPassword()!=null)
       {
           admin.setPassword(passwordEncoder.encode(editAdminDTO.getPassword()));
       }

       userRepo.save(admin);
       return "Edited Sucessfully...";
    }
}