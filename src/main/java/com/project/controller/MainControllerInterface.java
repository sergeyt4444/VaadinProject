package com.project.controller;

import com.project.entity.Attribute;
import com.project.entity.Obj;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/api/main_categories")
    public ResponseEntity<List<Obj>> getMainCategories();

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/courses")
    public ResponseEntity createObj(@RequestBody Map<Integer, String> mappedObj);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/attributes")
    public ResponseEntity<List<Attribute>> getAttributes();

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/attributes/{id}")
    public ResponseEntity<List<Attribute>> getAttributesByObjTypeId(@PathVariable(value = "id")Integer objTypeId);

}
