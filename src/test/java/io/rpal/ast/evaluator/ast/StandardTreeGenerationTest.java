package io.rpal.ast.evaluator.ast;

import io.rpal.ast.evaluator.TestUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StandardTreeGenerationTest {
    @Test(description = "Test for standard tree formation from AST",
            dataProvider = "standardTreeGenerationTestDataProvider",
            groups = {"ast"})
    public void testStandardTreeGeneration(String filename) throws Exception {
        ASTTree astTree = ASTParser.parse(TestUtils.getASTFile(filename));
        astTree.standardize();
        String stTree = astTree.traverse().trim();
        String expected = TestUtils.getSTContent(filename);
        Assert.assertEquals(stTree, expected);
    }

    @DataProvider(name = "standardTreeGenerationTestDataProvider")
    public Object[] dataProvider() {
        return new Object[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "9-simplified", "10", "11", "12", "13"};
    }
}
