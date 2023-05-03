package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecNode extends InnerNode implements Standardizable {
    public Node child;

    public RecNode() {
        super("rec");
    }

    @Override
    public void setChild(Node child) {
        if (this.child == null) this.child = child;
        else throw new IllegalStateException("Cannot assign more children to : " + this.name);
    }

    public List<Node> getChildren() {
        return new ArrayList<Node>(Collections.singletonList(child));
    }

    @Override
    public Node standardize() {
        if (child instanceof Standardizable) this.child = ((Standardizable) child).standardize();

        if (child instanceof EqualNode) {
            LambdaNode newRightRight = new LambdaNode();
            newRightRight.left = ((EqualNode) child).left;
            newRightRight.right = ((EqualNode) child).right;

            GammaNode newRight = new GammaNode();
            newRight.left = new YNode();
            newRight.right = newRightRight;

            EqualNode newNode = new EqualNode();
            newNode.left = ((EqualNode) child).left;
            newNode.right = newRight;

            return newNode;
        } else throw new IllegalStateException("AST cannot be standardized");
    }

    @Override
    public void removeChildren() {
        child = null;
    }
}
