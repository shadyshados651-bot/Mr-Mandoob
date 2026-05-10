package com.Mahmoud.sales_backend.module.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.Payment;

@Repository
public class PaymentRepository extends BaseRepository {

    public PaymentRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
    public List<Payment> getAll() {
        String sql = """
                    SELECT
                        p.*,
                        u.name AS mandoob_name,
                        u.id AS mandoob_id,
                        c.name AS client_name,
                        c.id AS client_id
                    FROM payments p
                    LEFT JOIN invoices   i ON i.id = p.invoice_id
                    LEFT JOIN users   u ON u.id = i.mandoob_id
                    LEFT JOIN clients c ON c.id = i.client_id
                """;
        List<Map<String, Object>> result = query(sql);

        List<Payment> payments = new ArrayList<>();
        for (Map<String, Object> row : result) {
            payments.add(mapToPayment(row));
        }

        return payments;
    }

    public List<Payment> getPaymentsByMandoob(int mandoobid) {
        String sql = """
                    SELECT
                        p.*,
                        u.name AS mandoob_name,
                        u.id AS mandoob_id,
                        c.name AS client_name,
                        c.id AS client_id
                    FROM payments p
                    LEFT JOIN invoices i ON i.id = p.invoice_id
                    LEFT JOIN users u ON u.id = i.mandoob_id
                    LEFT JOIN clients c ON c.id = i.client_id
                    WHERE i.mandoob_id = ?
                """;
        List<Map<String, Object>> result = query(sql, mandoobid);

        List<Payment> payments = new ArrayList<>();
        for (Map<String, Object> row : result) {
            payments.add(mapToPayment(row));
        }

        return payments;
    }

     // ================= Get By ID =================
     public Payment getById(int Id) {
     String sql = """
                    SELECT
                        p.*,
                        u.name AS mandoob_name,
                        u.id AS mandoob_id,
                        c.name AS client_name,
                        c.id AS client_id
                    FROM payments p
                    LEFT JOIN invoices i ON i.id = p.invoice_id
                    LEFT JOIN users u ON u.id = i.mandoob_id
                    LEFT JOIN clients c ON c.id = i.client_id
                    WHERE p.id = ?
                """;
        List<Map<String, Object>> result = query(sql, Id);
    
     if (result.isEmpty()) return null;
    
     return mapToPayment(result.get(0));
     }
     public List<Payment> getByInvoice(int invoiceId) {
     String sql = """
                    SELECT
                        p.*,
                        u.name AS mandoob_name,
                        u.id AS mandoob_id,
                        c.name AS client_name,
                        c.id AS client_id
                    FROM payments p
                    LEFT JOIN invoices i ON i.id = p.invoice_id
                    LEFT JOIN users u ON u.id = i.mandoob_id
                    LEFT JOIN clients c ON c.id = i.client_id
                    WHERE p.invoice_id = ?
                """;
        List<Map<String, Object>> result = query(sql, invoiceId);
        List<Payment> payments = new ArrayList<>();
        for (Map<String, Object> row : result) {
            payments.add(mapToPayment(row));
        }
    
     return payments;
     }
      // ================= Get Count =================
      public int count() {
      return super.count("payments");
      }

      // ================= Create (Transaction) =================
public boolean create(Payment payment) {
    try {
        conn.setAutoCommit(false);

        // 1) Insert payment
        String insertSql = "INSERT INTO payments (invoice_id, amount, notes) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setInt(1, payment.getInvoiceId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getNotes());
            stmt.executeUpdate();
        }

        // 2) Update invoice paid + status
        String updateSql = """
            UPDATE invoices
            SET
                paid = paid + ?,
                status = CASE
                    WHEN paid + ? >= total THEN 'paid'
                    ELSE 'partial'
                END
            WHERE id = ? AND (paid + ?) <= total
        """;
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setDouble(1, payment.getAmount());
            stmt.setDouble(2, payment.getAmount());
            stmt.setInt(3, payment.getInvoiceId());
            stmt.setDouble(4, payment.getAmount());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                // amount بيعدي الـ total أو invoice مش موجود
                conn.rollback();
                return false;
            }
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace();
        return false;

    } finally {
        try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
    }
}
// ================= Get Today Payments =================
public List<Payment> getTodayPayments() {
    String sql = """
        SELECT
            p.*,
            u.name AS mandoob_name,
            u.id AS mandoob_id,
            c.name AS client_name,
            c.id AS client_id
        FROM payments p
        LEFT JOIN invoices i ON i.id = p.invoice_id
        LEFT JOIN users u ON u.id = i.mandoob_id
        LEFT JOIN clients c ON c.id = i.client_id
        WHERE DATE(p.created_at) = CURDATE()
    """;
    List<Map<String, Object>> result = query(sql);
    List<Payment> payments = new ArrayList<>();
    for (Map<String, Object> row : result) {
        payments.add(mapToPayment(row));
    }
    return payments;
}

public List<Payment> getTodayPaymentsByMandoob(int mandoobId) {
    String sql = """
        SELECT
            p.*,
            u.name AS mandoob_name,
            u.id AS mandoob_id,
            c.name AS client_name,
            c.id AS client_id
        FROM payments p
        LEFT JOIN invoices i ON i.id = p.invoice_id
        LEFT JOIN users u ON u.id = i.mandoob_id
        LEFT JOIN clients c ON c.id = i.client_id
        WHERE DATE(p.created_at) = CURDATE()
          AND i.mandoob_id = ?
    """;
    List<Map<String, Object>> result = query(sql, mandoobId);
    List<Payment> payments = new ArrayList<>();
    for (Map<String, Object> row : result) {
        payments.add(mapToPayment(row));
    }
    return payments;
}
    // ================= Mapper =================
    private Payment mapToPayment(Map<String, Object> row) {
        Payment product = new Payment();
        product.setId(((Number) row.get("id")).intValue());
        product.setClientid(((Number) row.get("client_id")).intValue());
        product.setMandoobid(((Number) row.get("mandoob_id")).intValue());
        product.setInvoiceId(((Number) row.get("invoice_id")).intValue());
        product.setAmount(((Number) row.get("amount")).doubleValue());
        product.setNotes((String) row.get("notes"));
        product.setClientname((String) row.get("client_name"));
        product.setMandoobname((String) row.get("mandoob_name"));
        product.setCreatedAt((Date) row.get("created_at"));
        return product;
    }
}