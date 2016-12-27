package com.huachao.plugin.listener;


import com.huachao.plugin.util.CharPanel;
import com.huachao.plugin.util.GlobalVar;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Created by huachao on 2016/12/27.
 */
public class AmazingDocumentListener implements DocumentListener {
    private Project mProject;
    private Editor mEditor;

    private AmazingCaretListener caretListener;


    public AmazingDocumentListener(Project project) {
        mProject = project;
        //添加光标移动监听器
        caretListener = new AmazingCaretListener();
    }

    @Override
    public void beforeDocumentChange(DocumentEvent documentEvent) {


        if (mEditor == null) {
            mEditor = FileEditorManager.getInstance(mProject).getSelectedTextEditor();
            if (mEditor == null)
                return;
        }

        //添加光标移动监听器
        CaretModel caretModel = mEditor.getCaretModel();
        caretModel.addCaretListener(caretListener);

        //更新全局变量
        GlobalVar.updateGlobalVar(mEditor);


        JComponent editorComponent = mEditor.getContentComponent();
        CharPanel charPanel = CharPanel.getInstance(editorComponent);
        //删除字符串
        String deleteStr = documentEvent.getOldFragment().toString().trim();
        if (deleteStr.length() > 0) {
            charPanel.addStrToList(deleteStr, false);
        }
        //添加字符串
        String newStr = documentEvent.getNewFragment().toString().trim();
        if (newStr.length() > 0) {
            charPanel.addStrToList(newStr, true);
        }
    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {

    }
}
