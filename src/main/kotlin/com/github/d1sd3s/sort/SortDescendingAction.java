package com.github.d1sd3s.sort;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SortDescendingAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();
        Caret cursor = editor.getCaretModel().getPrimaryCaret();
        SelectionHandler selection = new SelectionHandler(editor, project, document, cursor);
        selection.sort(Direction.DESCENDING);
        selection.replaceInline();
    }
}
