package com.Mahmoud.sales_backend.module.Payment;

import com.Mahmoud.sales_backend.model.Payment;
import com.Mahmoud.sales_backend.model.Product;
import com.Mahmoud.sales_backend.module.Payment.PaymentService;
import com.Mahmoud.sales_backend.shared.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET /products
    @GetMapping
    public ApiResponse getAll() {
        return paymentService.getAll();
    }
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse getByInvoice(@PathVariable int invoiceId) {
        return paymentService.getByInvoice(invoiceId);
    }
    @PostMapping
    public ApiResponse create(@RequestBody Payment payment) {
        return paymentService.create(payment);
    }
}