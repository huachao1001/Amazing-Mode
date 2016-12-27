package com.huachao.plugin.util;

import com.huachao.plugin.Entity.CharObj;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by huachao on 2016/12/27.
 */
public class CharPanel implements Runnable {
    private JComponent mComponent;
    private Point mCurPosition;
    private Set<CharObj> charSet = new HashSet<CharObj>();
    private List<CharObj> bufferList = new ArrayList<CharObj>();


    private GlobalVar.State state = GlobalVar.getInstance().state;


    public void setComponent(JComponent component) {
        mComponent = component;
    }


    public void run() {
        while (state.IS_ENABLE) {
            if (GlobalVar.font != null) {
                synchronized (bufferList) {
                    charSet.addAll(bufferList);
                    bufferList.clear();
                }
                draw();
                int minFontSize = GlobalVar.font.getSize();

                //修改各个Label的属性，使之能以动画形式出现和消失
                Iterator<CharObj> it = charSet.iterator();
                while (it.hasNext()) {
                    CharObj obj = it.next();
                    if (obj.isAdd()) {//如果是添加到文本框
                        if (obj.getSize() <= minFontSize) {//当字体大小到达最小后，使之消失
                            mComponent.remove(obj.getLabel());
                            it.remove();
                        } else {//否则，继续减小
                            int size = obj.getSize() - 6 < minFontSize ? minFontSize : (obj.getSize() - 6);
                            obj.setSize(size);
                        }
                    } else {//如果是从文本框中删除
                        Point p = obj.getPosition();
                        if (p.y <= 0 || obj.getSize() <= 0) {//如果到达最底下，则清理
                            mComponent.remove(obj.getLabel());
                            it.remove();
                        } else {
                            p.y = p.y - 10;
                            int size = obj.getSize() - 1 < 0 ? 0 : (obj.getSize() - 1);
                            obj.setSize(size);
                        }
                    }
                }

            }
            try {
                if (charSet.isEmpty()) {
                    synchronized (charSet) {
                        charSet.wait();
                    }
                }
                Thread.currentThread().sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //绘制文本，本质上只是修改各个文本的位置和字体大小
    private void draw() {
        if (mComponent == null)
            return;

        for (CharObj obj : charSet) {
            JLabel label = obj.getLabel();

            Font font = new Font(GlobalVar.font.getName(), GlobalVar.font.getStyle(), obj.getSize());

            label.setFont(font);
            FontMetrics metrics = label.getFontMetrics(label.getFont());
            int textH = metrics.getHeight(); //字符串的高, 只和字体有关
            int textW = metrics.stringWidth(label.getText()); //字符串的宽
            label.setBounds(obj.getPosition().x, obj.getPosition().y - (textH - GlobalVar.minTextHeight), textW, textH);
        }
        mComponent.invalidate();
    }

    public void clearAllStr() {
        synchronized (bufferList) {
            bufferList.clear();
            charSet.clear();

            Iterator<CharObj> setIt = charSet.iterator();
            while (setIt.hasNext()) {
                CharObj obj = setIt.next();
                mComponent.remove(obj.getLabel());
            }

            Iterator<CharObj> bufferIt = bufferList.iterator();
            while (bufferIt.hasNext()) {
                CharObj obj = bufferIt.next();
                mComponent.remove(obj.getLabel());
            }

            System.out.println("清理 ");

        }
    }

    //单例模式，静态内部类
    private static class SingletonHolder {
        //静态初始化器，由JVM来保证线程安全
        private static CharPanel instance = new CharPanel();
    }

    //返回单例对象
    public static CharPanel getInstance(JComponent component) {
        if (component != null) {
            SingletonHolder.instance.mComponent = component;
        }
        return SingletonHolder.instance;
    }

    //由光标监听器回调，由此可动态获取当前光标位置
    public void setPosition(Point position) {
        this.mCurPosition = position;
    }

    /**
     * 将字符串添加到列表中。
     *
     * @isAdd 如果为true表示十新增字符串，否则为被删除字符串
     * @str 字符串
     */
    public void addStrToList(String str, boolean isAdd) {
        if (mComponent != null && mCurPosition != null) {

            CharObj charObj = new CharObj(mCurPosition.y);
            JLabel label = new JLabel(str);
            charObj.setStr(str);
            charObj.setAdd(isAdd);
            charObj.setLabel(label);
            if (isAdd)
                charObj.setSize(60);
            else
                charObj.setSize(GlobalVar.font.getSize());
            charObj.setPosition(mCurPosition);
            if (state.IS_RANDOM) {
                label.setForeground(randomColor());
            } else {
                label.setForeground(GlobalVar.defaultForgroundColor);
            }
            synchronized (bufferList) {
                bufferList.add(charObj);
            }
            if (charSet.isEmpty()) {
                synchronized (charSet) {
                    charSet.notify();
                }
            }

            mComponent.add(label);
        }
    }

    //以下用于产生随机颜色
    private static final Color[] COLORS = {Color.GREEN, Color.BLACK, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.RED, Color.CYAN, Color.MAGENTA};

    private Color randomColor() {
        int max = COLORS.length;
        int index = new Random().nextInt(max);
        return COLORS[index];
    }
}
