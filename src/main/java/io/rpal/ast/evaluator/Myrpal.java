package io.rpal.ast.evaluator;

import io.rpal.ast.evaluator.ast.ASTParser;
import io.rpal.ast.evaluator.ast.ASTTree;
import io.rpal.ast.evaluator.csemachine.Machine;

import java.io.FileNotFoundException;

public class Myrpal {
    public static void main(String[] args) {
        try {
            ASTTree astTree = ASTParser.parse(args[0]);
//            astTree.traverse();
            astTree.standardize();
//            System.out.println("\nStandardized Tree\n");
//            astTree.traverse();

            // CSE Machine
//            System.out.println("\n CSE Machine control structures \n");
            Machine machine = new Machine(astTree);
//            System.out.println(machine.getControlStructures());

            // evaluating
            System.out.println("Output of the program:");
            machine.evaluate();
        } catch (FileNotFoundException | IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}