package com.Mahmoud.sales_backend.module.Product;

import com.Mahmoud.sales_backend.model.Product;
import java.util.List;

public interface IProductRepository {
    List<Product> getAll();
    Product getById(int id);
    int getCount();
    int countLowStock();
    boolean create(Product product);
    boolean update(Product product);
    boolean delete(int id);
    boolean increaseQuantity(int id, int qty);
}
