package com.collegemanagement.Repository;

import com.collegemanagement.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Integer>
{
   Optional<Department> findByName(String dept);
}
