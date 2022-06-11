package com.example.ghost_storage.Model;

import org.springframework.security.core.GrantedAuthority;

public enum CompanyRole implements GrantedAuthority {
    USER, EDITOR, ADMIN, REQUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
