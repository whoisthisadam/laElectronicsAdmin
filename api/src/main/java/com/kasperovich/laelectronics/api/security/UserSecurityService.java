package com.kasperovich.laelectronics.api.security;

import com.kasperovich.laelectronics.models.User;
import com.kasperovich.laelectronics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            /*Find user in DB*/
            Optional<User> searchResult = userRepository.findByCredentialsLoginAndIsDeleted(username, false);

            if (searchResult.isPresent()) {
                User user = searchResult.get();

                /*We are creating Spring Security User object*/
                return new org.springframework.security.core.userdetails.User(
                        user.getCredentials().getLogin(),
                        user.getCredentials().getPassword(),
//                        ["ROLE_USER", "ROLE_ADMIN"]
                        AuthorityUtils.commaSeparatedStringToAuthorityList(
                               "ROLE_"+user.getRole().getName().toString()+","
                        )
                );
            } else {
                throw new UsernameNotFoundException(String.format("No user found with login '%s'.", username));
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User with this login not found");
        }
    }
}
