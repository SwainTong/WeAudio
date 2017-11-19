package com.lan.tong.weaudit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.domain.Employee;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_add_employee)
public class AddEmployeeActivity extends AppCompatActivity {

    @ViewInject(R.id.employee_name)
    private EditText employeeName;
    @ViewInject(R.id.employee_phone)
    private EditText EmployeePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    //确认提交
    @Event(value = {R.id.add_button},type = View.OnClickListener.class)
    private void submit(View v)
    {
        String employee_name = employeeName.getText().toString();
        String employee_phone = EmployeePhone.getText().toString();
        if (!employee_name.isEmpty()) {
            Employee mEmployee = new Employee();
            mEmployee.setEmployeeName(employee_name);
            mEmployee.setEmployeePhone(employee_phone);
            if (mEmployee.save()) {
                Toast.makeText(this, "存储成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存储失败", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(AddEmployeeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "员工姓名未填写！", Toast.LENGTH_SHORT).show();
        }
    }

    //返回键
    @Event(value = {R.id.back},type = View.OnClickListener.class)
    private void addEmployeeBack(View v)
    {
        finish();
    }
}
