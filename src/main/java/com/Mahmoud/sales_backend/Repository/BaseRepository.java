package com.Mahmoud.sales_backend.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRepository {

    protected Connection conn;

    public BaseRepository(Connection conn) {
        this.conn = conn;
    }

    // ================= GET ALL =================
    public List<Map<String, Object>> getAll(String table) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = "SELECT * FROM " + table;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columns; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET BY COLUMN =================
    public List<Map<String, Object>> getByColumn(String table, String column, Object value) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = "SELECT * FROM " + table + " WHERE " + column + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columns; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET ONE ROW =================
    public Map<String, Object> getRowByColumn(String table, String column, Object value) {

        String sql = "SELECT * FROM " + table + " WHERE " + column + " = ? LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                return row;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= COUNT =================
    public int count(String table) {
        String sql = "SELECT COUNT(*) FROM " + table;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= INSERT =================
    public int insert(String table, Map<String, Object> data) {

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (String key : data.keySet()) {
            columns.append(key).append(",");
            values.append("?,");
        }

        columns.setLength(columns.length() - 1);
        values.setLength(values.length() - 1);

        String sql = "INSERT INTO " + table +
                " (" + columns + ") VALUES (" + values + ")";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            int i = 1;
            for (Object value : data.values()) {
                stmt.setObject(i++, value);
            }

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= UPDATE =================
    public int update(String table, Map<String, Object> data, String column, Object value) {

        StringBuilder set = new StringBuilder();

        for (String key : data.keySet()) {
            set.append(key).append("=?,");
        }

        set.setLength(set.length() - 1);

        String sql = "UPDATE " + table + " SET " + set +
                " WHERE " + column + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            int i = 1;
            for (Object val : data.values()) {
                stmt.setObject(i++, val);
            }

            stmt.setObject(i, value);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= DELETE =================
    public int delete(String table, String column, Object value) {

        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
        // ================= CUSTOM QUERY =================
    public List<Map<String, Object>> query(String sql, Object... params) {

        List<Map<String, Object>> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            // binding parameters لو موجودة
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columns; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}