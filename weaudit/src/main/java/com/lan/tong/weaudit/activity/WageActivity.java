package com.lan.tong.weaudit.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class WageActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_wage);
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

    //按月查询工资，按员工id聚合
    private void findWageByEmployee()
    {
        //先找出所有员工
        List<Employee> employeeList = DataSupport.findAll(Employee.class);
        //list  分别存放每个员工的工资、id
        List<Map<String,Object>> wageList = new ArrayList<Map<String,Object>>();

        //然后挨个查记录，算工资
        int eID; Double eWage,total = 0.0;String eName;
        List<Make> makeList;//存每个人的生产记录
        for (int i = 0;i < employeeList.size();i++)
        {
            eID = employeeList.get(i).getId();
            eName = employeeList.get(i).getEmployeeName();
            makeList = DataSupport
                    .where("employee_id = ?",String.valueOf(eID))
                    .find(Make.class,true);
            eWage = 0.0;
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
                eWage += makeList.get(k).getProduct().getProductWage() * makeList.get(k).getMakeAmount();
            }
            Map<String,Object> wage = new HashMap<String,Object>();
            wage.put("id",eID);
            wage.put("name",eName);
            wage.put("wage",df.format(eWage));
            wageList.add(wage);
            total += eWage;
        }
        sa=new SimpleAdapter(this,
                wageList,
                R.layout.statistics,
                new String[]{"name","wage"},
                new int[]{R.id.wage_name,R.id.wage_item});
        wage_list.setAdapter(sa);
        total_wage.setText("总计："+df.format(total)+"元");
        //为listView对象进行监听：当点击子项目的时候触发
        wage_list.setOnItemClickListener(new ItemClickEvent());
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

    //继承OnItemClickListener，当子项目被点击的时候触发
    private class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //通过单击事件，获得单击选项的内容
            HashMap<String,Object> map = (HashMap<String, Object>) wage_list.getItemAtPosition(i);
            int eID = (int) map.get("id");

            Intent intent = new Intent(context, WageDetailActivity.class);
            intent.putExtra("employee", eID);
            intent.putExtra("month",mon+1);
            startActivity(intent);
        }
    }
}
