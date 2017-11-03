package com.lan.tong.weaudit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.daimajia.swipe.util.Attributes;
import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.bean.EmployeeBean;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.utils.EmployeeAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEmployeeActivity extends AppCompatActivity {
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private ListView listView;
    //private int[] itemIds = {R.drawable.back,R.drawable.back,R.drawable.back1,R.drawable.back1,R.drawable.add,R.drawable.back1};
    //private String[] data = { "Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);
        listView = findViewById(R.id.employee_list);
        fillInList();
    }
    //填充首页的listView
    public void fillInList(){
        List<EmployeeBean> lists=new ArrayList<EmployeeBean>();
        List<Employee> myList = DataSupport.findAll(Employee.class);
        for (int i=0;i<myList.size();i++){
            lists.add(new EmployeeBean(myList.get(i).getEmployeeName(),myList.get(i).getEmployeePhone()));
        }
        EmployeeAdapter adapter=new EmployeeAdapter(ListEmployeeActivity.this,lists);
        listView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }
}
