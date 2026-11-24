package com.collegemanagement.Controller;

import com.collegemanagement.DTO.Admin.AdminLoginDTO;
import com.collegemanagement.DTO.Admin.EditAdminDTO;
import com.collegemanagement.DTO.Staff.AssignStaffsDTO;
import com.collegemanagement.DTO.Staff.StaffDTO;
import com.collegemanagement.DTO.Staff.UpdateStaffDTO;
import com.collegemanagement.DTO.Staff.ViewStaffDTO;
import com.collegemanagement.DTO.Student.StudentAttendancePercentageDTO;
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class AdminController
{
    @Autowired
    AdminService adminService;
    public AdminController(AdminService adminService)
    {
        this.adminService = adminService;
    }

    @PostMapping("admin/login")
   public ResponseEntity<String>login(@Valid @RequestBody AdminLoginDTO adminLoginDTO)
   {
       System.out.println("Controller reached");
       return new ResponseEntity<>(adminService.login(adminLoginDTO), HttpStatus.OK);
   }

    @PostMapping("admin/createStaff")
    public ResponseEntity<String>createStaff(@Valid @RequestBody StaffDTO staffDTO)
    {
        return  new ResponseEntity<>(adminService.createStaff(staffDTO),HttpStatus.CREATED);
    }

    @GetMapping("admin/manageStaffs/viewStaffs")
    public ResponseEntity<List<StaffDTO>>viewStaffs()
    {
        return new ResponseEntity<>(adminService.viewStaffs(),HttpStatus.OK);
    }

    @GetMapping("admin/manageStaffs/viewStaffsById/{id}")
    public ResponseEntity<ViewStaffDTO>viewStaffsById(@PathVariable int id)
    {
        return new ResponseEntity<>(adminService.viewStaffsById(id),HttpStatus.OK);
    }

    @PatchMapping("admin/manageStaffs/editStaffs/{id}")
    public ResponseEntity<String>editStaffs(@PathVariable int id,@RequestBody UpdateStaffDTO updateStaffDTO)
    {
        return new ResponseEntity<>(adminService.editStaffs(id,updateStaffDTO),HttpStatus.OK);
    }

    @PatchMapping("admin/manageStaffs/assignStaffs/{id}")
    public ResponseEntity<String>assignStaffs(@PathVariable int id,@RequestBody AssignStaffsDTO assignStaffsDTO)
    {
        return new ResponseEntity<>(adminService.assignStaffs(id,assignStaffsDTO),HttpStatus.OK);
    }

    @GetMapping("admin/viewStudentsAttendancePercentage")
    public ResponseEntity<List<StudentAttendancePercentageDTO>>viewStudentsPercentage(@RequestParam Year year)
    {
        return new ResponseEntity<>(adminService.viewStudentsAttendancePercentage(year),HttpStatus.OK);
    }

    @GetMapping("admin/viewStudentsBelow75Percentage")
    public ResponseEntity<List<StudentAttendancePercentageDTO>>viewStudentsBelow75Percentage(@RequestParam Year year)
    {
        return new ResponseEntity<>(adminService.viewStudentsBelow75Percentage(year),HttpStatus.OK);
    }

    @PatchMapping("admin/editProfile")
    public ResponseEntity<String>editProfile( @Valid @RequestBody EditAdminDTO editAdminDTO)
    {
        return new ResponseEntity<>(adminService.editProfile(editAdminDTO),HttpStatus.OK);
    }

}
