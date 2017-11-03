package com.lan.tong.weaudit.bean;

/**
 * Created by Administrator on 2017/8/11.
 */

public class EmployeeBean {

    private Integer id;
    private String employeeName;//姓名
    private String employeePhone;


    public EmployeeBean(String employeeName, String employeePhone) {
        this.employeeName = employeeName;
        this.employeePhone = employeePhone;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

}