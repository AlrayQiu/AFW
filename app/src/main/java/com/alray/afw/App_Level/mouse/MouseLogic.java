package com.alray.afw.App_Level.mouse;

import android.graphics.Point;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Struct;

public class MouseLogic {

    static public boolean Connected = false;

    MouseLogic(PropertyChangeListener listener) {
        //构造函数，创建好Bound对象，用于实时修改参数
        addPropertyChangeListener(listener);
    }

    MouseInfo info = new MouseInfo();
    MouseInfo lastInfo = new MouseInfo();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void changeInfo(long controlCode) {
        if((controlCode & 0x0f00000000000000l) == 0x0100000000000000l)
        {
            info.pos.x = (int) (0xfff & controlCode);
            info.pos.y = (int) (0xfff & (controlCode >> 24));
        }
        else {

        }
        support.firePropertyChange("info", lastInfo, info);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
