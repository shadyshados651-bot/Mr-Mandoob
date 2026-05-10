package com.Mahmoud.sales_backend.model;
import java.util.Date;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private String password;
    private String token;
    private String role;
    private int status;
    private Date createdAt;
    private Date updatedAt;
}