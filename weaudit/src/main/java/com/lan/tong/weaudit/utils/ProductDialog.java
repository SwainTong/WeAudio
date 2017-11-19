package com.lan.tong.weaudit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lan.tong.weaudit.R;

//
//public class CreateUserDialog extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_user_dialog);
//    }
//}
public class ProductDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;

    //按钮
    private Button btn_save;
    private Button btn_cancel;

    public Spinner getProductType() {
        return productType;
    }

    public void setProductType(Spinner productType) {
        this.productType = productType;
    }

    public EditText getProductName() {
        return productName;
    }

    public void setProductName(EditText productName) {
        this.productName = productName;
    }

    public EditText getProductWage() {
        return productWage;
    }

    public void setProductWage(EditText productWage) {
        this.productWage = productWage;
    }

    public EditText getProductMargin() {
        return productMargin;
    }

    public void setProductMargin(EditText productMargin) {
        this.productMargin = productMargin;
    }

    //输入框
    private Spinner productType;
    private EditText productName;
    private EditText productWage;
    private EditText productMargin;
    //内容
    String Type;
    String Name;
    double Wage;
    double Margin;


    private View.OnClickListener mClickListener;

    public ProductDialog(Activity context, String Type, String Name,double Wage,double Margin, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
        this.Type = Type;
        this.Name = Name;
        this.Wage = Wage;
        this.Margin = Margin;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_product);

        productType = findViewById(R.id.product_type);
        productName = findViewById(R.id.product_name);
        productWage = findViewById(R.id.product_wage);
        productMargin = findViewById(R.id.product_margin);

        //productType.setPrompt(Type);
        productName.setText(Name);
        productWage.setText(Wage + "");
        productMargin.setText(Margin + "");


        // 建立数据源
        String[] mItems = {"内衬","耳朵","拖把","毛巾"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        productType.setAdapter(adapter);
        productType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                //String[] languages = {"内衬","耳朵","拖把","毛巾"};
                //Toast.makeText(AddProductActivity.this, "你点击的是:"+languages[pos],Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
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
}
