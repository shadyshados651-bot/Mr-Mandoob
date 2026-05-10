package com.Mahmoud.sales_backend.model;
import java.util.Date;

import lombok.Data;

@Data
public class MandoobStock {
    private int id;
    private int mandoobId;
    private String mandoobName;
    private int productid;
    private String productName;
    private int quantity;
    private Date createdAt;
    private Date updatedAt;
}
