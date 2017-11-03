package com.lan.tong.weaudit.bean;

/**
 * Created by Administrator on 2017/8/11.
 */

public class ProductBean {
    private Integer id;
    private String productName;//产品名称
    private String productType;//产品类型
    private int productAmount;//产品数量
    private double productWage;//工价
    private double productMargin;
    private boolean tag;

    public ProductBean(Integer id, String productName, String productType, double productWage, double productMargin) {
        this.id = id;
        this.productName = productName;
        this.productType = productType;
        this.productWage = productWage;
        this.productMargin = productMargin;
        this.tag = false;
        this.productAmount = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public double getProductWage() {
        return productWage;
    }

    public void setProductWage(double productWage) {
        this.productWage = productWage;
    }

    public double getProductMargin() {
        return productMargin;
    }

    public void setProductMargin(double productMargin) {
        this.productMargin = productMargin;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}