package com.lan.tong.weaudit.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lan.tong.weaudit.R;
import com.lan.tong.weaudit.domain.Employee;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //建数据库
        SQLiteDatabase db = Connector.getDatabase();
        //填充首页ListView
        listView = findViewById(R.id.record_list);
        fillInList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.employee_wage) {
            Intent intent = new Intent(this,AddEmployeeActivity.class);
            startActivity(intent);
        } else if (id == R.id.product_wage) {

        } else if (id == R.id.employee_manage) {
            Intent intent = new Intent(this,ListEmployeeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //填充首页的listView
    public void fillInList(){
        //(1)根据Id创建布局控件相应的ListView类对象(之后会为这个对象添加适配器)
        //(2)创建适配器SimpleAdapter，它的构造函数包含以下参数：
        //      a)上下文对象
        //      b)所有要添加到ListView中的数据  （List对象）
        //      c)ListView中每一项的布局 （R.id）
        //      d)List中每个MAP对象的key值----sourse
        //      e)d)中每项key值数据指向的布局控件的id -----destination
        //     适配器的作用是把布局跟数据联系起来
        //从数据库找出所有员工记录
        List<Employee> employeeList = DataSupport.findAll(Employee.class);
        for (int i = 0; i < employeeList.size(); i ++)
        {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("pic",employeeList.get(i).getEmployeeName());
            map.put("name",employeeList.get(i).getEmployeePhone());
            list_map.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,/*传入一个上下文作为参数*/
                list_map,         /*传入相对应的数据源，这个数据源不仅仅是数据而且还是和界面相耦合的混合体。*/
                R.layout.list_item, /*设置具体某个items的布局，需要是新的布局，而不是ListView控件的布局*/
                new String[]{"pic","name"}, /*传入上面定义的键值对的键名称,会自动根据传入的键找到对应的值*/
                new int[]{R.id.item_img,R.id.item_name});/*传入items布局文件中需要指定传入的控件，这里直接上id即可*/
        listView.setAdapter(simpleAdapter);
    }
}
