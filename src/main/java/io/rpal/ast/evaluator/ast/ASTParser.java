package io.rpal.ast.evaluator.ast;

import io.rpal.ast.evaluator.ast.nodes.InnerNode;
import io.rpal.ast.evaluator.ast.nodes.Node;
import io.rpal.ast.evaluator.ast.nodes.NodeFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ASTParser {
    public static ASTTree parse(String filename) throws FileNotFoundException, IllegalArgumentException {
        File astFile = new File(filename);
        Scanner astScan = new Scanner(astFile);

        Node parentNode = NodeFactory.createNode(astScan.nextLine().trim());
        int parentLevel = 0;  // to keep track of the level
        ASTTree astTree = new ASTTree(parentNode);

        while (astScan.hasNextLine()) {
            String entry = astScan.nextLine().trim();
            String type = entry.replace(".", "");
            String dots = entry.replace(type, "");

            int currLevel = dots.length();
            Node currNode = NodeFactory.createNode(type);

            while (currLevel <= parentLevel) {
                parentNode = parentNode.getParent();
                parentLevel = parentLevel - 1;
            }

            currNode.setParent((InnerNode) parentNode);
            parentLevel = currLevel;
            parentNode = currNode;
        }
        return astTree;
    }
}
