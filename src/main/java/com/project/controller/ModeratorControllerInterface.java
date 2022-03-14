package com.project.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "spring-backend", contextId = "3",decode404 = true)
@RestController
@PreAuthorize("hasRole('MODERATOR')")
public interface ModeratorControllerInterface {
    @PostMapping("/moderator/objattrs")
    public ResponseEntity createObjAttr(@RequestBody Map<String, String> mappedObjAttr);

    @PutMapping("/moderator/objattrs")
    public ResponseEntity changeObjAttr(@RequestBody Map<String, String> mappedObjAttr);

    @DeleteMapping("moderator/objattrs/{id}")
    public Map<String, Boolean> deleteObjAttr(@PathVariable (value = "id")Integer id);
}
