package com.Mahmoud.sales_backend.model;
import java.util.Date;

import lombok.Data;

@Data
public class Client {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private double debt;
    private double totalpaid;
    private double totalinvoice;
    private Integer mandoobId;
    private Date createdAt; 
    private Date updatedAt; 
}
