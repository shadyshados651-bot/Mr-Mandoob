package com.Mahmoud.sales_backend.module.CashRequest;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.CashRequest;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class CashRequestRepository extends BaseRepository {

    public CashRequestRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
    public List<CashRequest> getAll() {
        String sql = """
            SELECT
                cr.*,
                u.name  AS mandoob_name,
                a.name  AS approved_by_name
            FROM cash_requests cr
            LEFT JOIN users u ON u.id = cr.mandoob_id
            LEFT JOIN users a ON a.id = cr.approved_by
        """;
        List<Map<String, Object>> result = query(sql);

        List<CashRequest> requests = new ArrayList<>();
        for (Map<String, Object> row : result) {
            requests.add(mapToCashRequest(row));
        }
        return requests;
    }

    // ================= Get By Mandoob =================
    public List<CashRequest> getByMandoob(int mandoobId) {
        String sql = """
            SELECT
                cr.*,
                u.name  AS mandoob_name,
                a.name  AS approved_by_name
            FROM cash_requests cr
            LEFT JOIN users u ON u.id = cr.mandoob_id
            LEFT JOIN users a ON a.id = cr.approved_by
            WHERE cr.mandoob_id = ?
        """;
        List<Map<String, Object>> result = query(sql, mandoobId);

        List<CashRequest> requests = new ArrayList<>();
        for (Map<String, Object> row : result) {
            requests.add(mapToCashRequest(row));
        }
        return requests;
    }

    // ================= Get Pending =================
    public List<CashRequest> getPending() {
        String sql = """
            SELECT
                cr.*,
                u.name  AS mandoob_name,
                a.name  AS approved_by_name
            FROM cash_requests cr
            LEFT JOIN users u ON u.id = cr.mandoob_id
            LEFT JOIN users a ON a.id = cr.approved_by
            WHERE cr.status = 'pending'
        """;
        List<Map<String, Object>> result = query(sql);

        List<CashRequest> requests = new ArrayList<>();
        for (Map<String, Object> row : result) {
            requests.add(mapToCashRequest(row));
        }
        return requests;
    }

    // ================= Get Status =================
    public String getStatus(int id) {
        String sql = "SELECT status FROM cash_requests WHERE id = ?";
        List<Map<String, Object>> result = query(sql, id);

        if (result.isEmpty()) return null;
        return (String) result.get(0).get("status");
    }

    // ================= Count =================
    public int count() {
        return super.count("cash_requests");
    }

    // ================= Create =================
    public boolean create(CashRequest cashRequest) {
        String sql = "INSERT INTO cash_requests (mandoob_id, amount, notes) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cashRequest.getMandoobId());
            stmt.setDouble(2, cashRequest.getAmount());
            stmt.setString(3, cashRequest.getNotes());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= Approve =================
    public boolean approve(int id, int adminId) {
        String sql = """
            UPDATE cash_requests
            SET status = 'approved', approved_by = ?
            WHERE id = ? AND status = 'pending'
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= Reject =================
    public boolean reject(int id, int adminId, String notes) {
        String sql = """
            UPDATE cash_requests
            SET status = 'rejected', approved_by = ?, notes = ?
            WHERE id = ? AND status = 'pending'
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            stmt.setString(2, notes);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= Mapper =================
    private CashRequest mapToCashRequest(Map<String, Object> row) {
        CashRequest cr = new CashRequest();

        cr.setId(((Number) row.get("id")).intValue());
        cr.setMandoobId(((Number) row.get("mandoob_id")).intValue());
        cr.setAmount(((Number) row.get("amount")).doubleValue());
        cr.setStatus((String) row.get("status"));
        cr.setNotes((String) row.get("notes"));
        cr.setMandoobName((String) row.get("mandoob_name"));
        cr.setApprovedByName((String) row.get("approved_by_name"));

        Object approvedBy = row.get("approved_by");
        if (approvedBy != null)
            cr.setApprovedBy(((Number) approvedBy).intValue());

        cr.setCreatedAt((Date) row.get("created_at"));

        return cr;
    }
}