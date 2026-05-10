package com.Mahmoud.sales_backend.module.Product;

import com.Mahmoud.sales_backend.model.Product;
import com.Mahmoud.sales_backend.shared.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /products
    @GetMapping
    public ApiResponse getAll() {
        return productService.getAll();
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable int id) {
        return productService.getById(id);
    }

    // GET /products/count
    @GetMapping("/count")
    public ApiResponse getCount() {
        return productService.getCount();
    }

    // GET /products/low-stock
    @GetMapping("/low-stock")
    public ApiResponse getcountLowStock() {
        return productService.getcountLowStockProducts();
    }

    // POST /products
    @PostMapping
    public ApiResponse create(@RequestBody Product product) {
        return productService.create(product);
    }

    // PUT /products
    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable int id, @RequestBody Product product) {
        if (id <= 0) {
            return ApiResponse.error("Invalid product id");
        }
        product.setId(id);
        return productService.update(product);
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable int id) {
        return productService.delete(id);
    }

    // PATCH /products/{id}/quantity?qty=10
    @PatchMapping("/{id}/quantity")
    public ApiResponse increaseQuantity(@PathVariable int id, @RequestParam int qty) {
        return productService.increaseQuantity(id, qty);
    }
}