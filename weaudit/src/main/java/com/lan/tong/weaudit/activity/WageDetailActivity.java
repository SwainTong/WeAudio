package com.lan.tong.weaudit.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Make;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WageDetailActivity extends AppCompatActivity {
    private int employee;
    private int month;

    private ListView wage_list;//总计
    private TextView total_wage;
    private TextView name;
    private TextView month_now;

    private DecimalFormat df = new DecimalFormat("#.0");
    private DecimalFormat mondf = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_detail);
        Intent intent = getIntent();
        employee= intent.getIntExtra("employee",0);
        month= intent.getIntExtra("month", 0);
        wage_list = findViewById(R.id.detail_list);
        total_wage = findViewById(R.id.wage_total);
        name = findViewById(R.id.wage_employee);
        month_now = findViewById(R.id.wage_month);
        findWageDetail();
    }
    //按月查询工资
    @SuppressLint("SimpleDateFormat")
    private void findWageDetail()
    {
        //获取当前年月日
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //先找出员工
        Employee mEemployee = DataSupport.find(Employee.class,employee,true);

        //list存放员工这个月每一笔生产记录
        List<Map<String,Object>> wageList = new ArrayList<Map<String,Object>>();
        //List<Make> recordList = DataSupport.where("employee_id = ? and makedate >= ? and makedate < ?",employee+"",year+"-"+month+"-1",year+"-"+month+1+"-1").find(Make.class,true);
        String str = "employee_id = " + employee + " and makedate >= '" + year+"-"+mondf.format(month)+"-01"+ "' and makedate <  '" + year+"-"+mondf.format(month + 1)+"-01'";
        //String str = "employee_id = 1 and makedate >= '2017-09-01' and makedate <= '2017-12-01'";
        List<Make> recordList = DataSupport.where(str).find(Make.class,true);
        double total = 0;
        //然后查记录，算工资
        for (int k = 0; k < recordList.size(); k++){
            double eWage = recordList.get(k).getProduct().getProductWage() * recordList.get(k).getMakeAmount();
            Map<String,Object> wage = new HashMap<String,Object>();
            wage.put("date",recordList.get(k).getMakeDate());
            wage.put("product",recordList.get(k).getProduct().getProductType() + recordList.get(k).getProduct().getProductName());
            wage.put("amount",recordList.get(k).getMakeAmount());
            wage.put("wage",df.format(eWage));
            wageList.add(wage);
            total += eWage;
        }
        SimpleAdapter sa=new SimpleAdapter(this,
                wageList,
                R.layout.wage_detail,
                new String[]{"date","product","amount","wage"},
                new int[]{R.id.wage_name,R.id.detail_product,R.id.detail_amount,R.id.wage_item});
        wage_list.setAdapter(sa);
        total_wage.setText(df.format(total));
        name.setText(mEemployee.getEmployeeName());
        month_now.setText(month+"");
    }
}
