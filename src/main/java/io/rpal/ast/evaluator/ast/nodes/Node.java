package io.rpal.ast.evaluator.ast.nodes;

public abstract class Node {
    public String name;
    public Node parent;

    public Node(String name) {
        this.name = name;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(InnerNode parent) {
        parent.setChild(this);
        this.parent = parent;
    }
}
