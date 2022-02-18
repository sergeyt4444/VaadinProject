package com.project.controller;

import com.project.entity.Attribute;
import com.project.entity.Obj;
import com.project.entity.ObjectTypeEnum;
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/category/{name}")
    public ResponseEntity<Obj> getCategoryByName(@PathVariable(value = "name")String name);

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/categories/{pid}")
    public ResponseEntity<List<Obj>> getSubCategories(@PathVariable(value = "pid")Integer parentId);

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/object_by_id/{id}")
    public ResponseEntity<Obj> getObjectById(@PathVariable(value = "id")Integer id);

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/courses/{pid}")
    public ResponseEntity<List<Obj>> getCourses(@PathVariable(value = "pid")Integer parentId);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/categories")
    public ResponseEntity createCategory(@RequestBody Map<Integer, String> mappedObj);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/courses")
    public ResponseEntity createCourse(@RequestBody Map<Integer, String> mappedObj);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/attributes")
    public ResponseEntity<List<Attribute>> getAttributes();

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/attributes/{id}")
    public ResponseEntity<List<Attribute>> getAttributesByObjTypeId(@PathVariable(value = "id")Integer objTypeId);

}