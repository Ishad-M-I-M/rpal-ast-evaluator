package io.rpal.ast.evaluator.csemachine;

import io.rpal.ast.evaluator.csemachine.elements.Element;
import io.rpal.ast.evaluator.csemachine.elements.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to maintain the environment of the stack
 * Stores data in a Tree structure
 */
public class MachineEnvironment {
    private Node current;

    protected MachineEnvironment(Environment environment) {
        current = new Node(environment, null);
    }

    /**
     * Add new node to the environment tree and move to it
     */
    public void addNewEnvironment(Environment environment) {
        current.addChild(environment);
        this.current = new Node(environment, current);
    }

    /**
     * Move to parent environment
     */
    protected void moveToParent() {
        this.current = current.getParent();
    }


    /**
     * @return resolved variable value
     */
    public Element findValue(String variableName) {
        Element found = null;
        Node searchNode = current;
        while (found == null) {
            found = searchNode.environment.getValue(variableName);
            if (found != null) return found;

            if (searchNode.getParent() == null) {
                break;
            }

            for (Environment e :
                    searchNode.getParent().getChildren()) {
                found = e.getValue(variableName);
                if (found != null) return found;
            }

            searchNode = searchNode.getParent();
        }
        return found;
    }

    protected int getEnvironmentTag() {
        return current.environment.tag;
    }

    /**
     * Inner class to represent Nodes in the tree
     */
    class Node {
        private final Node parent;
        private final Environment environment;
        private final List<Environment> children;

        Node(Environment environment, Node parent) {
            this.environment = environment;
            this.parent = parent;
            this.children = new ArrayList<>();
        }

        Node getParent() {
            return this.parent;
        }

        private void addChild(Environment environment) {
            children.add(environment);
        }

        List<Environment> getChildren() {
            return this.children;
        }
    }
}
