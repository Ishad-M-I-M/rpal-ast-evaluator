package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LambdaNode extends InnerNode {
    public Node left;
    public Node right;

    public LambdaNode() {
        super("lambda");
    }

    @Override
    public void setChild(Node child) {
        if (left == null) this.left = child;
        else if (right == null) this.right = child;
        else throw new IllegalStateException("Cannot assign more children to : " + this.name);
    }

    public List<Node> getChildren() {
        return new ArrayList<Node>(Arrays.asList(left, right));
    }

    @Override
    public void removeChildren() {
        left = null;
        right = null;
    }

}
