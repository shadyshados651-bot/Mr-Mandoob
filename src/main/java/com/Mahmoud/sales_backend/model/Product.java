package com.Mahmoud.sales_backend.model;
import java.util.Date;
import lombok.Data;
@Data
public class Product {
    private int id;
    private String name;
    private double pricecost;
    private double sellprice;
    private int quantity;
    private int minStock;
    private Date createdAt;
    private Date updatedAt;

}
