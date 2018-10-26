package com.cdivtc.directorydemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //第一步：定义变量
    //控件变量
    private EditText mEtName;
    private EditText mEtPhone;
    private TextView mTvShow;
    private Button mBtnAdd;
    private Button mBtnQuery;
    private Button mBtnUpdate;
    private Button mBtnDelete;
    //数据库类对象
    MyHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //第二步：创建数据库
        //实例化一个数据库
        myHelper = new MyHelper(MainActivity.this,"telephone.db",null,1);
        //第三步：初始化控件变量
        initial();
        //第四步：编写按钮的逻辑功能
        final Button.OnClickListener bListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义四个变量，分别为姓名，电话，数据库，存储数据的变量
                String name;
                String phone;
                SQLiteDatabase db;
                ContentValues values;
               switch (v.getId()){
                   case R.id.btn_add://单击的是添加按钮，实现插入一条数据的功能
                       //获取输入的姓名和电话号码
                       name = mEtName.getText().toString();
                       phone = mEtPhone.getText().toString();
                       //要想将获取到数据存放到数据库，必须先获取到数据库
                       db = myHelper.getWritableDatabase();
                       //实例化一个ContentValues对象，用来存放获取到的姓名和电话
                       values = new ContentValues();
                       //将姓名和电话添加到ContentValues对象中
                       values.put("name",name);
                       values.put("phone",phone);
                       //数据库执行插入操作，调用的是insert方法
                       db.insert("information",null,values);
                       //给出一个提示信息，告诉用户查询成功
                       Toast.makeText(MainActivity.this,"信息已添加",Toast.LENGTH_SHORT).show();
                       //关闭数据库
                       db.close();
                       break;
                   case R.id.btn_query://执行查询操作，查询通讯录中有哪些联系人
                       //获取要查询数据的数据库
                       db = myHelper.getWritableDatabase();
                       //查询数据库中的联系人，将查询结果返回一个游标指针，指定最后一条记录的位置
                       Cursor cursor = db.query("information", null, null, null, null, null, null);
                       //将查询结果取出 并显示到TextView控件中
                       //通过cusor调用getCount()方法，来获取查询的记录总数
                       //判断是否查询到了数据
                       if (cursor.getCount() == 0){
                           //没有查询到数据，则将TextView控件内容设置为空
                           mTvShow.setText("");
                           //给出一条提示信息，告诉用户没有查询到数据
                           Toast.makeText(MainActivity.this,"没有查询到数据",Toast.LENGTH_SHORT).show();
                       }else {
                           //先将第一条数据显示出来
                           //将游标指针移动到第一条数据的位置
                           cursor.moveToFirst();
                           //将数据显示到TextView控件中
                           mTvShow.setText("Name:  "+cursor.getString(1)+"； Phone:  "+cursor.getString(2));
                       }
                       //通过while循环，来获取第一条以外的数据，并显示出来
                       while (cursor.moveToNext()){
                           //将获取到的数据，追加到第一条数据之后，并且每行只显示一条记录
                           mTvShow.append("\n"+"Name:  "+cursor.getString(1)+"； Phone:  "+cursor.getString(2));
                       }
                       //关闭游标
                       cursor.close();
                       //关闭数据库
                       db.close();
                       break;
                   case R.id.btn_update://更新一条数据，以姓名为条件，修改电话号码
                       //获取要更新数据的数据库
                       db = myHelper.getWritableDatabase();
                       //实例化一个ContentValues对象，用来存放修改后的数据
                       values = new ContentValues();
                       //将修改的数据存放到values中
                       values.put("phone",phone = mEtPhone.getText().toString());
                       //执行修改操作，条件是姓名，姓名的值是通过EditText获取到
                       db.update("information",values,"name=?",new String[]{mEtName.getText().toString()});
                       //给出一条提示信息，告诉用户信息已修改
                       Toast.makeText(MainActivity.this,"信息已修改",Toast.LENGTH_SHORT).show();
                       //关闭数据库
                       db.close();
                       break;
                   case R.id.btn_delete://删除通讯录中的所有信息
                       //获取要删除的数据库
                       db = myHelper.getWritableDatabase();
                       //执行删除操作
                       db.delete("information","name=?",new String[]{mEtName.getText().toString()});
                       //给出一条提示信息，告诉用户信息已删除
                       Toast.makeText(MainActivity.this,"信息已删除",Toast.LENGTH_SHORT).show();
                       //将TextView的内容设置为空
                       mTvShow.setText("");
                       //关闭数据库
                       db.close();
                       break;

               }
            }
        };
        //四个按钮分别调用单击监听事件，完成单击的功能
        mBtnAdd.setOnClickListener(bListener);
        mBtnQuery.setOnClickListener(bListener);
        mBtnUpdate.setOnClickListener(bListener);
        mBtnDelete.setOnClickListener(bListener);
    }

    private void initial() {
        mEtName = findViewById(R.id.et_name);
        mEtPhone = findViewById(R.id.et_phone);
        mTvShow = findViewById(R.id.tv_show);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnQuery = findViewById(R.id.btn_query);
        mBtnUpdate = findViewById(R.id.btn_update);
    }
}
