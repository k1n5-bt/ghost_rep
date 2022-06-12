package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Company;
import com.example.ghost_storage.Model.CompanyRole;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Storage.CompanyRepo;
import com.example.ghost_storage.Storage.UserRepo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Service
public class CompanyService {
    private final UserRepo userRepo;
    private final MailSender mailSender;
    private final CompanyRepo companyRepo;

    public CompanyService(UserRepo userRepo, MailSender mailSender, CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    public Set<Company> getCompanies(){
        return companyRepo.findAllByIdNotNull();
    }

    public Set<User> getUsersRequests(Company company){
        Set<User> result = new HashSet<>();
        Set<User> allUsers = userRepo.findByCompany(company);
        allUsers.forEach(user -> {
            if (!user.isAddInCompany())
                result.add(user);
        });
        return result;
    }

    public Company findCompanyByName(String name) { return companyRepo.findCompanyByName(name); }

    public boolean createCompany(String companyName, User user){
        Company companyFromDb = companyRepo.findCompanyByName(companyName);
        if (companyFromDb != null)
            return false;
        user.setCompany_roles(Collections.singleton(CompanyRole.ADMIN));
        Company company = new Company(companyName);
        company.addUser(user);
        companyRepo.save(company);
        return true;
    }

    public Set<String> getAllowedEmails(Company company) throws IOException {
        Set<String> emails = new HashSet<>();
        URL url = new URL(String.format("http://redis_stor:8085/get_email?id=%s", company.getId()));
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        Scanner s = new Scanner(in).useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";
        for (var email : body.split(",")){
            if (!email.equals(""))
                emails.add(email);
        }
        return emails;
    }

    public void allowEmail(String email, Company company) throws IOException {
        URL url = new URL(String.format("http://redis_stor:8085/add_email?email=%s&id=%s", email, company.getId()));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.connect();

        int code = connection.getResponseCode();
    }
}
