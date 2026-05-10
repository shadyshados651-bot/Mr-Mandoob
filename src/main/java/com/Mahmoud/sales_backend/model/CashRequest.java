package com.Mahmoud.sales_backend.model;
import java.util.Date;

import lombok.Data;

@Data
public class CashRequest {
    private int id;
    private int mandoobId;
    private String mandoobName;
    private double amount;
    private String status; // pending / approved / rejected
    private String notes;
    private Integer approvedBy;
    private String approvedByName;
    private Date createdAt;
    private Date approvedAt;

}