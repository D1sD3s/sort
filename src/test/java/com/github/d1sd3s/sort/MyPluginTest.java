package com.github.d1sd3s.sort;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.TestDataPath;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@TestDataPath("$CONTENT_ROOT/src/test/testData")
public class MyPluginTest extends BasePlatformTestCase {
    public void testSortAscending() {
        try {
            String testDataFilePath = getTestDataPath() + "ascending.txt";
            PsiFile psiFile = copyFileToProject(testDataFilePath, "ascending.txt");
            assertNotNull(psiFile);

            // Open the file in the editor
            myFixture.configureFromExistingVirtualFile(psiFile.getVirtualFile());

            // Select lines 1 to 6 in the editor
            int startLine = 0;   // 0-based line number
            int endLine = 5;     // 0-based line number

            Document document = myFixture.getEditor().getDocument();
            int startOffset = document.getLineStartOffset(startLine);
            int endOffset = document.getLineEndOffset(endLine);
            myFixture.getEditor().getSelectionModel().setSelection(startOffset, endOffset);

            // Invoke the "SortAscending" action
            myFixture.performEditorAction("sort.SortAscending");

            // Example assertion: Check if the content matches after sorting
            assertEquals("bar\nfoo\nhello, world!\nline 1\nline 2\nline 3", myFixture.getEditor().getDocument().getText());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to copy test file: " + e.getMessage());
        }
    }

    private PsiFile copyFileToProject(@NotNull String sourceFilePath, @NotNull String destinationFileName) throws IOException {
        Project project = getProject();
        String destinationFilePath = project.getBasePath() + File.separator + destinationFileName;

        // Copy the content of the source file to the project
        Files.copy(new File(sourceFilePath).toPath(), new File(destinationFilePath).toPath());

        // Refresh the project to make sure the new file is recognized
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(destinationFilePath);
        assertNotNull(virtualFile);

        // Load the virtual file as a PsiFile
        return getPsiManager().findFile(virtualFile);
    }

    @Override
    public String getTestDataPath() {
        return "src/test/testData/sort/";
    }
}
