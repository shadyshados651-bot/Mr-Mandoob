package com.Mahmoud.sales_backend.module.Payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.Mahmoud.sales_backend.model.Payment;
import com.Mahmoud.sales_backend.model.Product;
import com.Mahmoud.sales_backend.security.UserContext;
import com.Mahmoud.sales_backend.shared.ApiResponse;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ================= GetAll =================
    public ApiResponse getAll() {
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();
        List<Payment> payments;
        if ("admin".equals(role)) {
            payments = paymentRepository.getAll();
            // return ApiResponse.error("Not allowed");
        } else {
            payments = paymentRepository.getPaymentsByMandoob(userId);
        }
        double totalValue = payments.stream()
                .mapToDouble(p -> p.getAmount())
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("total_payments", payments.size());
        response.put("total_value", totalValue);

        return ApiResponse.success(response);
    }

    // ================= getByInvoice =================
    public ApiResponse getByInvoice(int id) {
        if (id <= 0)
            return ApiResponse.error("Invalid ID");

        List<Payment> payment = paymentRepository.getByInvoice(id);

        if (payment == null)
            return ApiResponse.error("payments not found");

        return ApiResponse.success(payment);
    }

    // ================= Get Today Total =================
    public ApiResponse getTotalToday() {
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();

        List<Payment> payments;

        if ("admin".equals(role)) {
            payments = paymentRepository.getTodayPayments();
        } else {
            payments = paymentRepository.getTodayPaymentsByMandoob(userId);
        }

        double totalValue = payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("total_payments", payments.size());
        response.put("total_value", totalValue);

        return ApiResponse.success(response);
    }

    // ================= Create =================
    public ApiResponse create(Payment payment) {
        if (payment.getInvoiceId() <= 0)
            return ApiResponse.error("Sell price must be greater than 0");

        if (payment.getAmount() <= 0)
            return ApiResponse.error("Sell price must be greater than 0");

        boolean isCreated = paymentRepository.create(payment);

        if (isCreated)
            return ApiResponse.success("Product created successfully");

        return ApiResponse.error("Failed to create product");
    }

}