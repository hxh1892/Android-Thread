package com.hxh.thread;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    private static String TAG = "Thread";

    private TextView tv;

    //避免内存泄漏的Handler
    private BaseHandler handler = new BaseHandler(this, new BaseHandler.CallBack()
    {
        @Override
        public void callBack(Message msg)
        {
            switch (msg.what)
            {
                //判断标志位
                case 1:
                {
                    //移除what=1的信息
//                    removeMessages(1);

                    tv.setText(msg.obj.toString());

                    break;
                }
                default:
                    break;
            }
        }
    });

//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            super.handleMessage(msg);
//
//            switch (msg.what)
//            {
//                //判断标志位
//                case 1:
//                {
//                    //移除what=1的信息
////                    removeMessages(1);
//
//                    tv.setText(msg.obj.toString());
//
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    };

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            Log.i(TAG, "Handler:i=" + i);

            sendMessage();

            i++;

            handler.postDelayed(runnable, 1000);
        }
    };

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        //在结束Activity时候，删除所有回调和消息处理
        handler.removeCallbacksAndMessages(null);
    }

    public void handler(View v)
    {
        tv.setText("");
        i = 0;

        handler.postDelayed(runnable, 1000);
    }

    public void hs(View v)
    {
        handler.removeCallbacks(runnable);
    }

    private boolean threadMark = true;

    public void tr(View v)
    {
        threadMark = true;
        tv.setText("");
        i = 0;

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (threadMark)
                {
                    Log.i(TAG, "Thread:i=" + i);

                    sendMessage();

                    i++;

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        });

        thread.start();
    }

    public void trs(View v)
    {
        threadMark = false;
    }

    private Timer timer;

    public void timer(View v)
    {
        tv.setText("");
        i = 0;

        timer = new Timer();

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                Log.i(TAG, "Timer:i=" + i);

                sendMessage();

                i++;
            }
        };

        //0秒后每隔1秒执行一次
        timer.schedule(task, 0, 1000);
    }

    public void tis(View v)
    {
        if (timer != null) { timer.cancel(); }
    }

    private void sendMessage()
    {
        //创建新对象
        //Message message = new Message();
        //从整个Messge池中返回一个新的Message实例，节约内存资源
        Message message = handler.obtainMessage();
        message.obj = i + "";
        //标志消息的标志
        message.what = 1;

        //发送空message
//        handler.sendEmptyMessage(1);
        //指定时间发送空message
//        handler.sendEmptyMessageAtTime(1, SystemClock.uptimeMillis() + 1000);
        //1s后发送空message
//        handler.sendEmptyMessageDelayed(1, 1000);
        //发送message
        handler.sendMessage(message);
        //指定时间发送message
//        handler.sendMessageAtTime(message, SystemClock.uptimeMillis() + 1000);
        //1s后发送message
//        handler.sendMessageDelayed(message, 1000);
    }
}
