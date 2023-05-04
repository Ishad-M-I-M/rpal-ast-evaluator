package io.rpal.ast.evaluator.ast;

import io.rpal.ast.evaluator.ast.nodes.InnerNode;
import io.rpal.ast.evaluator.ast.nodes.Node;
import io.rpal.ast.evaluator.ast.nodes.NodeFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class ASTParser {

    public static ASTTree parse(File astFile) throws FileNotFoundException, IllegalArgumentException {
        Scanner astScan = new Scanner(astFile);
        Iterator<String> lines = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return astScan.hasNextLine();
            }

            @Override
            public String next() {
                return astScan.nextLine();
            }
        };
        return parseTree(lines);
    }

    private static ASTTree parseTree(Iterator<String> lines){
        Node parentNode = NodeFactory.createNode(lines.next().trim());
        int parentLevel = 0;  // to keep track of the level
        ASTTree astTree = new ASTTree(parentNode);

        while (lines.hasNext()) {
            String entry = lines.next().trim();
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
