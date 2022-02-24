package com.project.controller;

import com.project.entity.*;
import com.project.tools.ObjectConverter;
import com.sun.jersey.api.NotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/filteredcourses/{pid}")
    public ResponseEntity<List<Obj>> getFilteredCourses(@PathVariable(value = "pid")Integer parentId,
                                                        @RequestParam List<String> difficulties,
                                                        @RequestParam List<String> languages,
                                                        @RequestParam List<String> formats);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/categories")
    public ResponseEntity createCategory(@RequestBody Map<Integer, String> mappedObj);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/courses")
    public ResponseEntity createCourse(@RequestBody Map<Integer, String> mappedObj);

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/courses")
    public ResponseEntity editCourse(@RequestBody List<Map<String, String>> mappedObjAttrs);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/attributes")
    public ResponseEntity<List<Attribute>> getAttributes();

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/attributes/{id}")
    public ResponseEntity<List<Attribute>> getAttributesByObjTypeId(@PathVariable(value = "id")Integer objTypeId);

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/api/objattrs")
    public ResponseEntity createObjAttr(@RequestBody Map<String, String> mappedObjAttr);

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/api/objattrs")
    public ResponseEntity changeObjAttr(@RequestBody Map<String, String> mappedObjAttr);

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("api/objattrs/{id}")
    public Map<String, Boolean> deleteObjAttr(@PathVariable (value = "id")Integer id);

    @PreAuthorize("hasRole('USER')")
    @GetMapping("api/users/{name}")
    public ResponseEntity<Obj> getUser(@PathVariable (value = "name") String username);

    @PreAuthorize("hasRole('USER')")
    @PutMapping("api/usercourses")
    public ResponseEntity addUserCourse(@RequestBody Map<String, String> mappedObjAttr);

    @PreAuthorize("hasRole('USER')")
    @GetMapping("api/usercourses/{name}")
    public ResponseEntity<List<Obj>> getUserCourses(@PathVariable (value = "name") String username);

}
