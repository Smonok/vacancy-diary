package pro.inmost.vacancydiary.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pro.inmost.vacancydiary.model.JwtUserDetails;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User userByEmail = userService.findByEmail(s);

        return new JwtUserDetails(userByEmail);
    }
}
