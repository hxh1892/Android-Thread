package com.hxh.thread;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class BaseHandler extends Handler
{
    private WeakReference<Activity> wr;
    private CallBack callBack;

    public BaseHandler(Activity activity, CallBack callBack)
    {
        wr = new WeakReference<>(activity);
        this.callBack = callBack;
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        if (wr.get() != null)
        {
            callBack.callBack(msg);
        }
    }

    public interface CallBack
    {
        void callBack(Message msg);
    }
}
