package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.List;

public class TauNode extends InnerNode {
    public List<Node> children = new ArrayList<>();

    public TauNode() {
        super("tau");
    }

    @Override
    public void setChild(Node child) {
        children.add(child);
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public void removeChildren() {
        children = new ArrayList<>();
    }
}
