package com.kasperovich.laelectronics.service.role;

import com.kasperovich.laelectronics.enums.Roles;
import com.kasperovich.laelectronics.models.Role;

import java.util.List;

public interface RoleService {

    Role findRoleByName(Roles name);

    List<Role> findAll();

    Long deleteById(Long id);

    Role createRole(Role role);

}
