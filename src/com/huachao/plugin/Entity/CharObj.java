package com.huachao.plugin.Entity;

import javax.swing.*;
import java.awt.*;

/**
 * 用于封装一个字符对象
 * <p>
 * Created by huachao on 2016/12/27.
 */
public class CharObj {
    private int initY;
    private boolean isAdd;
    private String str;
    private int size;
    private JLabel label;
    private Point position;

    public CharObj(int initY) {
        this.initY = initY;
    }

    public int getInitY() {
        return initY;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JLabel getLabel() {
        return label;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public boolean isAdd() {
        return isAdd;
    }
}
