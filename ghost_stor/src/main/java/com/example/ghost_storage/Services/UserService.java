package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Role;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Storage.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final MailSender mailSender;

    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String ipAddress;

    public UserService(UserRepo userRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findByEmail(s);
    }

    public boolean addUser(User user){
        User userFromDb = userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (userFromDb != null)
            return false;

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Здравствуйте, %s! \n" +
                    "Спасибо за регистрацию! Перейдите, пожалуйста, по ссылке для активации аккаунта: http://%s:%s/activate/%s",
                    user.getFullName(),
                    ipAddress,
                    port,
                    user.getActivationCode());

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }
}
