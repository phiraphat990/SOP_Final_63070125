package com.example.sop_final_63070125.view;

import com.example.sop_final_63070125.pojo.Product;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Route(value="ProductView")
public class ProductView extends VerticalLayout {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ComboBox<String> productList;
    private TextField productName;
    private NumberField productCost, productProfit, productPrice;
    private Button add, update, delete, clear;
    private List<Product> product;
    private Product productNow;
    private List<String> nameOfproduct;
    private String nameCombo;
    private String text = "";

    public ProductView(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.nameOfproduct = new ArrayList<>();
        productNow = new Product();
        productList = new ComboBox<>("Product List:");
        productList.setWidth("600px");
        productName = new TextField("Product Name:");
        productName.setWidth("600px");
        productCost = new NumberField("Product Cost:");
        productCost.setWidth("600px");
        productCost.setValue(0.0);
        productProfit = new NumberField("Product Profit:");
        productProfit.setWidth("600px");
        productProfit.setValue(0.0);
        productPrice = new NumberField("Product Price:");
        productPrice.setWidth("600px");
        productPrice.setValue(0.0);
        productPrice.setEnabled(false);

        HorizontalLayout h1 = new HorizontalLayout();
        add = new Button("Add Product");
        update = new Button("Update Product");
        delete = new Button("Delete Product");
        clear = new Button("Clear Product");
        h1.add(add, update, delete, clear);
        this.add(productList, productName, productCost, productProfit, productPrice, h1);
//        this.start();
        clear.addClickListener(event ->{
            this.productList.setValue("");
        });
        productCost.addKeyPressListener(Key.ENTER ,e-> {
            this.calClulate();
        });
        productProfit.addKeyPressListener(Key.ENTER ,e-> {
            this.calClulate();
        });

        productList.addValueChangeListener(event->{
            if(this.productList.getValue() != null){
                this.nameCombo = this.productList.getValue();}
            else {
                this.nameCombo = this.productName.getValue();
            }
            System.out.println("combobox "+this.productList.getValue());
            System.out.println("nameBox "+this.nameCombo);
            System.out.println("productName "+this.productName.getValue());
            try {
                // this.nameCombo = this.productList.getValue();
                if(!this.nameCombo.equals(this.text)){
                    this.productNow = (Product) rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", this.nameCombo);
                    productName.setValue(this.productNow.getProductName());
                    productCost.setValue(this.productNow.getProductCost());
                    productProfit.setValue(this.productNow.getProductProfit());
                    productPrice.setValue(this.productNow.getProductPrice());}
                else {
                    productName.setValue("");
                    productCost.setValue(0.0);
                    productProfit.setValue(0.0);
                    productPrice.setValue(0.0);
                }
            }catch (Exception e) {
                System.out.println("error");

                this.productNow = (Product) rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", this.nameCombo);
                productName.setValue(this.productNow.getProductName());
                productCost.setValue(this.productNow.getProductCost());
                productProfit.setValue(this.productNow.getProductProfit());
                productPrice.setValue(this.productNow.getProductPrice());

            }

        });

        productList.addFocusListener(event ->{
            this.callProductName();
        });

        add.addClickListener(event -> {
            this.calClulate();
            Product product = new Product();
            product.setProductName(productName.getValue());
            product.setProductCost(productCost.getValue());
            product.setProductProfit(productProfit.getValue());
            product.setProductPrice(productPrice.getValue());
//           this.nameCombo = this.productName.getValue();

            boolean out = (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "add", product);
            if(out){
                this.callProductName();
                // this.productList.setValue(name);
                this.nameCombo = this.productName.getValue();
                this.productList.setValue(this.nameCombo);
                new Notification("Add Success", 5000).open();
            }else {
                new Notification("Add Not Success", 5000).open();
            }
        });

        update.addClickListener(event -> {
            this.calClulate();

            this.productNow.setProductName(productName.getValue());
            this.productNow.setProductCost(productCost.getValue());
            this.productNow.setProductProfit(productProfit.getValue());
            this.productNow.setProductPrice(productPrice.getValue());

            boolean out = (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "update", this.productNow);
            if(out){
                //this.productList.setValue(this.productName.getValue());
                this.callProductName();
                this.nameCombo = this.productName.getValue();
                this.productList.setValue(this.nameCombo);
                new Notification("Update Success", 5000).open();
            }else {
                new Notification("Update Not Success", 5000).open();
            }
        });

        delete.addClickListener(event -> {
            boolean out = (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "delete", this.productNow);
            if(out){
                this.productList.setValue("");
                this.callProductName();
                new Notification("Delete Success", 5000).open();
            }else {
                new Notification("Delete Not Success", 5000).open();
            }
        });
    }
    public void callProductName(){
        this.nameOfproduct.clear();
        this.product = (List<Product>) rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "");
        for (Product product: this.product){
            this.nameOfproduct.add(product.getProductName());
        }
        productList.setItems(this.nameOfproduct);
    }

    public void calClulate(){
        double out = WebClient.create()
                .get()
                .uri("http://localhost:8080/getPrice/"+this.productCost.getValue()+"/"+this.productProfit.getValue())
                .retrieve()
                .bodyToMono(double.class)
                .block();
        this.productPrice.setValue(out);
    }
}