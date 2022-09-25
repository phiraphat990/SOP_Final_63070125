package com.example.sop_final_63070125.service;

import com.example.sop_final_63070125.pojo.Product;
import com.example.sop_final_63070125.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository respository;
    public ProductService(ProductRepository respository){
        this.respository = respository;
    }
    public boolean addProduct(Product product){
        try{
            respository.insert(product);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public boolean updateProduct(Product product){
        try{
            respository.save(product);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean deleteProduct(Product product){
        try {
            respository.delete(product);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public List<Product> getAllProduct(){
        try {
            return respository.findAll();
        }catch (Exception e){
            return null;
        }

    }

    public Product getProductByName (String name){
        try {
            return respository.findByName(name);
        }catch (Exception e){
            return null;
        }
    }
}
