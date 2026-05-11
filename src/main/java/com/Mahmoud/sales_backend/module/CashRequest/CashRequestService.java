package com.Mahmoud.sales_backend.module.CashRequest;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Mahmoud.sales_backend.model.CashRequest;
import com.Mahmoud.sales_backend.module.Product.ProductRepository;
import com.Mahmoud.sales_backend.security.UserContext;
import com.Mahmoud.sales_backend.shared.ApiResponse;

@Service
public class CashRequestService {

    private final ICashRequestRepository cashRequestRepository;  // ← interface مش concrete

    public CashRequestService(ICashRequestRepository cashRequestRepository) {
        this.cashRequestRepository = cashRequestRepository;
    }

    // ================= GetAll =================
    public ApiResponse getAll() {
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();

        List<CashRequest> requests;

        if ("admin".equals(role)) {
            requests = cashRequestRepository.getAll();
        } else {
            requests = cashRequestRepository.getByMandoob(userId);
        }

        return ApiResponse.success(requests);
    }

    // ================= getPending =================
    public ApiResponse getPending() {
        String role = UserContext.getRole();

        if (!"admin".equals(role))
            return ApiResponse.error("Not allowed");

        return ApiResponse.success(cashRequestRepository.getPending());
    }

    // ================= getByMandoob =================
    public ApiResponse getByMandoob(int mandoobId) {
        if (mandoobId <= 0)
            return ApiResponse.error("Invalid mandoob ID");

        if (!"admin".equals(UserContext.getRole()))
            return ApiResponse.error("Not allowed");

        return ApiResponse.success(cashRequestRepository.getByMandoob(mandoobId));
    }

    // ================= create =================
    public ApiResponse create(CashRequest cashRequest) {
        if (cashRequest.getAmount() <= 0)
            return ApiResponse.error("Amount must be greater than 0");

        cashRequest.setMandoobId(UserContext.getUserId());

        boolean created = cashRequestRepository.create(cashRequest);
        if (!created)
            return ApiResponse.error("Failed to create request");

        return ApiResponse.success("Request created successfully");
    }

    // ================= approve =================
    public ApiResponse approve(int id) {
        if (id <= 0)
            return ApiResponse.error("Invalid ID");

        if (!"admin".equals(UserContext.getRole()))
            return ApiResponse.error("Not allowed");

        String currentStatus = cashRequestRepository.getStatus(id);

        if (currentStatus == null)
            return ApiResponse.error("Request not found");

        if (!"pending".equals(currentStatus))
            return ApiResponse.error("Cannot approve — request is already: " + currentStatus);

        boolean approved = cashRequestRepository.approve(id, UserContext.getUserId());
        if (!approved)
            return ApiResponse.error("Failed to approve request");

        return ApiResponse.success("Request approved successfully");
    }

    // ================= reject =================
    public ApiResponse reject(int id, CashRequest cashRequest) {
        if (id <= 0)
            return ApiResponse.error("Invalid ID");

        if (!"admin".equals(UserContext.getRole()))
            return ApiResponse.error("Not allowed");

        String currentStatus = cashRequestRepository.getStatus(id);

        if (currentStatus == null)
            return ApiResponse.error("Request not found");

        if (!"pending".equals(currentStatus))  // ✅ الشرط الصح
            return ApiResponse.error("Cannot reject — request is already: " + currentStatus);

        boolean rejected = cashRequestRepository.reject(id, UserContext.getUserId(), cashRequest.getNotes());
        if (!rejected)
            return ApiResponse.error("Failed to reject request");

        return ApiResponse.success("Request rejected successfully");
    }
}


