package com.Mahmoud.sales_backend.module.MandoobStock;

import com.Mahmoud.sales_backend.shared.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import com.Mahmoud.sales_backend.model.Invoice;
import com.Mahmoud.sales_backend.model.MandoobStock;

@RestController
@RequestMapping("/mandoob-stock")
public class MandoobStockController {

    private final MandoobStockService mandoobStockService;

    public MandoobStockController(MandoobStockService mandoobStockService) {
        this.mandoobStockService = mandoobStockService;
    }

    // GET /mandoob-stock
    @GetMapping
    public ApiResponse getAll() {
        return mandoobStockService.getAll();
    }

    // GET /mandoob-stock/mandoob/{mandoobId}
    @GetMapping("/mandoob/{mandoobId}")
    public ApiResponse getByMandoob(@PathVariable int mandoobId) {
        return mandoobStockService.getByMandoob(mandoobId);
    }


    // POST /mandoob-stock/add
    @PostMapping("/add")
    public ApiResponse create(@RequestBody MandoobStock mandoobstock) {
        return mandoobStockService.add(mandoobstock);
    }
    // POST /mandoob-stock/reduce
    @PostMapping("/reduce")
    public ApiResponse reduce(@RequestBody MandoobStock mandoobstock) {
        return mandoobStockService.reduce(mandoobstock);
    }
}