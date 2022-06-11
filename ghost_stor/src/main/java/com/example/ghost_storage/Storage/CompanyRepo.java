package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.Company;
import com.example.ghost_storage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface CompanyRepo extends JpaRepository<Company, Long> {
    Set<Company> findAllByIdNotNull();
    Company findCompanyByName(String companyName);
}
