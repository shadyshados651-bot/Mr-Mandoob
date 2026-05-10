package com.Mahmoud.sales_backend.module.Invoice;

import com.Mahmoud.sales_backend.shared.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.Mahmoud.sales_backend.model.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // GET /invoices
    @GetMapping
    public ApiResponse getAll() {
        return invoiceService.getAll();
    }

    // GET /invoices/{id}
    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable int id) {
        return invoiceService.getById(id);
    }

    // GET /invoices/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ApiResponse getByClient(@PathVariable int clientId) {
        return invoiceService.getByClient(clientId);
    }

    // GET /invoices/mandoob/{mandoobId}
    @GetMapping("/mandoob/{mandoobId}")
    public ApiResponse getByMandoob(@PathVariable int mandoobId) {
        return invoiceService.getByMandoob(mandoobId);
    }

    // GET /invoices/{id}/items
    @GetMapping("/{id}/items")
    public ApiResponse getWithItems(@PathVariable int id) {
        return invoiceService.getWithItems(id);
    }

    // POST /invoices
    @PostMapping
    public ApiResponse create(@RequestBody Invoice invoice) {
        return invoiceService.create(invoice);
    }

    // DELETE /invoices/{id}
    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable int id) {
        return invoiceService.delete(id);
    }
}