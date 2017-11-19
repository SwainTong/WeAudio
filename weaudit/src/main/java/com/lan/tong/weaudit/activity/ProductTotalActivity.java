package com.lan.tong.weaudit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Make;
import com.lan.tong.weaudit.domain.Product;

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

public class ProductTotalActivity extends AppCompatActivity {
    private SimpleAdapter sa; //创建一个SimpleAdapter
    private ListView wage_list;//总计
    private Spinner month;//月份下拉框
    private Integer mon;
    private DecimalFormat df = new DecimalFormat("#.0");
    private TextView total_wage;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_total);
        context = this;
        // Inflate the layout for this fragment
        Calendar calendar=Calendar.getInstance();
        mon = calendar.get(Calendar.MONTH)+1;
        wage_list = (ListView)findViewById(R.id.onefragment_content);
        total_wage = (TextView)findViewById(R.id.wage_total);
        month = (Spinner)findViewById(R.id.wage_month);
        getMonth();
        findWageByEmployee();
    }

    //按月查询工资，按产品id聚合
    private void findWageByEmployee()
    {
        //先找出所有产品
        List<Product> productList = DataSupport.findAll(Product.class);
        //list  分别存放每种产品的数量、id
        List<Map<String,Object>> wageList = new ArrayList<Map<String,Object>>();

        //然后挨个查记录，算结算账
        int pID,pAmount; Double total = 0.0,pMargin;String pName,pType;
        List<Make> makeList;//存每个人的生产记录
        for (int i = 0;i < productList.size();i++)
        {
            pID = productList.get(i).getId();
            pName = productList.get(i).getProductName();
            pType = productList.get(i).getProductType();
            makeList = DataSupport
                    .where("product_id = ?",String.valueOf(pID))
                    .find(Make.class,true);
            pAmount = 0;
            for (int k = 0; k < makeList.size(); k++)
            {
                //处理记录的时间
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(makeList.get(k).getMakeDate());
                    System.out.println(DateFormat.getDateInstance().format(date));
                    int m = date.getMonth();
                    if(m != mon)
                    {
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pAmount += makeList.get(k).getMakeAmount();
            }
            pMargin = Double.valueOf(df.format(pAmount * productList.get(i).getProductMargin()));
            Map<String,Object> wage = new HashMap<String,Object>();
            wage.put("id",pID);
            wage.put("name",pName);
            wage.put("type",pType);
            wage.put("amount",pAmount);
            wage.put("margin",pMargin);
            wageList.add(wage);
            total += pMargin;
        }
        sa=new SimpleAdapter(this,
                wageList,
                R.layout.statistics_product_total,
                new String[]{"name","type","amount","margin"},
                new int[]{R.id.product_total_name,R.id.product_total_type,R.id.product_total_amount,R.id.product_total_margin});
        wage_list.setAdapter(sa);
        total_wage.setText("总计："+df.format(total)+"元");
    }

    private void getMonth()
    {
        // 建立数据源
        String[] mItems = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        month.setAdapter(adapter);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mon = pos;
                findWageByEmployee();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
