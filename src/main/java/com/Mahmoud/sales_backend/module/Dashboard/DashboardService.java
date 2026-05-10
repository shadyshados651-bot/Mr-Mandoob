package com.Mahmoud.sales_backend.module.Dashboard;

import org.springframework.stereotype.Service;

import com.Mahmoud.sales_backend.model.Dashboard;
import com.Mahmoud.sales_backend.security.UserContext;
import com.Mahmoud.sales_backend.shared.ApiResponse;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    // ================= Get Stats =================
    public ApiResponse getStats() {
        int userId  = UserContext.getUserId();
        String role = UserContext.getRole();

        Dashboard dto= new Dashboard();

        if ("admin".equals(role)) {
            dto.setTotalSalesToday(dashboardRepository.getTotalSalesToday());
            dto.setInvoicesToday(dashboardRepository.getInvoicesToday());
            dto.setCollectionsToday(dashboardRepository.getCollectionsToday());
            dto.setTotalClients(dashboardRepository.getTotalClients());
            dto.setTotalProducts(dashboardRepository.getTotalProducts());
            dto.setLowStockProducts(dashboardRepository.getLowStockProducts());
            dto.setTotalDebts(dashboardRepository.getTotalDebts());
            dto.setPendingCashRequests(dashboardRepository.getPendingCashRequests());
            dto.setTotalProfitToday(dashboardRepository.getTotalProfitToday());
            dto.setTotalProfitAllTime(dashboardRepository.getTotalProfitAllTime());

        } else {
            dto.setTotalSalesToday(dashboardRepository.getMandoobSalesToday(userId));
            dto.setInvoicesToday(dashboardRepository.getMandoobInvoicesToday(userId));
            dto.setCollectionsToday(dashboardRepository.getMandoobCollectionsToday(userId));
            dto.setTotalDebts(dashboardRepository.getMandoobDebts(userId));
            dto.setLowStockProducts(dashboardRepository.getLowStockProducts());
        }

        return ApiResponse.success(dto);
    }
}