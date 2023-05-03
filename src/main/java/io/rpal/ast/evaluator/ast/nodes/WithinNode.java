package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WithinNode extends InnerNode implements Standardizable {
    public Node left;
    public Node right;

    public WithinNode() {
        super("within");
    }


    @Override
    public void setChild(Node child) {
        if (left == null) this.left = child;
        else if (right == null) this.right = child;
        else throw new IllegalStateException("Cannot assign more children to : " + this.name);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(Arrays.asList(left, right));
    }

    @Override
    public Node standardize() {
        if (left instanceof Standardizable) this.left = ((Standardizable) left).standardize();
        if (right instanceof Standardizable) this.right = ((Standardizable) right).standardize();

        if (!(left instanceof EqualNode) || !(right instanceof EqualNode)) {
            throw new IllegalStateException("AST cannot be standardized");
        }

        LambdaNode newRightLeft = new LambdaNode();
        newRightLeft.left = ((EqualNode) left).left;
        newRightLeft.right = ((EqualNode) right).right;

        GammaNode newRight = new GammaNode();
        newRight.left = newRightLeft;
        newRight.right = ((EqualNode) left).right;

        EqualNode newNode = new EqualNode();
        newNode.left = ((EqualNode) right).left;
        newNode.right = newRight;

        return newNode;
    }

    @Override
    public void removeChildren() {
        left = null;
        right = null;
    }
}
