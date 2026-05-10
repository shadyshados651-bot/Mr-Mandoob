package com.Mahmoud.sales_backend.module.Product;

import com.Mahmoud.sales_backend.Repository.BaseRepository;
import com.Mahmoud.sales_backend.model.Product;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository extends BaseRepository implements IProductRepository {

    public ProductRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
    public List<Product> getAll() {
        List<Map<String, Object>> result = super.getAll("products");

        List<Product> products = new ArrayList<>();
        for (Map<String, Object> row : result) {
            products.add(mapToProduct(row));
        }

        return products;
    }

    // ================= Get By ID =================
    public Product getById(int id) {
        List<Map<String, Object>> result =
                query("SELECT * FROM products WHERE id = ?", id);

        if (result.isEmpty()) return null;

        return mapToProduct(result.get(0));
    }

    // ================= Get Count =================
    public int getCount() {
        return super.count("products");
    }

    // ================= Count Low Stock =================
    public int countLowStock() {
        List<Map<String, Object>> result =
                query("SELECT COUNT(*) AS cnt FROM products WHERE quantity <= min_stock");

        if (result.isEmpty()) return 0;

        Object cnt = result.get(0).get("cnt");
        return cnt != null ? ((Number) cnt).intValue() : 0;
    }

    // ================= Create =================
    public boolean create(Product product) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("name",       product.getName());
            data.put("price_cost", product.getPricecost());
            data.put("sell_price", product.getSellprice());
            data.put("quantity",   product.getQuantity());
            data.put("min_stock",  product.getMinStock());

            return insert("products", data) > 0;
        } catch (Exception e) {
            System.out.println("DB ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= Update =================
    public boolean update(Product product) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("name",       product.getName());
            data.put("price_cost", product.getPricecost());
            data.put("sell_price", product.getSellprice());
            data.put("quantity",   product.getQuantity());
            data.put("min_stock",  product.getMinStock());

            return update("products", data, "id", product.getId()) > 0;
        } catch (Exception e) {
            System.out.println("DB ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= Delete =================
    public boolean delete(int id) {
        try {
            return delete("products", "id", id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= Increase Quantity =================
    public boolean increaseQuantity(int id, int qty) {
        String sql = "UPDATE products SET quantity = quantity + ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, qty);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= Mapper =================
    private Product mapToProduct(Map<String, Object> row) {
        Product product = new Product();

        product.setId(((Number) row.get("id")).intValue());
        product.setName((String) row.get("name"));
        product.setPricecost(((Number) row.get("price_cost")).doubleValue());
        product.setSellprice(((Number) row.get("sell_price")).doubleValue());
        product.setQuantity(((Number) row.get("quantity")).intValue());
        product.setMinStock(((Number) row.get("min_stock")).intValue());
        product.setCreatedAt((Date) row.get("created_at"));
        product.setUpdatedAt((Date) row.get("updated_at"));

        return product;
    }
}