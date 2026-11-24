package com.collegemanagement.Repository;

import com.collegemanagement.Model.Enum.SemName;
import com.collegemanagement.Model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepo extends JpaRepository<Semester, Integer>
{
    Optional<Semester>findByName(SemName semName);
}
