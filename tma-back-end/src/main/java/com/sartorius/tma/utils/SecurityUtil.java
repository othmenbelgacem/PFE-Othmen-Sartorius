package com.sartorius.tma.utils;

import com.sartorius.tma.security.UserDetailsImpl;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {

    public static UUID getCurrentUserUuid() {
        return ((UserDetailsImpl) (SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal())).getUuid();
    }



}