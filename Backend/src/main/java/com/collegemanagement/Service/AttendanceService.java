package com.collegemanagement.Service;

import com.collegemanagement.DTO.MarkAttendanceDTO;
import com.collegemanagement.DTO.Student.ViewStudentDTO;
import com.collegemanagement.Exception.AccessDeniedException;
import com.collegemanagement.Exception.AttendanceAlreadyMarkedException;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.AcademicSemester;
import com.collegemanagement.Model.Attendance;
import com.collegemanagement.Model.Department;
import com.collegemanagement.Model.Enum.AttendanceStatus;
import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.AttendanceRepo;
import com.collegemanagement.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService
{

    @Autowired
    UserRepo  userRepo;
    AttendanceRepo attendanceRepo;

    public AttendanceService(UserRepo userRepo,AttendanceRepo attendanceRepo)
    {
      this.userRepo = userRepo;
      this.attendanceRepo = attendanceRepo;
    }

    private User getLoggedInStaff()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User staff =  userRepo.findByEmail(authentication.getName()).orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(staff.getRole()!= Role.ROLE_STAFF)
        {
            throw new AccessDeniedException("Only Staff can Access");
        }
        return staff;
    }

    public List<ViewStudentDTO> getStudentsForAttendance()
    {
        User staff = getLoggedInStaff();

        Department department = staff.getDepartment();
        Year year = staff.getYear();


        List<User> users = userRepo.findByYearAndDepartmentAndStatusAndRole(year,department,Status.ACTIVE,Role.ROLE_STUDENT);

        List<ViewStudentDTO> students = new ArrayList<>();

        for(User user :users)
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

        return students;
    }

     @Transactional
    public String markAttendance(List<MarkAttendanceDTO>markAttendanceDTO)
    {
        User staff = getLoggedInStaff();

        Department department = staff.getDepartment();
        Year year = staff.getYear();

        LocalDate today = LocalDate.now();

        if(markAttendanceDTO==null && markAttendanceDTO.isEmpty())
        {
            throw new IllegalArgumentException("Attendance list cannot be empty..");
        }

        for(MarkAttendanceDTO list : markAttendanceDTO)
        {
            User student = userRepo.findByIdAndDepartmentAndYearAndStatusAndRole(list.getStudentId(), department, year, Status.ACTIVE, Role.ROLE_STUDENT).orElseThrow(() -> new UserNotFoundException("User Not Found..."));

            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setDate(today);
            attendance.setAttendanceStatus(list.getAttendanceStatus());

            boolean alreadyMarked = attendanceRepo.existsByStudentAndDate(student, today);

            if (alreadyMarked)
            {
                throw new AttendanceAlreadyMarkedException("Attendance Already Marked...");
            }

            attendanceRepo.save(attendance);
        }

        return "Attendance Marked Successfully";
    }

    public double calculateAttendancePercentage(User student,AcademicSemester academicSemester)
    {
        LocalDate startDate = academicSemester.getStartDate();
        LocalDate endDate = academicSemester.getEndDate();
        LocalDate today =  LocalDate.now();

        if(endDate == null || endDate.isAfter(today))
        {
            endDate = today;
        }

        long presentCount = attendanceRepo.countByStudentAndDateBetweenAndAttendanceStatus(student,startDate,endDate,AttendanceStatus.PRESENT);
        long totalCount = attendanceRepo.countByStudentAndDateBetween(student,startDate,endDate);

        if(totalCount ==0)
        {
            return 0.0;
        }

        return (presentCount*100.0)/totalCount;
    }
}
