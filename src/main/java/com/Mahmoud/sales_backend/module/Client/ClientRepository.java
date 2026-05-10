package com.Mahmoud.sales_backend.module.Client;
import java.sql.Connection;
import org.springframework.stereotype.Repository;
import com.Mahmoud.sales_backend.model.Client;
import com.Mahmoud.sales_backend.Repository.BaseRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
@Repository
public class ClientRepository extends BaseRepository implements IClientRepository {
    public ClientRepository(Connection conn) {
        super(conn);
    }

    // ================= Get All =================
    public List<Client> getAll() {

        List<Map<String, Object>> result = super.getAll("clients");

        List<Client> clients = new ArrayList<>();

        for (Map<String, Object> row : result) {
            clients.add(mapToClient(row));
        }

        return clients;
    }
    // ================= Get All With Debt =================
    public List<Client> getAllWithDebt() {
        String sql = """
            SELECT 
                c.id,
                c.name,
                c.phone,
                c.address,
                c.mandoob_id,
                c.created_at,
                c.updated_at,
                COUNT(i.id)                        AS invoice_count,
                COALESCE(SUM(i.total), 0)          AS total_invoiced,
                COALESCE(SUM(i.paid), 0)           AS total_paid,
                COALESCE(SUM(i.total - i.paid), 0) AS debt
            FROM clients c
            LEFT JOIN invoices i ON i.client_id = c.id
            GROUP BY c.id
            ORDER BY debt DESC
        """;
        List<Map<String, Object>> result =query(sql);
        List<Client> clients = new ArrayList<>();

        for (Map<String, Object> row : result) {
            clients.add(mapToClient(row));
        }
        return clients;
    }
    public List<Client> getClientsByMandoobWithDebt(int mandoobId) {
    String sql = """
        SELECT 
            c.id,
            c.name,
            c.phone,
            c.address,
            c.mandoob_id,
            c.created_at,
            c.updated_at,
            COUNT(i.id)                        AS invoice_count,
            COALESCE(SUM(i.total), 0)          AS total_invoiced,
            COALESCE(SUM(i.paid), 0)           AS total_paid,
            COALESCE(SUM(i.total - i.paid), 0) AS debt
        FROM clients c
        LEFT JOIN invoices i ON i.client_id = c.id
        WHERE c.mandoob_id = ?
        GROUP BY c.id, c.name, c.phone, c.created_at, c.updated_at
        ORDER BY debt DESC
    """;

    List<Map<String, Object>> result = query(sql, mandoobId);

    List<Client> clients = new ArrayList<>();
    for (Map<String, Object> row : result) {
        clients.add(mapToClient(row));
    }

    return clients;
}

    // ================= Get All =================
    public List<Client> getClientsByMandoob(int mandoobid) {

        List<Map<String, Object>> result =
                query("SELECT * FROM clients WHERE mandoob_id = ?", mandoobid);

        List<Client> clients = new ArrayList<>();

        for (Map<String, Object> row : result) {
            clients.add(mapToClient(row));
        }

        return clients;
    }
    // ================= Get By ID =================
    public Client getById(int id) {

    String sql = """
        SELECT 
            c.id,
            c.name,
            c.phone,
            c.address,
            c.mandoob_id,
            c.created_at,
            c.updated_at,
            COUNT(i.id)                        AS invoice_count,
            COALESCE(SUM(i.total), 0)          AS total_invoiced,
            COALESCE(SUM(i.paid), 0)           AS total_paid,
            COALESCE(SUM(i.total - i.paid), 0) AS debt
        FROM clients c
        LEFT JOIN invoices i ON i.client_id = c.id
        WHERE c.id = ?
        GROUP BY c.id, c.name, c.phone, c.address, c.mandoob_id, c.created_at, c.updated_at
    """;

    List<Map<String, Object>> result = query(sql, id);

    if (result.isEmpty()) return null;

    return mapToClient(result.get(0));
}
    // ================= Create =================
    public boolean create(Client client) {
        try {
            Map<String, Object> data = Map.of(
            "name", client.getName(),
            "address", client.getAddress(),
            "phone", client.getPhone(),
            "mandoob_id", client.getMandoobId()
            );
            return insert("clients", data) >0;
        } catch (Exception e) {
            System.out.println("DB ERROR: " + e.getMessage());
            return false;
        }
    }
    // ================= Delete =================
    public boolean delete(int id) {
        try {
            return delete("clients", "id", id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= Mapper =================
    private Client mapToClient(Map<String, Object> row) {

        Client client = new Client();

        client.setId((int) row.get("id"));
        client.setName((String) row.get("name"));
        client.setAddress((String) row.get("address"));
        client.setPhone((String) row.get("phone"));
        client.setMandoobId((int) row.get("mandoob_id"));
        client.setDebt(((Number) row.get("debt")).doubleValue());
        client.setTotalinvoice(((Number) row.get("total_invoiced")).doubleValue());
        client.setTotalpaid(((Number) row.get("total_paid")).doubleValue());
        client.setCreatedAt((Date) row.get("created_at"));
        client.setUpdatedAt((Date) row.get("updated_at"));

        return client;
    }
}