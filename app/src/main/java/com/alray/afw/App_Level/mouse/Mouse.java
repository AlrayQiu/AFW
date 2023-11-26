package com.alray.afw.App_Level.mouse;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;

import com.alray.afw.R;

import androidx.annotation.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Mouse extends Service implements PropertyChangeListener {

    public static boolean isStarted = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    @Override//bound 数据监听
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private  void SetPos(Point p)
    {}

    private  void SetKey(short keyCode)
    {}


    private ImageView cursor;
    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 80;
        layoutParams.height = 80;
        layoutParams.x = 300;
        layoutParams.y = 300;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            cursor = new ImageView(this.getApplicationContext());
            cursor.setImageDrawable(getDrawable(R.drawable.cursor_draw));
            windowManager.addView(cursor, layoutParams);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
