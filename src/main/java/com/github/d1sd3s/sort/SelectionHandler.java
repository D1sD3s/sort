package com.github.d1sd3s.sort;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionHandler {
    private List<String> lines;
    private Editor editor;
    private Project project;
    private Document document;
    private int selectionStartLine;
    private int selectionEndLine;

    public SelectionHandler(Editor editor, Project project, Document doc, Caret cursor) {
        if (!isTextSelected(editor)) {
            Messages.showInfoMessage("Nothing to sort. Select lines and try again.", "No Selection");
            return;
        }
        this.editor = editor;
        this.project = project;
        this.document = doc;
        this.selectionStartLine = doc.getLineNumber(cursor.getSelectionStart());
        this.selectionEndLine = doc.getLineNumber(cursor.getSelectionEnd());
        this.lines = getLines(this.selectionStartLine, this.selectionEndLine, doc);
    }
    public void sort(Direction direction){
        switch (direction){
            case ASCENDING :
                Collections.sort(this.lines);
                break;
            case DESCENDING:
                Collections.sort(this.lines);
                Collections.reverse(this.lines);
                break;
            default:
                throw new EnumConstantNotPresentException(Direction.class, "direction");
        }
    }
    public void replaceInline() {
        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            int startOffset = this.document.getLineStartOffset(this.selectionStartLine);
            int endOffset = this.document.getLineEndOffset(this.selectionEndLine);
            this.editor.getDocument().replaceString(startOffset, endOffset, String.join("\n", this.lines));
        });
    }
    private boolean isTextSelected(Editor editor) {
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        return selectedText != null;
    }

    private List<String> getLines(int startLine, int endLine, Document doc) {
        List<String> selectedLinesList = new ArrayList<>();
        for (int line = startLine; line <= endLine; line++) {
            int lineStartOffset = doc.getLineStartOffset(line);
            int lineEndOffset = doc.getLineEndOffset(line);
            String lineText = doc.getText(new TextRange(lineStartOffset, lineEndOffset));
            selectedLinesList.add(lineText);
        }
        return selectedLinesList;
    }
}
