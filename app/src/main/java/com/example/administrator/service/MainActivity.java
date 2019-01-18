package com.example.administrator.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyService.MyBinder myBinder;

    //创建ServiceConnection的匿名类
    private ServiceConnection connection = new ServiceConnection(){
        //重写onServiceConnection()方法和onServiceDisconnected()方法
        //在Activity与Service建立关联和解除关联的时候调用
        @Override
        public void onServiceDisconnected(ComponentName name){
        }

        //在Activity与Service解除关联的时候调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            //实例化Service的内部类myBinder
            //通过向下转型得到了MyBinder的实例
            myBinder = (MyService.MyBinder) service;
            //在Activity调用Service类的方法
            myBinder.service_connect_Activity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mystart(View view){
        //构建启动服务的Intent对象
        Intent startIntent = new Intent(this,MyService.class);
        //调用startService()方法-传入Intent对象，以此启动服务
        startService(startIntent);
    }

    public void mystop(View view){
        //构建停止服务的Intent对象
        Intent stopIntent = new Intent(this,MyService.class);
        stopService(stopIntent);
    }

    public void bind_service(View view){
        //构建绑定服务的Intent对象
        Intent bindIntent = new Intent(this,MyService.class);
        //调用bindService()方法，以此停止服务
        bindService(bindIntent,connection,BIND_AUTO_CREATE);
        //参数说明
        //第一个参数：Intent对象
        //第二个参数：上面创建的Serviceconnection实例
        //第三个参数：标志位
        //这里传入BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service
        //这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行
    }

    public void unbind_service(View view){
        //调用unbindService()解绑服务
        //参数是上面创建的Serviceconnection实例
        unbindService(connection);
    }
}
