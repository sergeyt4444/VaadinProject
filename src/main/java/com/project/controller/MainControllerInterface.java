package com.project.controller;

import com.project.entity.Obj;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FeignClient(name = "spring-backend")
@RestController
public interface MainControllerInterface {

    @GetMapping("/api/anonymous")
    public String getAnonymousInfo();

    @GetMapping("/api/user")
    @PreAuthorize("hasRole('USER')")
    public String getUserInfo();

    @GetMapping("/api/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminInfo();

    @GetMapping("/api/me")
    public Object getMe();

    @PreAuthorize("hasRole('USER')")
    @GetMapping("api/main_categories")
    public List<Obj> getMainCategories();

}
