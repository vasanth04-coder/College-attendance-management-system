package com.collegemanagement.Controller;

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
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Model.Semester;
import com.collegemanagement.Service.SuperAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class SuperAdminController
{
    @Autowired
    SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService)
    {
        this.superAdminService = superAdminService;
    }

    @PostMapping("superAdmin/login")
   public ResponseEntity<String>login(@Valid @RequestBody SuperAdminLoginDTO superAdminLoginDTO)
   {
       System.out.println("Controller Reach");
       return new ResponseEntity<>(superAdminService.login(superAdminLoginDTO), HttpStatus.OK);
   }

    @PostMapping("superAdmin/createAdmin")
    public ResponseEntity<String>createAdmin(@Valid @RequestBody AdminDTO adminDTO)
    {
        return new ResponseEntity<>(superAdminService.createAdmin(adminDTO),HttpStatus.CREATED);
    }

    @PostMapping("superAdmin/createDepartment")
    public ResponseEntity<String>createDepartment(@Valid @RequestBody DeptDTO deptDTO)
    {
       return  new ResponseEntity<>(superAdminService.createDepartment(deptDTO),HttpStatus.CREATED);
    }

    @GetMapping("superAdmin/manageAdmins/viewAdmins")
    public ResponseEntity<List<AdminDTO>>viewAdmins()
    {
        return new ResponseEntity<>(superAdminService.viewAdmins(),HttpStatus.OK);
    }

    @GetMapping("superAdmin/manageAdmins/viewAdmins/{id}")
    public ResponseEntity<AdminDTO>viewAdminById(@PathVariable int id)
    {
       return new ResponseEntity<>(superAdminService.viewAdminsById(id),HttpStatus.OK);
    }

    @PatchMapping("superAdmin/manageAdmins/editAdmins/{id}")
    public ResponseEntity<String>editAdmins(@PathVariable int id,@RequestBody UpdateAdminDTO adminDTO)
    {
        return new ResponseEntity<>(superAdminService.editAdmins(id,adminDTO),HttpStatus.OK);
    }

    @PostMapping("superAdmin/createSemester")
    public ResponseEntity<String>createSem(@Valid @RequestBody Semester semester)
    {
        return new ResponseEntity<>(superAdminService.createSemester(semester),HttpStatus.CREATED);
    }

    @PostMapping("superAdmin/createAcademicYear")
    public ResponseEntity<String>createAcademicSem(@Valid @RequestBody AcademicSemDTO academicSemDTO)
    {
        return new ResponseEntity<>(superAdminService.createAcademicSem(academicSemDTO),HttpStatus.CREATED);
    }

    @PatchMapping("superAdmin/academicSemester/{id}/endDate")
    public ResponseEntity<String>setEndDate(@Valid @RequestBody int id, EndDateDTO endDateDTO)
    {
        return new ResponseEntity<>(superAdminService.setEndDate(id,endDateDTO),HttpStatus.CREATED);
    }

    @GetMapping("superAdmin/viewAllData/students/{year}/{department}")
    public ResponseEntity<StudentCountDTO>viewStudents(@PathVariable Year year, @PathVariable  String department)
    {
        return new ResponseEntity<>(superAdminService.viewStudents(year,department),HttpStatus.OK);
    }

    @GetMapping("superAdmin/viewAllData/viewStudentById/{id}")
    public ResponseEntity<ViewStudentDTO> viewStudentById(@PathVariable int id)
    {
        return new ResponseEntity<>(superAdminService.viewStudentById(id),HttpStatus.OK);
    }

    @GetMapping("superAdmin/viewAllData/staffs/{department}")
    public ResponseEntity <List<ViewStaffDTO>> viewStaffs(@PathVariable String department)
    {
        return new ResponseEntity<>(superAdminService.viewStaffs(department),HttpStatus.OK);
    }

    @GetMapping("superAdmin/viewAllData/viewStaffById/{id}")
    public ResponseEntity<ViewStaffDTO> viewStaffById(@PathVariable int id)
    {
        return new ResponseEntity<>(superAdminService.viewStaffById(id),HttpStatus.OK);
    }

    @GetMapping("superAdmin/viewStudentsAttendancePercentage")
    public ResponseEntity<List<StudentAttendancePercentageDTO>>viewStudentAttendancePercentage(@RequestParam Year year,@RequestParam String department)
    {
        return new ResponseEntity<>(superAdminService.viewStudentsAttendancePercentage(year,department),HttpStatus.OK);
    }

    @GetMapping("superAdmin/viewStudentsBelow75Percentage")
    public ResponseEntity<List<StudentAttendancePercentageDTO>>viewStudentsBelow75Percentage(@RequestParam Year year,@RequestParam String department)
    {
        return new ResponseEntity<>(superAdminService.viewStudentsBelow75Percentage(year,department),HttpStatus.OK);
    }
}
