package io.rpal.ast.evaluator.csemachine;

import io.rpal.ast.evaluator.TestUtils;
import io.rpal.ast.evaluator.ast.ASTParser;
import io.rpal.ast.evaluator.ast.ASTTree;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class STEvaluationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeTest
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterTest
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test(description = "Test for standard tree evaluation",
            dataProvider = "standardTreeEvaluationTestDataProvider",
            groups = {"csemachine"})
    public void testStandardTreeEvaluation(String filename) throws Exception {
        ASTTree astTree = ASTParser.parse(TestUtils.getASTFile(filename));
        astTree.standardize();
        Machine machine = new Machine(astTree);
        machine.evaluate();
        String actual = outContent.toString().trim();
        outContent.reset();
        String expected = TestUtils.getOutputContent(filename);
        Assert.assertEquals(actual, expected);
    }

    @DataProvider(name = "standardTreeEvaluationTestDataProvider")
    public Object[] dataProvider() {
        return new Object[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "9-simplified", "10", "11", "12"};
    }
}
