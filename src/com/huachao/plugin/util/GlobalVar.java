package com.huachao.plugin.util;

import com.huachao.plugin.listener.AmazingDocumentListener;
import com.huachao.plugin.listener.AmazingTypedActionHandler;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * 配置文件
 * Created by huachao on 2016/12/27.
 */
@State(
        name = "amazing-mode",
        storages = {
                @Storage(
                        id = "amazing-mode",
                        file = "$APP_CONFIG$/amazing-mode_setting.xml"
                )
        }
)
public class GlobalVar implements PersistentStateComponent<GlobalVar.State> {
    //全局共享
    public static Font font;
    public static Color defaultForgroundColor;
    public static int minTextHeight;
    public static boolean hasAddListener = false;


    private static AmazingTypedActionHandler typedActionHandler;

    static {
        //添加按键监听
        EditorActionManager actionManager = EditorActionManager.getInstance();
        TypedAction typedAction = actionManager.getTypedAction();
        TypedActionHandler lastHandler = typedAction.getHandler();
        typedActionHandler = new AmazingTypedActionHandler(lastHandler);
        typedAction.setupHandler(typedActionHandler);
    }

    //当Editor有可能发生变化时，应当调用一次updateGlobalVar
    public static void updateGlobalVar(Editor editor) {
        font = editor.getColorsScheme().getFont(EditorFontType.PLAIN);
        defaultForgroundColor = editor.getColorsScheme().getDefaultForeground();
        minTextHeight = new Label("A").getFontMetrics(GlobalVar.font).getHeight();
    }

    private static AmazingDocumentListener amazingDocumentListener = null;

    public static void registerDocumentListener(Project project, Editor editor, boolean isFromEnableAction) {
        if (!hasAddListener || isFromEnableAction) {
            hasAddListener = true;
            JComponent component = editor.getContentComponent();
            if (component == null)
                return;
            if (amazingDocumentListener == null) {

                amazingDocumentListener = new AmazingDocumentListener(project);
                Document document = editor.getDocument();
                document.addDocumentListener(amazingDocumentListener);
            }

            Thread thread = new Thread(CharPanel.getInstance(component));
            thread.start();
        }
    }


    //--------------------//
    //以下为本地状态保存相关代码
    public static final class State {
        public boolean IS_ENABLE;
        public boolean IS_RANDOM;
    }

    @Nullable
    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public void loadState(State state) {
        this.state = state;
    }

    public State state = new State();

    public GlobalVar() {

        state.IS_ENABLE = false;
        state.IS_RANDOM = false;
    }

    public static GlobalVar getInstance() {
        return ServiceManager.getService(GlobalVar.class);
    }

}

