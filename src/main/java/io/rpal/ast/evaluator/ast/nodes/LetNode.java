package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LetNode extends InnerNode implements Standardizable {
    public Node left;
    public Node right;

    public LetNode() {
        super("let");
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
    public Node standardize() {
        if (left instanceof Standardizable) this.left = ((Standardizable) left).standardize();

        if (left instanceof EqualNode) {
            LambdaNode newLeft = new LambdaNode();
            newLeft.left = ((EqualNode) left).left;
            newLeft.right = right;
            GammaNode newNode = new GammaNode();
            newNode.right = ((EqualNode) left).right;
            newNode.left = newLeft;
            return newNode;
        } else throw new IllegalStateException("AST cannot be standardized");
    }

    @Override
    public void removeChildren() {
        left = null;
        right = null;
    }
}
