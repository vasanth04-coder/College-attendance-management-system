package com.collegemanagement.Repository;

import com.collegemanagement.Model.AcademicSemester;
import com.collegemanagement.Model.Enum.SemName;
import com.collegemanagement.Model.Enum.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AcademicSemRepo extends JpaRepository<AcademicSemester,Integer>
{
    @Query(""" 
               SELECT a FROM AcademicSemester a
               WHERE a.semester.name = :semName
               AND a.semester.year = :year
               AND a.startDate <= :today
               AND (a.endDate IS NULL OR a.endDate >= :today)
            """)

    Optional<AcademicSemester>findCurrentSemester(
            @Param("semName")SemName semName,
            @Param("year")Year year,
            @Param("today")LocalDate today
            );

}
