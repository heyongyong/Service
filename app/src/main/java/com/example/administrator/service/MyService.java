package com.example.administrator.service;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    private String notificationId = "channelId";
    private String notificationName = "channelName";
    private NotificationManager manager;
    //设置日期格式
    private SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");

    private MyBinder mBinder = new MyBinder();

    //启动Service后，就可以在onCreate()或onStartCommand()方法里去执行一些具体的逻辑
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("my_service", "执行了onCreat()");

        //获取NotificationManager对象
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //添加下列代码将后台Service变成前台Service
        //构建“点击通知后打开MainActivity”的Intent对象
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        //新建Builer对象
        Notification.Builder builder = new Notification.Builder(this);
        //设置通知的标题
        builder.setContentTitle("标题");
        //设置通知的内容
        builder.setContentText("内容");
        //设置通知的图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置点击通知后的操作
        builder.setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001","my_channel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        //用当前时间充当通知的id，这里是为了区分不同的通知，如果是同一个id，前者就会被后者覆盖
        int requestId=(int) new Date().getTime();

        pendingIntent=PendingIntent.getActivity(this,requestId,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //发出通知，参数是（通知栏的id，设置内容的对象）
        manager.notify(requestId,builder.build());

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.e("my_service","执行了onStartCommand()");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("my_service","执行了onDestroy()");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        Log.e("my_service","执行了onBind()");
        //返回实例
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        Log.e("my_service","执行了onUnbind()");
        return super.onUnbind(intent);
    }

    //新建一个子类继承Binder类
    class MyBinder extends Binder{
        public void service_connect_Activity(){
            Log.e("my_service","Service关联了Activity，并在Activity执行了Service的方法");
        }
    }
}
