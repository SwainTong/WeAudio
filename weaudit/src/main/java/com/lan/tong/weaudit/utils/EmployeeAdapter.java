package com.lan.tong.weaudit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.lan.tong.weaudit.activity.ListEmployeeActivity;
import com.lan.tong.weaudit.bean.EmployeeBean;
import com.lan.tong.weaudit.domain.Employee;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.lan.tong.weaudit.R;

public class EmployeeAdapter extends BaseSwipeAdapter {

    //dialog
    private CreateUserDialog createUserDialog;
    //保存每个list项目
    private List<EmployeeBean> items;
    private Context context;
    private OkHttpClient okHttpClient = new OkHttpClient();

    //Handler回调
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if(result.equals("success")){
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public EmployeeAdapter(Context context, List<EmployeeBean> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.sl_content;
    }


    @Override
    public View generateView(final int i, ViewGroup viewGroup) {
        return View.inflate(context, R.layout.employee_adapter, null);
    }


    @Override
    public void fillValues(final int i, View view) {
        TextView employeeLogo = (TextView) view.findViewById(R.id.employee_name_logo);
        TextView employeeName = (TextView) view.findViewById(R.id.employee_name_item);
        TextView employeePhone = (TextView) view.findViewById(R.id.employee_phone_item);

        //是否标记
        TextView tv_swipe_update = (TextView) view.findViewById(R.id.cb_swipe_update);
        TextView tv_swipe_delect1 = (TextView) view.findViewById(R.id.tv_swipe_delect1);
        TextView tv_swipe_top1 = (TextView) view.findViewById(R.id.tv_swipe_top1);

        //layout
        final SwipeLayout sl_content = (SwipeLayout) view.findViewById(R.id.sl_content);
        sl_content.setShowMode(SwipeLayout.ShowMode.PullOut);

        tv_swipe_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //弹出修改对话框
                createUserDialog = new CreateUserDialog((Activity) context,items.get(i).getEmployeeName(),items.get(i).getEmployeePhone(),R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_save_pop:
                                String name = createUserDialog.text_name.getText().toString().trim();
                                String mobile = createUserDialog.text_mobile.getText().toString().trim();
                                System.out.println(name+"——"+mobile);
//                                List<Employee> employeeList;
//                                employeeList = DataSupport.where("employeeName=?",items.get(i).getEmployeeName()).find(Employee.class);
                                //找到要修改的项
                                //Employee employee = DataSupport.find(Employee.class,items.get(i).getId());
                                ContentValues values = new ContentValues();
                                values.put("employeeName", name);
                                values.put("employeePhone",mobile);
                                DataSupport.update(Employee.class,values,items.get(i).getId());
                                items.get(i).setEmployeeName(name);
                                items.get(i).setEmployeePhone(mobile);
                                createUserDialog.dismiss();
                                notifyDataSetChanged();
                                //Intent intent = new Intent(context,ListEmployeeActivity.class);
                                //context.startActivity(intent);
                                //((Activity) context).finish();
                                break;
                            default:
                                createUserDialog.dismiss();
                                break;
                        }
                    }
                });
                createUserDialog.show();
                sl_content.close();
            }
        });



        tv_swipe_delect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setTitle("删除员工");
                builder2.setIcon(R.drawable.delete);
                builder2.setMessage("确定要删除吗？");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Employee.class, "employeeName=?",items.get(i).getEmployeeName());
                        items.remove(i);
                        notifyDataSetChanged();
                        sl_content.close();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sl_content.close();
                    }
                });
                builder2.show();
                notifyDataSetChanged();
                sl_content.close();
            }
        });

        tv_swipe_top1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,items.get(i).getEmployeeName()+i,Toast.LENGTH_SHORT).show();
//                items.add(items.get(position));
                items.add(0, items.get(i));
                items.remove(i + 1);
                notifyDataSetChanged();
                sl_content.close();
            }
        });

        employeeLogo.setText(items.get(i).getEmployeeName().substring(0,1));
        employeeName.setText(items.get(i).getEmployeeName());
        employeePhone.setText(items.get(i).getEmployeePhone());
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


    class ViewHolder{
        TextView name;
        TextView org;
        TextView count;
        TextView extra;
    }
    private void exec(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
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