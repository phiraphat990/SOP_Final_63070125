package com.example.sop_final_63070125.repository;

import com.example.sop_final_63070125.pojo.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value = "{ProductName: '?0'}")
    Product findByName(String productName);
}
