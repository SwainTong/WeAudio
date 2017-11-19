package com.lan.tong.weaudit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.bean.RecordBean;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Make;
import com.lan.tong.weaudit.domain.Product;

public class RecordAdapter extends BaseSwipeAdapter {
    //保存每个list项目
    private List<RecordBean> items;
    private Context context;
    private OkHttpClient okHttpClient = new OkHttpClient();

    //dialog
    private RecordDialog createUserDialog;

    //Handler回调
    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if(result.equals("success")){
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public RecordAdapter(Context context, List<RecordBean> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.product_item_content;
    }


    @Override
    public View generateView(final int i, ViewGroup viewGroup) {
        View view = View.inflate(context, R.layout.record_adapter, null);
        return view;
    }


    @Override
    public void fillValues(final int i, View view) {
        TextView record_logo = (TextView) view.findViewById(R.id.record_logo);
        TextView record_date_item = (TextView) view.findViewById(R.id.record_date_item);
        TextView record_employee_item = (TextView) view.findViewById(R.id.record_employee_item);
        TextView record_product_item = (TextView) view.findViewById(R.id.record_product_item);
        TextView record_amount_item = (TextView) view.findViewById(R.id.record_amount_item);
        TextView record_wage_item = (TextView) view.findViewById(R.id.record_wage_item);

        TextView tv_swipe_delete = (TextView) view.findViewById(R.id.tv_swipe_delect1);
        TextView tv_swipe_update = (TextView) view.findViewById(R.id.tv_swipe_update);

        //layout
        final SwipeLayout product_item_content = (SwipeLayout) view.findViewById(R.id.product_item_content);
        product_item_content.setShowMode(SwipeLayout.ShowMode.PullOut);

        tv_swipe_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setTitle("删除生产记录");
                builder2.setIcon(R.drawable.delete);
                builder2.setMessage("确定要删除吗？");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.delete(Make.class,items.get(i).getId());
                        items.remove(i);
                        notifyDataSetChanged();
                        product_item_content.close();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        product_item_content.close();
                    }
                });
                builder2.show();
                notifyDataSetChanged();
                product_item_content.close();
            }
        });




        tv_swipe_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改记录
                //Toast.makeText(context,"修改",Toast.LENGTH_SHORT).show();
                //弹出修改对话框
                createUserDialog = new RecordDialog((Activity) context,items.get(i).getRecordProduct().getId(),items.get(i).getRecordEmployee().getId(),items.get(i).getRecordDate(),items.get(i).getRecordAmount(),R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_save_pop:
                                int employeeId = Integer.parseInt(createUserDialog.getRecordEmployee().getSelectedItem().toString().split(" ")[0]);
                                int productId = Integer.parseInt(createUserDialog.getRecordProduct().getSelectedItem().toString().split(" ")[0]);
                                String date = createUserDialog.getRecordDate().getText().toString().trim();
                                int amount = Integer.parseInt(createUserDialog.getRecordAmount().getText().toString().trim());
                                //获取对象
                                Product product = DataSupport.find(Product.class,productId);
                                Employee employee = DataSupport.find(Employee.class,employeeId);
                                //更新内容
                                ContentValues values = new ContentValues();
                                values.put("employee_id", employeeId);
                                values.put("product_id",productId);
                                values.put("makedate",date);
                                values.put("makeamount",amount);
                                DataSupport.update(Make.class,values,items.get(i).getId());
                                items.get(i).setRecordProduct(product);
                                items.get(i).setRecordEmployee(employee);
                                items.get(i).setRecordDate(date);
                                items.get(i).setRecordAmount(amount);
                                createUserDialog.dismiss();
                                notifyDataSetChanged();
                                break;
                            default:
                                createUserDialog.dismiss();
                                break;
                        }
                    }
                });
                createUserDialog.show();
                product_item_content.close();
            }
        });

        record_logo.setText(items.get(i).getRecordEmployee().getEmployeeName());
        record_date_item.setText(items.get(i).getRecordDate());
        record_employee_item.setText(items.get(i).getRecordEmployee().getEmployeeName());
        record_product_item.setText(String.format("%s%s", items.get(i).getRecordProduct().getProductType(), items.get(i).getRecordProduct().getProductName()));
        record_amount_item.setText(String.valueOf(items.get(i).getRecordAmount()));
        record_wage_item.setText(String.valueOf(items.get(i).getRecordAmount() * items.get(i).getRecordProduct().getProductWage()));
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    private void exec(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.i("异常：", "--->" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                Log.i("成功：", "--->");
                String s = response.body().string();
                Log.i("结果：", "--->" + s);
                Message message1 = new Message();
                message1.what = 2;
                message1.obj = s;
                handler.sendMessage(message1);
            }
        });
    }
}