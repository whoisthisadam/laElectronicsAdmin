package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Role;
import com.kasperovich.laelectronics.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findAllByName(Roles name);

}
