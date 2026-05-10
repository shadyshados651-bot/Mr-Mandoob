package com.Mahmoud.sales_backend.module.Product;

import com.Mahmoud.sales_backend.model.Product;
import com.Mahmoud.sales_backend.shared.ApiResponse;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Mahmoud.sales_backend.security.UserContext;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ================= GetAll =================
    public ApiResponse getAll() {
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("Not allowed");
        }
        List<Product> products = productRepository.getAll();
        double totalValue = products.stream()
            .mapToDouble(p -> p.getSellprice() * p.getQuantity())
            .sum();
        int count = productRepository.countLowStock();

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("total_products", products.size());
        response.put("total_value", totalValue);
        response.put("lowstockcount", count);

        return ApiResponse.success(response);
    }

    // ================= GetById =================
    public ApiResponse getById(int id) {
        if (id <= 0)
            return ApiResponse.error("Invalid ID");

        Product product = productRepository.getById(id);

        if (product == null)
            return ApiResponse.error("Product not found");

        return ApiResponse.success(product);
    }

    // ================= GetCount =================
    public ApiResponse getCount() {
        int count = productRepository.getCount();
        return ApiResponse.success(count);
    }

    // ================= GetCountLowStockProducts =================
    public ApiResponse getcountLowStockProducts() {
        int count = productRepository.countLowStock();
        return ApiResponse.success(count);
    }

    // ================= Create =================
    public ApiResponse create(Product product) {
        if (product.getName() == null || product.getName().isBlank())
            return ApiResponse.error("Name is required");

        if (product.getSellprice() <= 0)
            return ApiResponse.error("Sell price must be greater than 0");
        if (product.getSellprice() < product.getPricecost())
            return ApiResponse.error("Sell price must be greater than cost price");

        if (product.getPricecost() <= 0)
            return ApiResponse.error("Price cost must be greater than 0");

        if (product.getQuantity() < 0)
            return ApiResponse.error("Quantity cannot be negative");

        if (product.getMinStock() < 0)
            return ApiResponse.error("Min stock cannot be negative");

        boolean isCreated = productRepository.create(product);

        if (isCreated)
            return ApiResponse.success("Product created successfully");

        return ApiResponse.error("Failed to create product");
    }

    // ================= Update =================
    public ApiResponse update(Product product) {
        if (product.getId() <= 0)
            return ApiResponse.error("Invalid ID");

        if (product.getName() == null || product.getName().isBlank())
            return ApiResponse.error("Name is required");

        if (product.getSellprice() <= 0)
            return ApiResponse.error("Sell price must be greater than 0");

        if (product.getPricecost() <= 0)
            return ApiResponse.error("Price cost must be greater than 0");

        Product existing = productRepository.getById(product.getId());
        if (existing == null)
            return ApiResponse.error("Product not found");

        boolean isUpdated = productRepository.update(product);

        if (isUpdated)
            return ApiResponse.success("Product updated successfully");

        return ApiResponse.error("Failed to update product");
    }

    // ================= Delete =================
    public ApiResponse delete(int id) {
        if (id <= 0)
            return ApiResponse.error("Invalid ID");

        boolean isDeleted = productRepository.delete(id);

        if (isDeleted)
            return ApiResponse.success("Product deleted successfully");

        return ApiResponse.error("Delete failed");
    }

    // ================= IncreaseQuantity =================
    public ApiResponse increaseQuantity(int id, int qty) {
        if (id <= 0)
            return ApiResponse.error("Invalid ID");

        if (qty <= 0)
            return ApiResponse.error("Quantity must be greater than 0");

        Product existing = productRepository.getById(id);
        if (existing == null)
            return ApiResponse.error("Product not found");

        boolean isUpdated = productRepository.increaseQuantity(id, qty);

        if (isUpdated)
            return ApiResponse.success("Quantity updated successfully");

        return ApiResponse.error("Failed to update quantity");
    }
}