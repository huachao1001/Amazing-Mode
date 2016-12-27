package com.huachao.plugin.action;

import com.btr.proxy.util.PlatformUtil;
import com.huachao.plugin.listener.AmazingDocumentListener;
import com.huachao.plugin.util.CharPanel;
import com.huachao.plugin.util.GlobalVar;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ComboBoxCompositeEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by huachao on 2016/12/27.
 */
public class EnableAction extends AnAction {
    private GlobalVar.State state = GlobalVar.getInstance().state;

    public EnableAction() {

    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null || project == null) {
            e.getPresentation().setEnabled(false);
        } else {
            JComponent component = editor.getContentComponent();
            if (component == null) {
                e.getPresentation().setEnabled(false);
            } else {
                e.getPresentation().setEnabled(true);
            }
        }
        updateState(e.getPresentation());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null || project == null) {
            return;
        }
        JComponent component = editor.getContentComponent();
        if (component == null)
            return;
        state.IS_ENABLE = !state.IS_ENABLE;
        updateState(e.getPresentation());

        //只要点击Enable项，就把缓存中所有的文本清理
        CharPanel.getInstance(component).clearAllStr();

        GlobalVar.registerDocumentListener(project, editor, state.IS_ENABLE);
    }


    private void updateState(Presentation presentation) {

        if (state.IS_ENABLE) {
            presentation.setText("Enable");
            presentation.setIcon(AllIcons.General.InspectionsOK);
        } else {
            presentation.setText("Disable");
            presentation.setIcon(AllIcons.Actions.Cancel);
        }
    }


}
