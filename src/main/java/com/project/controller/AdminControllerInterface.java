package com.project.controller;

import com.project.entity.Attribute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "spring-backend", contextId = "2", decode404 = true)
@RestController
@PreAuthorize("hasRole('ADMIN')")
public interface AdminControllerInterface {
    @PostMapping("/admin/categories")
    public ResponseEntity createCategory(@RequestBody Map<Integer, String> mappedObj);

    @PostMapping("/admin/courses")
    public ResponseEntity createCourse(@RequestBody Map<Integer, String> mappedObj);

    @PutMapping("/admin/courses")
    public ResponseEntity editCourse(@RequestBody List<Map<String, String>> mappedObjAttrs);

    @GetMapping("/admin/attributes")
    public ResponseEntity<List<Attribute>> getAttributes();

    @GetMapping("/admin/attributes/{id}")
    public ResponseEntity<List<Attribute>> getAttributesByObjTypeId(@PathVariable(value = "id")Integer objTypeId);
}
