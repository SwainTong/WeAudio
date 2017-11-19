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
import com.lan.tong.weaudit.bean.ProductBean;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Product;


public class ProductAdapter extends BaseSwipeAdapter {

    //dialog
    private ProductDialog createUserDialog;

    //保存每个list项目
    private List<ProductBean> items;
    private Context context;
    private OkHttpClient okHttpClient = new OkHttpClient();
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

    public ProductAdapter(Context context, List<ProductBean> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.product_item_content;
    }


    @Override
    public View generateView(final int i, ViewGroup viewGroup) {
        View view = View.inflate(context, R.layout.product_adapter, null);
        return view;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void fillValues(final int i, View view) {
        TextView productLogo = (TextView) view.findViewById(R.id.product_logo);
        TextView productId = (TextView) view.findViewById(R.id.product_id_item);
        TextView productName = (TextView) view.findViewById(R.id.product_name_item);
        TextView productWage = (TextView) view.findViewById(R.id.product_wage_item);
        TextView productMargin = (TextView) view.findViewById(R.id.product_margin_item);

        TextView tv_swipe_update = (TextView) view.findViewById(R.id.cb_swipe_update);
        TextView tv_swipe_delect = (TextView) view.findViewById(R.id.tv_swipe_delet);
        TextView tv_swipe_top = (TextView) view.findViewById(R.id.tv_swipe_top);

        //layout
        final SwipeLayout product_item_content = (SwipeLayout) view.findViewById(R.id.product_item_content);
        product_item_content.setShowMode(SwipeLayout.ShowMode.PullOut);

        //修改
        tv_swipe_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //弹出修改对话框
                createUserDialog = new ProductDialog((Activity) context,items.get(i).getProductType(),items.get(i).getProductName(),items.get(i).getProductWage(),items.get(i).getProductMargin(),R.style.Theme_AppCompat_Dialog,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_save_pop:
                                String type = createUserDialog.getProductType().getSelectedItem().toString().trim();
                                String name = createUserDialog.getProductName().getText().toString().trim();
                                double wage = Double.parseDouble(createUserDialog.getProductWage().getText().toString().trim());
                                double margin = Double.parseDouble(createUserDialog.getProductMargin().getText().toString().trim());
                                System.out.println(name+"——"+type);

                                ContentValues values = new ContentValues();
                                values.put("productName", name);
                                values.put("productType",type);
                                values.put("productWage",wage);
                                values.put("productMargin",margin);
                                DataSupport.update(Product.class,values,items.get(i).getId());
                                items.get(i).setProductName(name);
                                items.get(i).setProductType(type);
                                items.get(i).setProductWage(wage);
                                items.get(i).setProductMargin(margin);
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

        tv_swipe_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setTitle("删除产品");
                builder2.setIcon(R.drawable.delete);
                builder2.setMessage("确定要删除吗？");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Product.class, "id=?",items.get(i).getId().toString());
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



        //置顶操作
        tv_swipe_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,items.get(i).getProductName()+i,Toast.LENGTH_SHORT).show();
                items.add(0, items.get(i));
                items.remove(i + 1);
                notifyDataSetChanged();
                product_item_content.close();
            }
        });

        productLogo.setText(items.get(i).getProductName());
        productName.setText(items.get(i).getProductType()+items.get(i).getProductName());
        productId.setText(items.get(i).getId().toString());
        productWage.setText(String.valueOf(items.get(i).getProductWage()));
        productMargin.setText(String.valueOf(items.get(i).getProductMargin()));
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