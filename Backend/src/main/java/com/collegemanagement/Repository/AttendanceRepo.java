package com.collegemanagement.Repository;

import com.collegemanagement.Model.Attendance;
import com.collegemanagement.Model.Enum.AttendanceStatus;
import com.collegemanagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepo extends JpaRepository<Attendance,Long>
{
    Boolean existsByStudentAndDate(User student, LocalDate today);

    Long countByStudentAndDateBetweenAndAttendanceStatus(User student, LocalDate startDate, LocalDate endDate, AttendanceStatus attendanceStatus);

    Long countByStudentAndDateBetween(User student,LocalDate startDate,LocalDate endDate);

    Optional<AttendanceStatus> findByStudentIdAndDate(int id, LocalDate date);
}
