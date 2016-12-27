package com.huachao.plugin.listener;


import com.huachao.plugin.util.CharPanel;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;

import java.awt.*;

/**
 * Created by huachao on 2016/12/27.
 */
public class AmazingCaretListener implements CaretListener {

    @Override
    public void caretPositionChanged(CaretEvent caretEvent) {
        LogicalPosition logicalPosition = caretEvent.getNewPosition();
        Point position = caretEvent.getEditor().logicalPositionToXY(logicalPosition);
        CharPanel.getInstance(null).setPosition(position);
    }

    @Override
    public void caretAdded(CaretEvent caretEvent) {

    }

    @Override
    public void caretRemoved(CaretEvent caretEvent) {

    }
}
