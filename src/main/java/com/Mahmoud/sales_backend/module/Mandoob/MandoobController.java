package com.Mahmoud.sales_backend.module.Mandoob;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mahmoud.sales_backend.model.User;
import com.Mahmoud.sales_backend.shared.ApiResponse;
@RestController
@RequestMapping("/mandoobs")
public class MandoobController {

    private final MandoobService mandoobService;

    public MandoobController(MandoobService mandoobService) {
        this.mandoobService = mandoobService;
    }
    @PostMapping("/create")
    public ApiResponse create(@RequestBody User user) {
        return mandoobService.create(user);
    }
    @GetMapping
    public ApiResponse getAll() {
        return mandoobService.getAll();
    }
    @GetMapping("/{id}")
    public ApiResponse get(@PathVariable int id) {
        return mandoobService.get(id);
    }
    // PUT /mandoobs/{id}/toggle-status
@PostMapping("/{id}/toggle-status")
public ApiResponse toggleStatus(@PathVariable int id) {
    return mandoobService.toggleStatus(id);
}
}
