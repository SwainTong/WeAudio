package com.lan.tong.weaudit.bean;

import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Product;



public class RecordBean {
    private Integer id;
    private Product recordProduct;//产品
    private Employee recordEmployee;//生产人
    private String recordDate;//生产日期
    private int recordAmount;//数量
    private boolean tag;

    public RecordBean(Integer id, Product recordProduct, Employee recordEmployee, String recordDate, int recordAmount) {
        this.id = id;
        this.recordProduct = recordProduct;
        this.recordEmployee = recordEmployee;
        this.recordDate = recordDate;
        this.recordAmount = recordAmount;
    }

    public RecordBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getRecordProduct() {
        return recordProduct;
    }

    public void setRecordProduct(Product recordProduct) {
        this.recordProduct = recordProduct;
    }

    public Employee getRecordEmployee() {
        return recordEmployee;
    }

    public void setRecordEmployee(Employee recordEmployee) {
        this.recordEmployee = recordEmployee;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public int getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(int recordAmount) {
        this.recordAmount = recordAmount;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}