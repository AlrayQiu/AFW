package com.alray.afw.App_Level.mouse;

import android.graphics.Point;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MouseLogic  {
    MouseLogic(Mouse listener) {
        //构造函数，创建好Bound对象，用于实时修改参数
        MouseControlLMsg stockPrice = new MouseControlLMsg();
        stockPrice.addPropertyChangeListener(listener);

    }
    static class MouseControlLMsg {
        int LastInfo;
        private Point pos;
        private  short keyStatus;
        private PropertyChangeSupport support = new PropertyChangeSupport(this);

        public void changeInfo(int controlCode) {
            Point pos = new Point();
            short keyStatus = 0;

            support.firePropertyChange("pos", this.pos, pos);
            support.firePropertyChange("keyStatus", this.keyStatus, keyStatus);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            support.removePropertyChangeListener(listener);
        }

    }

}
