package com.Mahmoud.sales_backend.module.CashRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import com.Mahmoud.sales_backend.model.CashRequest;
import com.Mahmoud.sales_backend.shared.ApiResponse;

@RestController
@RequestMapping("/cash-requests")
public class CashRequestController {

    private final CashRequestService cashRequestService;

    public CashRequestController(CashRequestService cashRequestService) {
        this.cashRequestService = cashRequestService;
    }

    // GET /cash-requests
    @GetMapping
    public ApiResponse getAll() {
        return cashRequestService.getAll();
    }

    // GET /cash-requests/pending
    @GetMapping("/pending")
    public ApiResponse getPending() {
        return cashRequestService.getPending();
    }

    // GET /cash-requests/mandoob/{mandoobId}
    @GetMapping("/mandoob/{mandoobId}")
    public ApiResponse getByMandoob(@PathVariable int mandoobId) {
        return cashRequestService.getByMandoob(mandoobId);
    }

    // POST /cash-requests/create
    @PostMapping("/create")
    public ApiResponse create(@RequestBody CashRequest cashRequest) {
        return cashRequestService.create(cashRequest);
    }

    // PUT /cash-requests/{id}/approve
    @PutMapping("/{id}/approve")
    public ApiResponse approve(@PathVariable int id) {
        return cashRequestService.approve(id);
    }

    // PUT /cash-requests/{id}/reject
    @PutMapping("/{id}/reject")
    public ApiResponse reject(@PathVariable int id, @RequestBody CashRequest cashRequest) {
        return cashRequestService.reject(id, cashRequest);
    }
}