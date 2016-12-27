package com.huachao.plugin.listener;

import com.huachao.plugin.util.CharPanel;
import com.huachao.plugin.util.GlobalVar;
import com.intellij.CommonBundle;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by huachao on 2016/12/27.
 */
public class AmazingTypedActionHandler implements TypedActionHandler {
    private TypedActionHandler mLastHandler;

    public AmazingTypedActionHandler(TypedActionHandler lastHandler) {
        mLastHandler = lastHandler;
    }

    @Override
    public void execute(@NotNull Editor editor, char c, @NotNull DataContext dataContext) {
        if (!GlobalVar.hasAddListener) {
            Project project = PlatformDataKeys.PROJECT.getData(dataContext);
            Editor editor1 = PlatformDataKeys.EDITOR.getData(dataContext);
            GlobalVar.registerDocumentListener(project, editor, false);
        }
        if (mLastHandler != null)
            mLastHandler.execute(editor, c, dataContext);

    }
}
