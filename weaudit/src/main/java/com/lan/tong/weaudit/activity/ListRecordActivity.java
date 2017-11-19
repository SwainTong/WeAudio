package com.lan.tong.weaudit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.bean.ProductBean;
import com.lan.tong.weaudit.bean.RecordBean;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Make;
import com.lan.tong.weaudit.domain.Product;
import com.lan.tong.weaudit.utils.ProductAdapter;
import com.lan.tong.weaudit.utils.ProductDialog;
import com.lan.tong.weaudit.utils.RecordAdapter;
import com.lan.tong.weaudit.utils.RecordDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ListRecordActivity extends AppCompatActivity {
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private ListView listView;
    private RecordAdapter adapter;
    private List<RecordBean> lists;
    //dialog
    private RecordDialog createUserDialog;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ListRecordActivity.this;
        setContentView(R.layout.activity_list_record);
        listView = findViewById(R.id.employee_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();//当前时间
                String date_now = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                //弹出新增对话框
                createUserDialog = new RecordDialog((Activity) context,0,0,date_now,0,R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_cancel_pop:
                                createUserDialog.dismiss();
                                Log.i("点击================","取消");
                                break;
                            case R.id.btn_save_pop:
                                int employeeId = Integer.parseInt(createUserDialog.getRecordEmployee().getSelectedItem().toString().split(" ")[0]);
                                int productId = Integer.parseInt(createUserDialog.getRecordProduct().getSelectedItem().toString().split(" ")[0]);
                                final String date = createUserDialog.getRecordDate().getText().toString().trim();
                                final int amount = Integer.parseInt(createUserDialog.getRecordAmount().getText().toString().trim());
                                if (!date.isEmpty()) {
                                    //创建要保存的类对象
                                    final Product product = DataSupport.find(Product.class,productId);
                                    final Employee employee = DataSupport.find(Employee.class,employeeId);
                                    Log.i("产品id------------",""+product.getId());
                                    final Make make = new Make(employee,product,date,amount);

                                    //确认对话框
                                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                    builder2.setTitle("添加生产记录");
                                    builder2.setIcon(R.drawable.addrecord);
                                    builder2.setMessage("记录信息确认:\n时间："+date+"\n生产者："+employee.getEmployeeName()+"\n产量："+amount+" "+product.getProductName()+product.getProductType());
                                    builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (make.save()) {
                                                //存储成功
                                                lists.add(0,new RecordBean(make.getId(),product,employee,date,amount));
                                                //lists.add();
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                    builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    builder2.show();

                                    createUserDialog.dismiss();
                                }else {
                                    Toast.makeText(context, "未填写完整！", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            default:
                                createUserDialog.dismiss();
                                break;
                        }
                    }
                });
                createUserDialog.show();
            }
        });
        fillInList();
    }
    //填充首页的listView
    public void fillInList(){
        lists=new ArrayList<RecordBean>();
        List<Make> myList = DataSupport.findAll(Make.class,true);
        for (int i=myList.size()-1;i>=0;--i){
            lists.add(new RecordBean(myList.get(i).getId(),myList.get(i).getProduct(),myList.get(i).getEmployee(),myList.get(i).getMakeDate(),myList.get(i).getMakeAmount()));
        }
        adapter=new RecordAdapter(ListRecordActivity.this,lists);
        listView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }
}
