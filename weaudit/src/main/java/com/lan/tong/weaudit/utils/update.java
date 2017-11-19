package com.lan.tong.weaudit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lan.tong.weaudit.domain.Employee;
import com.lan.tong.weaudit.domain.Make;
import com.lan.tong.weaudit.domain.Product;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.litepal.crud.DataSupport.findAll;

public class update {
    private Context context;

    public update(Context context) {
        this.context = context;
    }

    //用来进行与后端通信的okHttpClient
    private OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder()
            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(20,TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(20,TimeUnit.SECONDS)//设置连接超时时间
            .build();

    //handler，在回调函数里监听,如果已经拿到了所有的活动的数据，就在listview显示出来
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){          // handle
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, "数据成功上传到云端！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    String jsonStr = (String) msg.obj;
                    JSONArray jsonArray = (JSONArray) JSON.parse(jsonStr);
                    Employee mEmployee = null;
                    JSONObject jsonObject = null;
                    for (int i = 0; i < jsonArray.size();i++)
                    {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        //先检查看看数据库有没有这条记录
                        List<Employee> myList = DataSupport
                                .where("employeeName = '"+jsonObject.getString("eName")+"'")
                                .find(Employee.class);
                        if(myList.size() == 0) {
                            //安卓端数据库没有这条记录，加进去
                            mEmployee = new Employee();
                            mEmployee.setEmployeeName(jsonObject.getString("eName"));
                            mEmployee.setEmployeePhone(jsonObject.getString("ePhone"));
                            mEmployee.setEmployeePicture(jsonObject.getString("ePic"));
                            mEmployee.save();
                            Log.i("添加=------",jsonObject.getString("eName"));
                            mEmployee = null;
                        }
                    }
                    Toast.makeText(context, "员工数据已从云端下载！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String productStr = (String) msg.obj;
                    JSONArray productJsonArray = (JSONArray) JSON.parse(productStr);
                    Product mproduct = null;
                    JSONObject productJsonObject = null;
                    for (int i = 0; i < productJsonArray.size();i++)
                    {
                        productJsonObject = (JSONObject) productJsonArray.get(i);
                        //先检查看看数据库有没有这条记录
                        List<Product> myList = DataSupport
                                .where("productName = '"+productJsonObject.getString("pName")+"' and productType = '"+productJsonObject.getString("pType")+"'")
                                .find(Product.class);
                        if(myList.size() == 0) {
                            //安卓端数据库没有这条记录，加进去
                            mproduct = new Product();
                            mproduct.setProductName(productJsonObject.getString("pName"));
                            mproduct.setProductType(productJsonObject.getString("pType"));
                            mproduct.setProductAmount(productJsonObject.getInteger("pAmount"));
                            mproduct.setProductMargin(productJsonObject.getDouble("pMargin"));
                            mproduct.setProductWage(productJsonObject.getDouble("pWage"));
                            mproduct.save();
                            Log.i("添加=------",productJsonObject.getString("pName"));
                            mproduct = null;
                        }
                    }
                    Toast.makeText(context, "产品数据已从云端下载！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    String recordStr = (String) msg.obj;
                    JSONArray recordJsonArray = (JSONArray) JSON.parse(recordStr);
                    Make record = null;
                    JSONObject recordJsonObject = null;
                    for (int i = 0; i < recordJsonArray.size();i++)
                    {
                        recordJsonObject = (JSONObject) recordJsonArray.get(i);
                        //String[] rDate = recordJsonObject.getString("rDate").split("-");
                        //String date = rDate[0]+"-"+Integer.valueOf(rDate[1])+"-"+Integer.valueOf(rDate[2]);
                        Employee rEmployee = DataSupport
                                .where("employeeName = '"+recordJsonObject.getString("rEmpName")+"'")
                                .find(Employee.class).get(0);
                        Product rProduct = DataSupport
                                .where("productName = '"+recordJsonObject.getString("rProName")+"'and productType = '"+recordJsonObject.getString("rProType")+"'")
                                .find(Product.class).get(0);
                        //先检查看看数据库有没有这条记录
                        List<Make> myList = DataSupport
                                .where("makeDate = '"+recordJsonObject.getString("rDate")+"' and employee_id = "+rEmployee.getId()+" and product_id = "+rProduct.getId())
                                .find(Make.class);
                        if(myList.size() == 0) {
                            //安卓端数据库没有这条记录，加进去
                            record = new Make();
                            record.setMakeAmount(recordJsonObject.getInteger("rAmount"));
                            record.setMakeDate(recordJsonObject.getString("rDate"));
                            record.setEmployee(rEmployee);
                            record.setProduct(rProduct);
                            Log.i("添加=------",recordJsonObject.getString("rDate")+rEmployee.getEmployeeName());
                            record.save();
                            mproduct = null;
                        }
                    }
                    Toast.makeText(context, "记录数据已从云端下载！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    //上传数据
    public void upload()
    {
        //使用post的方式，向后端action发起传输请求
        //从数据库读取员工信息
        List<Employee> myList = findAll(Employee.class);
        JSONArray jsonArray = new JSONArray();
        JSONObject tmpObj = null;
        Employee tmpEmp = null;
        String server_addr = "http://androidybtxmanager.azurewebsites.net/WorkManager/";
        for(int i = 0; i < myList.size();i++)
        {
            tmpEmp = myList.get(i);
            tmpObj = new JSONObject();
            tmpObj.put("eid",tmpEmp.getId());
            tmpObj.put("ename",tmpEmp.getEmployeeName());
            tmpObj.put("ephone",tmpEmp.getEmployeePhone());
            tmpObj.put("epic",tmpEmp.getEmployeePicture());
            jsonArray.add(tmpObj);
            tmpObj = null;
        }
        String employeeInfos = jsonArray.toString(); // 将JSONArray转换得到String
        FormBody.Builder builder1 = new FormBody.Builder();
        FormBody formBody = builder1.add("content",employeeInfos).build();
        Request.Builder builder = new Request.Builder();
        Request request1 = builder.url(server_addr+"Employee_dbDownload")
                .post(formBody)
                .build();
        exec(request1);


        //准备把安卓端的产品信息上传到服务器
        jsonArray.clear();
        Product tmpPro = null;
        //从数据库读取产品信息
        List<Product> productList = findAll(Product.class);
        for(int i = 0; i < productList.size();i++)
        {
            tmpPro = productList.get(i);
            tmpObj = new JSONObject();
            tmpObj.put("pName",tmpPro.getProductName());
            tmpObj.put("pType",tmpPro.getProductType());
            tmpObj.put("pAmount",tmpPro.getProductAmount());
            tmpObj.put("pMargin",tmpPro.getProductMargin());
            tmpObj.put("pWage",tmpPro.getProductWage());
            jsonArray.add(tmpObj);
            tmpObj = null;
        }
        String productInfos = jsonArray.toString(); // 将JSONArray转换得到String
        FormBody.Builder builder2 = new FormBody.Builder();
        FormBody formBody2 = builder2.add("content",productInfos).build();
        Request.Builder builder3 = new Request.Builder();
        Request request2 = builder3.url(server_addr+"Product_dbDownload")
                .post(formBody2)
                .build();
        exec(request2);

        //准备把安卓端的记录信息上传到服务器
        jsonArray.clear();
        Make tmpRecord = null;
        //从数据库读取记录信息
        List<Make> recordList = findAll(Make.class,true);
        for(int i = 0; i < recordList.size();i++)
        {
            tmpRecord = recordList.get(i);
            tmpObj = new JSONObject();
            tmpObj.put("rDate",tmpRecord.getMakeDate());
            tmpObj.put("rAmount",tmpRecord.getMakeAmount());
            tmpObj.put("rEmpName",tmpRecord.getEmployee().getEmployeeName());
            tmpObj.put("rProName",tmpRecord.getProduct().getProductName());
            tmpObj.put("rProType",tmpRecord.getProduct().getProductType());
            jsonArray.add(tmpObj);
            tmpObj = null;
        }
        String makeInfos = jsonArray.toString(); // 将JSONArray转换得到String
        Log.i("验证---------",makeInfos);
        FormBody.Builder builder4 = new FormBody.Builder();
        FormBody formBody3 = builder4.add("content",makeInfos).build();
        Request.Builder builder5 = new Request.Builder();
        Request request3 = builder5.url(server_addr+"Make_dbDownload")
                .post(formBody3)
                .build();
        exec(request3);
    }

    //下载数据
    public void download()
    {
        //使用post的方式，向后端action发起传输请求
        //从数据库读取员工信息
        FormBody.Builder builder1 = new FormBody.Builder();
        FormBody formBody = builder1.build();
        Request.Builder builder = new Request.Builder();
        String server_addr = "http://androidybtxmanager.azurewebsites.net/WorkManager/";
        //请求员工信息
        Request request1 = builder.url(server_addr+"Employee_dbUpload")
                .post(formBody)
                .build();
        exec(request1);
        //请求产品
        Request request2 = builder.url(server_addr+"Product_dbUpload")
                .post(formBody)
                .build();
        exec(request2);
        //请求记录
        Request request3 = builder.url(server_addr+"Make_dbUpload")
                .post(formBody)
                .build();
        exec(request3);
    }
    //将发送request的过程和回调函数的定义封装成一个方法
    private void exec(Request request) {
        Log.i("超时=================",okHttpClient.connectTimeoutMillis()+"");

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("失败：","-----"+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("成功：","-----");

                //从服务器传回来的json字符串
                final String msg = response.body().string();
                Log.i("结果==============",msg);
                Message message = new Message();
                if (msg.equals("download\r\n"))
                {
                    message.what = 0;
                }
                else {
                    //收到的是服务器端的数据信息
                    String data_type = msg.substring(0,3);
                    Log.i("数据类型==============",data_type);
                    if (data_type.equals("emp")){
                        message.what = 1;
                    }
                    else if (data_type.equals("pro")) {
                        message.what = 2;
                    }
                    else if (data_type.equals("rec")) {
                        message.what = 3;
                    }

                    message.obj = msg.substring(3,msg.length());
                    Log.i("结果==============", (String) message.obj);
                }
                handler.sendMessage(message);
            }
        });
    }
}
