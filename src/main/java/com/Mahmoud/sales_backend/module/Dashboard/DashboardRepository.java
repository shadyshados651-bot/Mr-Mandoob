package com.Mahmoud.sales_backend.module.Dashboard;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardRepository extends BaseRepository {

    public DashboardRepository(Connection conn) {
        super(conn);
    }

    // ================= Admin Stats =================
    public double getTotalSalesToday() {
        List<Map<String, Object>> result = query(
            "SELECT COALESCE(SUM(total), 0) AS val FROM invoices WHERE DATE(created_at) = CURDATE()");
        return toDouble(result, "val");
    }

    public int getInvoicesToday() {
        List<Map<String, Object>> result = query(
            "SELECT COUNT(*) AS val FROM invoices WHERE DATE(created_at) = CURDATE()");
        return toInt(result, "val");
    }

    public double getCollectionsToday() {
        List<Map<String, Object>> result = query(
            "SELECT COALESCE(SUM(amount), 0) AS val FROM payments WHERE DATE(created_at) = CURDATE()");
        return toDouble(result, "val");
    }

    public int getTotalClients() {
        List<Map<String, Object>> result = query(
            "SELECT COUNT(*) AS val FROM clients");
        return toInt(result, "val");
    }

    public int getTotalProducts() {
        List<Map<String, Object>> result = query(
            "SELECT COUNT(*) AS val FROM products");
        return toInt(result, "val");
    }

    public int getLowStockProducts() {
        List<Map<String, Object>> result = query(
            "SELECT COUNT(*) AS val FROM products WHERE quantity <= min_stock");
        return toInt(result, "val");
    }

    public double getTotalDebts() {
        List<Map<String, Object>> result = query("""
            SELECT COALESCE(SUM(total - paid), 0) AS val
            FROM invoices
            WHERE status != 'paid'
        """);
        return toDouble(result, "val");
    }

    public int getPendingCashRequests() {
        List<Map<String, Object>> result = query(
            "SELECT COUNT(*) AS val FROM cash_requests WHERE status = 'pending'");
        return toInt(result, "val");
    }

    public double getTotalProfitToday() {
        List<Map<String, Object>> result = query("""
            SELECT COALESCE(SUM(ii.profit), 0) AS val
            FROM invoice_items ii
            JOIN invoices i ON i.id = ii.invoice_id
            WHERE DATE(i.created_at) = CURDATE()
        """);
        return toDouble(result, "val");
    }

    public double getTotalProfitAllTime() {
        List<Map<String, Object>> result = query(
            "SELECT COALESCE(SUM(profit), 0) AS val FROM invoice_items");
        return toDouble(result, "val");
    }

    // ================= Mandoob Stats =================
    public double getMandoobSalesToday(int mandoobId) {
        List<Map<String, Object>> result = query("""
            SELECT COALESCE(SUM(total), 0) AS val
            FROM invoices
            WHERE mandoob_id = ? AND DATE(created_at) = CURDATE()
        """, mandoobId);
        return toDouble(result, "val");
    }

    public double getMandoobDebts(int mandoobId) {
        List<Map<String, Object>> result = query("""
            SELECT COALESCE(SUM(total - paid), 0) AS val
            FROM invoices
            WHERE mandoob_id = ? AND status != 'paid'
        """, mandoobId);
        return toDouble(result, "val");
    }

    public int getMandoobInvoicesToday(int mandoobId) {
        List<Map<String, Object>> result = query("""
            SELECT COUNT(*) AS val
            FROM invoices
            WHERE mandoob_id = ? AND DATE(created_at) = CURDATE()
        """, mandoobId);
        return toInt(result, "val");
    }

    public double getMandoobCollectionsToday(int mandoobId) {
        List<Map<String, Object>> result = query("""
            SELECT COALESCE(SUM(p.amount), 0) AS val
            FROM payments p
            JOIN invoices i ON i.id = p.invoice_id
            WHERE i.mandoob_id = ? AND DATE(p.created_at) = CURDATE()
        """, mandoobId);
        return toDouble(result, "val");
    }

    // ================= Helpers =================
    private double toDouble(List<Map<String, Object>> result, String col) {
        if (result.isEmpty() || result.get(0).get(col) == null) return 0;
        return ((Number) result.get(0).get(col)).doubleValue();
    }

    private int toInt(List<Map<String, Object>> result, String col) {
        if (result.isEmpty() || result.get(0).get(col) == null) return 0;
        return ((Number) result.get(0).get(col)).intValue();
    }
}