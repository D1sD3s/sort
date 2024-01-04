package com.github.d1sd3s.sort;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.TestDataPath;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@TestDataPath("$CONTENT_ROOT/src/test/testData")
public class TestSortAscendingAction extends BasePlatformTestCase {


    public void testSortAscending() {
        try {
            String testDataFilePath = getTestDataPath() + "ascending.txt";
            PsiFile psiFile = copyFileToProject(testDataFilePath, "ascending.txt");
            Assert.assertNotNull(psiFile);
            // open file in editor
            myFixture.configureFromExistingVirtualFile(psiFile.getVirtualFile());


            setSelection(0, 5);
            myFixture.performEditorAction("sort.SortAscending");
            Assert.assertEquals("bar\nfoo\nhello, world!\nline 1\nline 2\nline 3",
                    myFixture.getEditor().getDocument().getText());
            ApplicationManager.getApplication().runWriteAction(psiFile::delete);
        } catch (IOException e) {
            Assert.fail("Failed to copy test file: " + e.getMessage());
        }
    }

    public void testSortDescending() {
        try {
            String testDataFilePath = getTestDataPath() + "descending.txt";
            PsiFile psiFile = copyFileToProject(testDataFilePath, "descending.txt");
            Assert.assertNotNull(psiFile);
            // open file in editor
            myFixture.configureFromExistingVirtualFile(psiFile.getVirtualFile());

            setSelection(0, 5);
            myFixture.performEditorAction("sort.SortDescending");

            Assert.assertEquals("line 3\nline 2\nline 1\nhello, world!\nfoo\nbar",
                    myFixture.getEditor().getDocument().getText());
            ApplicationManager.getApplication().runWriteAction(psiFile::delete);
        } catch (IOException e) {
            Assert.fail("Failed to copy test file: " + e.getMessage());
        }
    }

    private void setSelection(int startLine, int endLine) {
        Document document = myFixture.getEditor().getDocument();
        int startOffset = document.getLineStartOffset(startLine);
        int endOffset = document.getLineEndOffset(endLine);
        myFixture.getEditor().getSelectionModel().setSelection(startOffset, endOffset);
    }

    private PsiFile copyFileToProject(@NotNull String sourceFilePath, @NotNull String destinationFileName) throws IOException {
        Project project = getProject();

        File tempDir = new File(project.getBasePath(), "temp");
        tempDir.mkdirs();
        File tempFile = new File(tempDir, destinationFileName);
        Files.copy(new File(sourceFilePath).toPath(), tempFile.toPath());

        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(tempFile);

        PsiFile psiFile = getPsiManager().findFile(Objects.requireNonNull(LocalFileSystem.getInstance().refreshAndFindFileByIoFile(tempFile)));
        Assert.assertNotNull(psiFile);
        return psiFile;
    }


    @Override
    public String getTestDataPath() {
        return "src/test/testData/sort/";
    }
}
