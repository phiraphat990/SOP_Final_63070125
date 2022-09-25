package com.example.sop_final_63070125.controller;

import com.example.sop_final_63070125.service.CalculatorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorPriceController {
    @Autowired
    private CalculatorPriceService priceservice;
    public CalculatorPriceController(CalculatorPriceService service) {
        this.priceservice = priceservice;
    }

    @RequestMapping(value = "/getPrice/{cost}/{profit}", method = RequestMethod.GET)
    public double serviceGetProducts(@PathVariable  Double cost, @PathVariable Double profit){
        return priceservice.getPrice(cost,profit);
    }
}
