package com.huachao.plugin.action;

import com.huachao.plugin.util.GlobalVar;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Created by HuaChao on 2016/12/27.
 */
public class RandomColorAction extends AnAction {
    private GlobalVar.State state = GlobalVar.getInstance().state;

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
        state.IS_RANDOM = !state.IS_RANDOM;
        updateState(e.getPresentation());

        GlobalVar.registerDocumentListener(project, editor,false);
    }

    private void updateState(Presentation presentation) {

        if (state.IS_RANDOM) {
            presentation.setIcon(AllIcons.General.InspectionsOK);
        } else {
            presentation.setIcon(AllIcons.Actions.Cancel);
        }
    }
}
