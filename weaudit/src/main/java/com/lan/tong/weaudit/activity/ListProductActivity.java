package com.lan.tong.weaudit.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.bean.EmployeeBean;
import com.lan.tong.weaudit.bean.ProductBean;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Product;
import com.lan.tong.weaudit.utils.CreateUserDialog;
import com.lan.tong.weaudit.utils.EmployeeAdapter;
import com.lan.tong.weaudit.utils.ProductAdapter;
import com.lan.tong.weaudit.utils.ProductDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListProductActivity extends AppCompatActivity {
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private ListView listView;
    private ProductAdapter adapter;
    private List<ProductBean> lists;
    //dialog
    private ProductDialog createUserDialog;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ListProductActivity.this;
        setContentView(R.layout.activity_list_product);
        listView = findViewById(R.id.employee_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出修改对话框
                createUserDialog = new ProductDialog((Activity) context,"","",0,0,R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_cancel_pop:
                                createUserDialog.dismiss();
                                Log.i("点击================","取消");
                                break;
                            case R.id.btn_save_pop:
                                String type = createUserDialog.getProductType().getSelectedItem().toString().trim();
                                String name = createUserDialog.getProductName().getText().toString().trim();
                                double wage = Double.parseDouble(createUserDialog.getProductWage().getText().toString().trim());
                                double margin = Double.parseDouble(createUserDialog.getProductMargin().getText().toString().trim());
                                System.out.println(name+"——"+type);
                                if (!name.isEmpty()) {
                                    //创建要保存的类对象
                                    Product mProduct = new Product(name,type,wage,margin);
                                    if (mProduct.save()) {
                                        //存储成功
                                        lists.add(new ProductBean(mProduct.getId(), name,type,wage,margin));
                                        adapter.notifyDataSetChanged();
                                    }
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
        lists=new ArrayList<ProductBean>();
        List<Product> myList = DataSupport.findAll(Product.class);
        for (int i=0;i<myList.size();i++){
            lists.add(new ProductBean(myList.get(i).getId(),myList.get(i).getProductName(),myList.get(i).getProductType(),myList.get(i).getProductWage(),myList.get(i).getProductMargin()));
        }
        adapter=new ProductAdapter(ListProductActivity.this,lists);
        listView.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }
}
