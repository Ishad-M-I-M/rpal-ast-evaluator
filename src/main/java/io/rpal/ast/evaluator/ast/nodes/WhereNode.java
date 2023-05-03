package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhereNode extends InnerNode implements Standardizable {
    public Node left;
    public Node right;

    public WhereNode() {
        super("where");
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
        if (right instanceof Standardizable) this.right = ((Standardizable) right).standardize();

        if (right instanceof EqualNode) {
            LambdaNode newLeft = new LambdaNode();
            newLeft.left = ((EqualNode) right).left;
            newLeft.right = left;

            GammaNode newNode = new GammaNode();
            newNode.left = newLeft;
            newNode.right = ((EqualNode) right).right;

            return newNode;
        } else throw new IllegalStateException("AST cannot be standardized");
    }

    @Override
    public void removeChildren() {
        left = null;
        right = null;
    }
}
