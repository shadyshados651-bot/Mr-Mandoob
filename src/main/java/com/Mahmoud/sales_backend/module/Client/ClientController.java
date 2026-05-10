package com.Mahmoud.sales_backend.module.Client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Mahmoud.sales_backend.module.Client.ClientService;
import com.Mahmoud.sales_backend.shared.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import com.Mahmoud.sales_backend.model.Client;
// localhost:8080/client
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    @GetMapping
    public ApiResponse getAll() {
        return clientService.getAll();
    }
    @GetMapping("/mandoob/{mandoobId}")
    public ApiResponse getByMandoob(@PathVariable int mandoobId) {
        return clientService.getByMandoob(mandoobId);
    }
    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable int id) {
        return clientService.getById(id);
    }
    @PostMapping
    public ApiResponse create(@RequestBody Client client) {
        System.out.println("sssss");
        return clientService.create(client);
    }
    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable int id) {
        return clientService.delete(id);
    }
}
