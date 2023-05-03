package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.List;

public class AndNode extends InnerNode implements Standardizable {

    List<Node> children = new ArrayList<>();

    public AndNode() {
        super("and");
    }

    @Override
    public void setChild(Node child) {
        if (child instanceof EqualNode) children.add(child);
        else throw new IllegalArgumentException("Children of an AndNode should be EqualNodes");
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public Node standardize() {
        CommaNode newLeft = new CommaNode();
        TauNode newRight = new TauNode();
        for (Node child :
                children) {
            newLeft.setChild(((EqualNode) child).left);
            newRight.setChild(((EqualNode) child).right);
        }

        EqualNode newNode = new EqualNode();
        newNode.left = newLeft;
        newNode.right = newRight;

        return newNode;
    }

    @Override
    public void removeChildren() {
        children = new ArrayList<>();
    }
}
