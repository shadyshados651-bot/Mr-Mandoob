package com.Mahmoud.sales_backend.module.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.Mahmoud.sales_backend.model.Client;
import com.Mahmoud.sales_backend.module.Client.IClientRepository;
import com.Mahmoud.sales_backend.module.Invoice.IInvoiceRepository;
import com.Mahmoud.sales_backend.security.UserContext;
import com.Mahmoud.sales_backend.shared.ApiResponse;
import com.Mahmoud.sales_backend.shared.PhoneValidator;

@Service
public class ClientService {

    private final IClientRepository clientRepository;
    private final IInvoiceRepository invoiceRepository;

    public ClientService(IClientRepository clientRepository, IInvoiceRepository invoiceRepository) {
        this.clientRepository = clientRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // ================= GetAll =================
    public ApiResponse getAll() {
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();
        List<Client> clients;
        double totalDebt;
        if ("admin".equals(role)) {
            clients = clientRepository.getAllWithDebt();
            totalDebt = invoiceRepository.getTotalDebt();
        }else{
            clients = clientRepository.getClientsByMandoobWithDebt(userId);
            totalDebt = invoiceRepository.getMandoobTotalDebt(userId);
        }

        System.out.println("USER ID: " + userId);
        System.out.println("ROLE: " + role);
        Map<String, Object> response = new HashMap<>();
        response.put("total_clients", clients.size());
        response.put("total_debt",    totalDebt);
        response.put("clients",       clients);

        return ApiResponse.success(response);
    }
    // ================= GetByid =================
    public ApiResponse getById(int id) {
        if (id <= 0 || id > 5000) {
            return ApiResponse.error("Invalid ID");
        }
        Client client = clientRepository.getById(id);
        if (client == null) {
            return ApiResponse.error("Client not found");
        }

        return ApiResponse.success(client);
    }
    // ================= Create Client =================
    public ApiResponse create(Client client) {
        Integer userId = UserContext.getUserId();
        String role = UserContext.getRole();
        if (client.getName() == null || client.getName().isEmpty()) {
        return ApiResponse.error("Name is required");
        }
        if (!PhoneValidator.isValidEgyptPhone(client.getPhone())) {
            return ApiResponse.error("Invalid Egyptian phone number");
        }
        if ("admin".equals(role)) {
            if (client.getMandoobId() <= 0) {
                return ApiResponse.error("Invalid mandoob id");
            }
        }else{
            client.setMandoobId(userId);
        }
        boolean isCreated = clientRepository.create(client);
        if (isCreated) {
            return ApiResponse.success("Client created successfully");
        }
        return ApiResponse.error("Failed to create client");
    }
    // ================= delete client =================

    public ApiResponse delete(int id) {
        if (id <= 0 || id > 5000) {
            return ApiResponse.error("Invalid ID");
        }
        boolean isDeleted = clientRepository.delete(id);
        if (isDeleted) {
            return ApiResponse.success("Deleted successfully");
        }
        return ApiResponse.error("Delete failed");
    }
    // ================= Get By Mandoob =================
    public ApiResponse getByMandoob(int mandoobId) {
        String role = UserContext.getRole();
        if ("admin".equals(role)) {
            List<Client> clients = clientRepository.getClientsByMandoob(mandoobId);
            return ApiResponse.success(clients);
        }
        return ApiResponse.error("not allowed");
    }
}