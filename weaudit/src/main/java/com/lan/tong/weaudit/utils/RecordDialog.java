package com.lan.tong.weaudit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Product;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//
//public class CreateUserDialog extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_user_dialog);
//    }
//}
public class RecordDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;

    //按钮
    private Button btn_save;
    private Button btn_cancel;

    public Spinner getRecordProduct() {
        return recordProduct;
    }

    public void setRecordProduct(Spinner recordProduct) {
        this.recordProduct = recordProduct;
    }

    public Spinner getRecordEmployee() {
        return recordEmployee;
    }

    public void setRecordEmployee(Spinner recordEmployee) {
        this.recordEmployee = recordEmployee;
    }

    public TextView getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(TextView recordDate) {
        this.recordDate = recordDate;
    }

    public EditText getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(EditText recordAmount) {
        this.recordAmount = recordAmount;
    }

    //输入框
    private Spinner recordProduct;
    private Spinner recordEmployee;
    private TextView recordDate;
    private EditText recordAmount;
    //内容
    private int productId;
    private int employeeId;
    private String date;
    private int amount;


    private View.OnClickListener mClickListener;

    public RecordDialog(Activity context,int productId,int employeeId,String date,int amount, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
        this.productId = productId;
        this.employeeId = employeeId;
        this.date = date;
        this.amount = amount;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_record);

        recordProduct = findViewById(R.id.record_product);
        recordEmployee = findViewById(R.id.record_employee);
        recordDate = findViewById(R.id.record_date);
        recordAmount = findViewById(R.id.record_amount);

        //productType.setPrompt(Type);
        recordDate.setText(date);
        recordAmount.setText(amount + "");


        // 建立数据源
        List<Product> productList = DataSupport.findAll(Product.class);
        List<Employee> employeeList = DataSupport.findAll(Employee.class);
        List<String> mProduct = new ArrayList<>();
        for (int i = 0; i < productList.size(); ++i)
        {
            mProduct.add(productList.get(i).getId() + " " + productList.get(i).getProductName() + productList.get(i).getProductType());
        }
        List<String> mEmployee = new ArrayList<>();
        for (int i = 0; i < employeeList.size(); ++i)
        {
            mEmployee.add(employeeList.get(i).getId() + " " + employeeList.get(i).getEmployeeName());
        }
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter1=new SpinnerAdapter(context,android.R.layout.simple_spinner_item, (String[])mProduct.toArray(new String[mProduct.size()]));
        //adapter1.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        //绑定 Adapter到控件
        recordProduct.setAdapter(adapter1);

        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter2=new SpinnerAdapter(context,android.R.layout.simple_spinner_item, (String[])mEmployee.toArray(new String[mEmployee.size()]));
        //adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice );
        //绑定 Adapter到控件
        recordEmployee.setAdapter(adapter2);

        //时间选择
        recordDate.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();//当前时间
            @Override
            public void onClick(View view) {
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(context,
                        // 绑定监听器(How the parent is notified that the date is set.)
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // 此处得到选择的时间，可以进行你想要的操作
                                recordDate.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                            }
                        }
                        // 设置初始日期
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

  /*
   * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
   * 对象,这样这可以以同样的方式改变这个Activity的属性.
   */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        // 根据id在布局中找到控件对象
        btn_save = (Button) findViewById(R.id.btn_save_pop);

        // 为按钮绑定点击事件监听器
        btn_save.setOnClickListener(mClickListener);

        //取消按钮
        btn_save = (Button) findViewById(R.id.btn_cancel_pop);
        btn_save.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }
    private class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[] {};

        public SpinnerAdapter(final Context context,
                              final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(22);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            // android.R.id.text1 is default text view in resource of the android.
            // android.R.layout.simple_spinner_item is default layout in resources of android.

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.LEFT);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(22);
            return convertView;
        }
    }
}
