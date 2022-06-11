package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.Company;
import com.example.ghost_storage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserById(Long id);
    User findByUsername(String username);
    User findByUsernameOrEmail(String name, String email);
    User findByActivationCode(String code);
    Set<User> findByCompany(Company company);
}
