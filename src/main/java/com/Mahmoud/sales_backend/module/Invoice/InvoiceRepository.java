package com.Mahmoud.sales_backend.module.Invoice;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.Invoice;
import com.Mahmoud.sales_backend.model.InvoiceItem;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
@Repository
public class InvoiceRepository extends BaseRepository implements IInvoiceRepository {

    public InvoiceRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
public List<Invoice> getAll() {
    String sql = """
        SELECT 
            i.*,
            u.name AS mandoob_name,
            c.name AS client_name
        FROM invoices i
        LEFT JOIN users   u ON u.id = i.mandoob_id
        LEFT JOIN clients c ON c.id = i.client_id
    """;
    List<Map<String, Object>> result = query(sql);

    List<Invoice> invoices = new ArrayList<>();
    for (Map<String, Object> row : result) {
        invoices.add(mapToInvoice(row));
    }
    return invoices;
}

// ================= Get By ID =================
public Invoice getById(int id) {
    String sql = """
        SELECT 
            i.*,
            u.name AS mandoob_name,
            c.name AS client_name
        FROM invoices i
        LEFT JOIN users   u ON u.id = i.mandoob_id
        LEFT JOIN clients c ON c.id = i.client_id
        WHERE i.id = ?
    """;
    List<Map<String, Object>> result = query(sql, id);

    if (result.isEmpty()) return null;
    return mapToInvoice(result.get(0));
}

// ================= Get By Client =================
public List<Invoice> getInvoicesByClient(int clientId) {
    String sql = """
        SELECT 
            i.*,
            u.name AS mandoob_name,
            c.name AS client_name
        FROM invoices i
        LEFT JOIN users   u ON u.id = i.mandoob_id
        LEFT JOIN clients c ON c.id = i.client_id
        WHERE i.client_id = ?
    """;
    List<Map<String, Object>> result = query(sql, clientId);

    List<Invoice> invoices = new ArrayList<>();
    for (Map<String, Object> row : result) {
        invoices.add(mapToInvoice(row));
    }
    return invoices;
}

// ================= Get By Mandoob =================
public List<Invoice> getInvoicesByMandoob(int mandoobId) {
    String sql = """
        SELECT 
            i.*,
            u.name AS mandoob_name,
            c.name AS client_name
        FROM invoices i
        LEFT JOIN users   u ON u.id = i.mandoob_id
        LEFT JOIN clients c ON c.id = i.client_id
        WHERE i.mandoob_id = ?
    """;
    List<Map<String, Object>> result = query(sql, mandoobId);

    List<Invoice> invoices = new ArrayList<>();
    for (Map<String, Object> row : result) {
        invoices.add(mapToInvoice(row));
    }
    return invoices;
}
    // ================= Get Today Invoices Count =================
    public int getTodayInvoicesCount() {
        List<Map<String, Object>> result =
                query("SELECT COUNT(*) AS cnt FROM invoices WHERE DATE(created_at) = CURDATE()");

        if (result.isEmpty()) return 0;

        Object cnt = result.get(0).get("cnt");
        return cnt != null ? ((Number) cnt).intValue() : 0;
    }

    // ================= Get Invoice Items =================
    public List<InvoiceItem> getInvoiceItems(int invoiceId) {
        List<Map<String, Object>> result =
            getByColumn("invoice_items", "invoice_id", invoiceId);
        List<InvoiceItem> items = new ArrayList<>();
        for (Map<String, Object> row : result) {
            items.add(mapToInvoiceItem(row));
        }

        return items;
    }

    // ================= Count =================
    public int count() {
        return super.count("invoices");
    }

    // ================= Create Invoice With Items (Transaction) =================
    public boolean create(Invoice invoice) {
    try {
        conn.setAutoCommit(false);

        // 1) Insert invoice & get generated ID

        String invoiceSql = "INSERT INTO invoices (mandoob_id, client_id, total, paid, status) " +
                            "VALUES (?, ?, ?, ?, ?)";

        int invoiceId;
        try (PreparedStatement stmt = conn.prepareStatement(invoiceSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setObject(1, invoice.getMandoobId());
            stmt.setObject(2, invoice.getClientId());
            stmt.setObject(3, invoice.getTotal());
            stmt.setObject(4, invoice.getPaid());
            stmt.setObject(5, invoice.getStatus());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();
                return false;
            }
            invoiceId = generatedKeys.getInt(1);
        }

        // 2) Insert each item
        String itemSql = "INSERT INTO invoice_items " +
                         "(invoice_id, product_id, quantity, sell_price, price_cost, total, profit) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(itemSql)) {
            for (InvoiceItem item : invoice.getItems()) {
                stmt.setInt(1, invoiceId);
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getSellprice());
                stmt.setDouble(5, item.getPricecost());
                stmt.setDouble(6, item.getTotal());
                stmt.setDouble(7, item.getProfit());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }

        // ================= DECREASE STOCK =================

        String stockSql =
            "UPDATE products SET quantity = quantity - ? " +
            "WHERE id = ? AND quantity >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(stockSql)) {

            for (InvoiceItem item : invoice.getItems()) {

                stmt.setInt(1, item.getQuantity());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());

                int affected = stmt.executeUpdate();

                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
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

    // ================= Delete =================
    public boolean delete(int id) {
        try {
            return delete("invoices", "id", id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // ================= Get Total Debt (Admin) =================
public double getTotalDebt() {
    String sql = """
        SELECT COALESCE(SUM(total - paid), 0) AS total_debt
        FROM invoices
        WHERE status != 'paid'
    """;
    List<Map<String, Object>> result = query(sql);
    if (result.isEmpty()) return 0;
    return ((Number) result.get(0).get("total_debt")).doubleValue();
}

// ================= Get Mandoob Total Debt =================
public double getMandoobTotalDebt(int mandoobId) {
    String sql = """
        SELECT COALESCE(SUM(total - paid), 0) AS total_debt
        FROM invoices
        WHERE mandoob_id = ? AND status != 'paid'
    """;
    List<Map<String, Object>> result = query(sql, mandoobId);
    if (result.isEmpty()) return 0;
    return ((Number) result.get(0).get("total_debt")).doubleValue();
}

    // ================= Invoice Mapper =================
    private Invoice mapToInvoice(Map<String, Object> row) {
        Invoice invoice = new Invoice();

        invoice.setId(((Number) row.get("id")).intValue());
        invoice.setMandoobId(((Number) row.get("mandoob_id")).intValue());
        invoice.setClientId(((Number) row.get("client_id")).intValue());
        invoice.setTotal(((Number) row.get("total")).doubleValue());
        invoice.setPaid(((Number) row.get("paid")).doubleValue());
        invoice.setStatus((String) row.get("status"));
        invoice.setCreatedAt((Date) row.get("created_at"));
        invoice.setMandoobName((String) row.get("mandoob_name"));
        invoice.setClientName((String) row.get("client_name"));

        return invoice;
    }

    // ================= InvoiceItem Mapper =================
    private InvoiceItem mapToInvoiceItem(Map<String, Object> row) {
        InvoiceItem item = new InvoiceItem();

        item.setId(((Number) row.get("id")).intValue());
        item.setInvoiceId(((Number) row.get("invoice_id")).intValue());
        item.setProductId(((Number) row.get("product_id")).intValue());
        item.setQuantity(((Number) row.get("quantity")).intValue());
        item.setPricecost(((Number) row.get("price_cost")).doubleValue());
        item.setSellprice(((Number) row.get("sell_price")).doubleValue());
        item.setTotal(((Number) row.get("total")).doubleValue());
        item.setProfit(((Number) row.get("profit")).doubleValue());

        return item;
    }
}

/*
{
  "clientId": 1,
  "mandoobId": 2,
  "paid": 100,
  "items": [
    {
      "productId": 5,
      "quantity": 2
    },
    {
      "productId": 7,
      "quantity": 1
    }
  ]
}
*/