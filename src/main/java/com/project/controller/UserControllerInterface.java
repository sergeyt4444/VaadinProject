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

@FeignClient(name = "spring-backend", contextId = "1",decode404 = true)
@RestController
@PreAuthorize("hasRole('USER')")
public interface UserControllerInterface {

    @GetMapping("/user/main_categories")
    public ResponseEntity<List<Obj>> getMainCategories(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("/user/main_categories_count")
    public ResponseEntity<Integer> getMainCategoriesCount();

    @GetMapping("/user/users")
    public ResponseEntity<List<Obj>> getUsers();

    @GetMapping("/user/category/{name}")
    public ResponseEntity<Obj> getCategoryByName(@PathVariable(value = "name")String name);

    @GetMapping("/user/categories/{pid}")
    public ResponseEntity<List<Obj>> getSubCategories(@PathVariable(value = "pid")Integer parentId);

    @GetMapping("/user/object_by_id/{id}")
    public ResponseEntity<Obj> getObjectById(@PathVariable(value = "id")Integer id);

    @GetMapping("/user/courses/{pid}")
    public ResponseEntity<List<Obj>> getCourses(@PathVariable(value = "pid")Integer parentId,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("/user/filteredcourses/{pid}")
    public ResponseEntity<List<Obj>> getFilteredCourses(@PathVariable(value = "pid")Integer parentId,
                                                        @RequestParam List<String> difficulties,
                                                        @RequestParam List<String> languages,
                                                        @RequestParam List<String> formats,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("user/courses_count/{pid}")
    public ResponseEntity<Integer> getCoursesCount(@PathVariable(value = "pid")Integer parentId);

    @GetMapping("user/filteredcourses_count/{pid}")
    public ResponseEntity<Integer> getFilteredCoursesCount(@PathVariable(value = "pid")Integer parentId,
                                                           @RequestParam List<String> difficulties,
                                                           @RequestParam List<String> languages,
                                                           @RequestParam List<String> formats);

    @GetMapping("user/users/{name}")
    public ResponseEntity<Obj> getUser(@PathVariable (value = "name") String username);

    @PutMapping("user/usercourses")
    public ResponseEntity addUserCourse(@RequestBody Map<String, String> mappedObjAttr);

    @GetMapping("user/usercourses/{name}")
    public ResponseEntity<List<Obj>> getUserCourses(@PathVariable (value = "name") String username,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("user/usercourses/{name}/count")
    public ResponseEntity<Integer> getUserCoursesCount(@PathVariable (value = "name") String username);

    @GetMapping("user/latest_courses")
    public ResponseEntity<List<Obj>> getLatestCourses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("user/courses_count")
    public ResponseEntity<Integer> getCoursesCount();

    @GetMapping("/user/search")
    public ResponseEntity<List<Map<Integer, String>>> searchCourses(@RequestParam String searchQuery,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("/user/search_count")
    public ResponseEntity<Integer> countSearchCourses(@RequestParam String searchQuery);

    @GetMapping("/user/categories")
    public ResponseEntity<List<Obj>> getCategories();

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@RequestBody Map<Integer, String> mappedObj);

    @PostMapping("/user/mail")
    public ResponseEntity sendMailNotifications(@RequestBody Integer courseId);

}
