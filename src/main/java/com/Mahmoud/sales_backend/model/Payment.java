package com.Mahmoud.sales_backend.model;
import java.util.Date;

import lombok.Data;

@Data
public class Payment {
    private int id;
    private int invoiceId;
    private int Clientid;
    private int Mandoobid;
    private double amount;
    private String notes;
    private String Clientname;
    private String Mandoobname;
    private Date createdAt;
}