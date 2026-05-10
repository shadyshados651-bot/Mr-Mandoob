package com.Mahmoud.sales_backend.model;
import java.util.Date;
import java.util.List;
import lombok.Data;
@Data
public class Invoice {
    private int id;
    private int mandoobId;
    private int clientId;
    private String mandoobName;
    private String clientName;
    private double total;
    private double paid;
    private String status;
    private List<InvoiceItem> items;
    private Date createdAt;
}
