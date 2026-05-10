package com.Mahmoud.sales_backend.module.MandoobStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.MandoobStock;

@Repository
public class MandoobStockRepository extends BaseRepository {

    public MandoobStockRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
    public List<MandoobStock> getAll() {
        String sql = """
            SELECT
                ms.*,
                u.name  AS mandoob_name,
                p.name  AS product_name
            FROM mandoob_stock ms
            LEFT JOIN users    u ON u.id = ms.mandoob_id
            LEFT JOIN products p ON p.id = ms.product_id
        """;
        List<Map<String, Object>> result = query(sql);

        List<MandoobStock> stocks = new ArrayList<>();
        for (Map<String, Object> row : result) {
            stocks.add(mapToMandoobStock(row));
        }
        return stocks;
    }

    // ================= Get By Mandoob =================
    public List<MandoobStock> getByMandoob(int mandoobId) {
        String sql = """
            SELECT
                ms.*,
                u.name  AS mandoob_name,
                p.name  AS product_name
            FROM mandoob_stock ms
            LEFT JOIN users    u ON u.id = ms.mandoob_id
            LEFT JOIN products p ON p.id = ms.product_id
            WHERE ms.mandoob_id = ?
        """;
        List<Map<String, Object>> result = query(sql, mandoobId);

        List<MandoobStock> stocks = new ArrayList<>();
        for (Map<String, Object> row : result) {
            stocks.add(mapToMandoobStock(row));
        }
        return stocks;
    }

    // ================= Get By ID =================
    public MandoobStock getById(int id) {
        String sql = """
            SELECT
                ms.*,
                u.name  AS mandoob_name,
                p.name  AS product_name
            FROM mandoob_stock ms
            LEFT JOIN users    u ON u.id = ms.mandoob_id
            LEFT JOIN products p ON p.id = ms.product_id
            WHERE ms.id = ?
        """;
        List<Map<String, Object>> result = query(sql, id);

        if (result.isEmpty()) return null;
        return mapToMandoobStock(result.get(0));
    }

    // ================= Count =================
    public int count() {
        return super.count("mandoob_stock");
    }

    // ================= Add Stock (Transaction) =================
    public boolean add(MandoobStock mandoobStock) {
        try {
            conn.setAutoCommit(false);

            // 1) Decrease from main products stock
            String decreaseSql = """
                UPDATE products
                SET quantity = quantity - ?
                WHERE id = ? AND quantity >= ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(decreaseSql)) {
                stmt.setInt(1, mandoobStock.getQuantity());
                stmt.setInt(2, mandoobStock.getProductid());
                stmt.setInt(3, mandoobStock.getQuantity());

                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2) Upsert into mandoob_stock
            String upsertSql = """
                INSERT INTO mandoob_stock (mandoob_id, product_id, quantity)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE quantity = quantity + ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(upsertSql)) {
                stmt.setInt(1, mandoobStock.getMandoobId());
                stmt.setInt(2, mandoobStock.getProductid());
                stmt.setInt(3, mandoobStock.getQuantity());
                stmt.setInt(4, mandoobStock.getQuantity());
                stmt.executeUpdate();
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

    // ================= Reduce Stock (Transaction) =================
    public boolean reduce(MandoobStock mandoobStock) {
        try {
            conn.setAutoCommit(false);

            // 1) Decrease from mandoob_stock
            String decreaseSql = """
                UPDATE mandoob_stock
                SET quantity = quantity - ?
                WHERE mandoob_id = ? AND product_id = ? AND quantity >= ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(decreaseSql)) {
                stmt.setInt(1, mandoobStock.getQuantity());
                stmt.setInt(2, mandoobStock.getMandoobId());
                stmt.setInt(3, mandoobStock.getProductid());
                stmt.setInt(4, mandoobStock.getQuantity());

                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2) Return back to main products stock
            String increaseSql = """
                UPDATE products
                SET quantity = quantity + ?
                WHERE id = ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(increaseSql)) {
                stmt.setInt(1, mandoobStock.getQuantity());
                stmt.setInt(2, mandoobStock.getProductid());
                stmt.executeUpdate();
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

    // ================= Mapper =================
    private MandoobStock mapToMandoobStock(Map<String, Object> row) {
        MandoobStock stock = new MandoobStock();

        stock.setId(((Number) row.get("id")).intValue());
        stock.setMandoobId(((Number) row.get("mandoob_id")).intValue());
        stock.setProductid(((Number) row.get("product_id")).intValue());
        stock.setQuantity(((Number) row.get("quantity")).intValue());
        stock.setMandoobName((String) row.get("mandoob_name"));
        stock.setProductName((String) row.get("product_name"));
        stock.setCreatedAt((Date) row.get("created_at"));
        stock.setUpdatedAt((Date) row.get("updated_at"));

        return stock;
    }
}