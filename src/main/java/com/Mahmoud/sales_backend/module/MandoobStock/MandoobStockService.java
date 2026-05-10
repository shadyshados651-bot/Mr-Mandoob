package com.Mahmoud.sales_backend.module.MandoobStock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Mahmoud.sales_backend.model.MandoobStock;
import com.Mahmoud.sales_backend.module.Product.ProductRepository;
import com.Mahmoud.sales_backend.security.UserContext;
import com.Mahmoud.sales_backend.shared.ApiResponse;

@Service
public class MandoobStockService {

    private final MandoobStockRepository mandoobstockRepository;
    private final ProductRepository productRepository;

    public MandoobStockService(MandoobStockRepository mandoobstockRepository,ProductRepository productRepository) {
        this.mandoobstockRepository = mandoobstockRepository;
        this.productRepository =productRepository;
    }

    // ================= GetAll =================
    public ApiResponse getAll() {
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();

        List<MandoobStock> stock;

        if ("admin".equals(role)) {
            stock = mandoobstockRepository.getAll();
        } else {
            stock = mandoobstockRepository.getByMandoob(userId);
        }

        return ApiResponse.success(stock);
    }


    // ================= GetByMandoob =================
    public ApiResponse getByMandoob(int mandoobId) {
        if (mandoobId <= 0) {
            return ApiResponse.error("Invalid mandoob ID");
        }
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("not allowed");
        } 
        List<MandoobStock> stock = mandoobstockRepository.getByMandoob(mandoobId);

        return ApiResponse.success(stock);
    }

    // ================= Create =================
    public ApiResponse add(MandoobStock mandoobStock) {

        if (mandoobStock.getMandoobId() <= 0)
            return ApiResponse.error("Invalid mandoob ID");

        if (mandoobStock.getProductid() <= 0)
            return ApiResponse.error("Invalid product ID");

        if (mandoobStock.getQuantity() <= 0)
            return ApiResponse.error("Invalid  qty");
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("not allowed");
        } 


        boolean isCreated = mandoobstockRepository.add(mandoobStock);
    
        if (!isCreated)
            return ApiResponse.error("Failed to create invoice");
    
    
        return ApiResponse.success("Invoice created successfully");
    }
    // ================= Create =================
    public ApiResponse reduce(MandoobStock mandoobStock) {

        if (mandoobStock.getMandoobId() <= 0)
            return ApiResponse.error("Invalid mandoob ID");

        if (mandoobStock.getProductid() <= 0)
            return ApiResponse.error("Invalid product ID");

        if (mandoobStock.getQuantity() <= 0)
            return ApiResponse.error("Invalid  qty");
        String role = UserContext.getRole();
        if (!"admin".equals(role)) {
            return ApiResponse.error("not allowed");
        }


        boolean isCreated = mandoobstockRepository.reduce(mandoobStock);
    
        if (!isCreated)
            return ApiResponse.error("Failed to create invoice");
    
    
        return ApiResponse.success("Invoice created successfully");
    }

}