package com.food.order.system.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.order.system.entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
 Role findByRole(String role);
}
