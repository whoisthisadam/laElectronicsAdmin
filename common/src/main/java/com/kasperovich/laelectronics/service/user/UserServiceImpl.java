package com.kasperovich.laelectronics.service.user;

import com.kasperovich.laelectronics.enums.Roles;
import com.kasperovich.laelectronics.exception.BadPasswordException;
import com.kasperovich.laelectronics.models.Credentials;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.repository.AddressRepository;
import com.kasperovich.laelectronics.repository.DiscountRepository;
import com.kasperovich.laelectronics.repository.RoleRepository;
import com.kasperovich.laelectronics.repository.UserRepository;
import com.kasperovich.laelectronics.util.ValidCheck;
import com.kasperovich.laelectronics.enums.Discounts;
import com.kasperovich.laelectronics.service.role.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User createUser(@Valid User user) throws BadPasswordException {
        user.setRole(roleService.findRoleByName(Roles.USER));
        if (!new ValidCheck().isPasswordValid(user.getCredentials().getPassword())) {
            throw new BadPasswordException("Password must include at least one capital, or number, or symbol");
        }
        user.setUserDiscount(discountRepository.findDiscountByNameAndIsDeletedIsFalse(Discounts.LOGIN_DISCOUNT));
        user.setCredentials(new Credentials(user.getCredentials().getLogin(), encoder.encode(user.getCredentials().getPassword())));

        addressRepository.save(user.getAddress());
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().filter(x -> !x.getIsDeleted()).collect(Collectors.toList());
    }

    @Override
    public User updateUser(@Valid User user) {
        user.setEditData(new Edit(user.getEditData().getCreationDate(), new Timestamp(new Date().getTime())));
        addressRepository.save(user.getAddress());
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with this ID does not exist!")
        );
        user.setIsDeleted(true);
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
    }
}
