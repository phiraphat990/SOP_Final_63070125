package com.example.sop_final_63070125.controller;

import com.example.sop_final_63070125.pojo.Product;
import com.example.sop_final_63070125.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Service
public class ProductController {
    @Autowired


    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
  @RabbitListener(queues = "AddProductQueue")
    public boolean serviceAddProduct(Product product){
        try {
            productService.addProduct(product);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @RabbitListener(queues = "UpdateProductQueue")
    public boolean serviceUpdateProduct(Product product) {
        try {
            productService.updateProduct(product);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RabbitListener(queues = "DeleteProductQueue")
    public boolean serviceDeleteProduct(Product product){
        try{
            productService.deleteProduct(product);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @RabbitListener(queues = "GetNameProductQueue")
    public Product  serviceGetProductName(String name){
        try{
            return productService.getProductByName(name);
        }catch (Exception e){
            return null;
        }
    }

    @RabbitListener(queues = "GetAllProductQueue")
    public List<Product> serviceGetProductName(){
        try{
            return productService.getAllProduct();
        }catch (Exception e){
            return null;
        }
    }

}
