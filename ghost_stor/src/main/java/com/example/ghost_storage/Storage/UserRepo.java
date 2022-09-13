package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserById(Long id);
    User findByUsername(String username);
    User findByUsernameOrEmail(String name, String email);
    User findByActivationCode(String code);
    User findByEmail(String email);
}
