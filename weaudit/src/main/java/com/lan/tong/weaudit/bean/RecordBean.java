package com.lan.tong.weaudit.bean;

/**
 * Created by Administrator on 2017/8/11.
 */

public class RecordBean {
    private Integer id;
    private String recordProduct;//产品
    private String recordEmployee;//生产人
    private String recordDate;//生产日期
    private int recordAmount;//数量
    private boolean tag;

    public RecordBean(Integer id, String recordProduct, String recordEmployee, String recordDate, int recordAmount) {
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

    public String getRecordProduct() {
        return recordProduct;
    }

    public void setRecordProduct(String recordProduct) {
        this.recordProduct = recordProduct;
    }

    public String getRecordEmployee() {
        return recordEmployee;
    }

    public void setRecordEmployee(String recordEmployee) {
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