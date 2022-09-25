package com.example.sop_final_63070125.service;

import com.example.sop_final_63070125.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculatorPriceService {
    @Autowired
    private ProductRepository resitory;
    public CalculatorPriceService(ProductRepository resitory){
        this.resitory = resitory;
    }
    public Double getPrice(Double cost, Double profit){
        Double productpricre = cost+profit;
        return  productpricre;
    }
}
