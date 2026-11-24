package com.collegemanagement.Repository;

import com.collegemanagement.Model.Department;
import com.collegemanagement.Model.Enum.Role;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.Enum.Year;
import com.collegemanagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository <User,Integer>
{
     Optional <User> findByEmail (String email);

      List<User> findByRole(Role role);

      List<User>findByRoleAndDepartment(Role role, Department department);

      List<User>findByRoleAndDepartmentAndStatus(Role role, Department department,Status status);

      List<User>findByYearAndDepartmentAndStatusAndRole(Year year, Department department, Status status,Role role);

      Optional<User> findByIdAndDepartmentAndYearAndStatusAndRole(int id,Department department,Year year,Status status,Role role);

      Optional<User> findByIdAndRoleAndStatus(int id,Role role,Status status);

      boolean existsByEmail(String email);
}
