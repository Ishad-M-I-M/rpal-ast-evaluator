package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfixNode extends InnerNode implements Standardizable {

    public Node E1;
    public Node N;
    public Node E2;

    public InfixNode() {
        super("@");
    }

    @Override
    public void setChild(Node child) {
        if (E1 == null) this.E1 = child;
        else if (N == null) this.N = child;
        else if (E2 == null) this.E2 = child;
        else throw new IllegalStateException("Cannot assign more children to : " + this.name);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(Arrays.asList(E1, N, E2));
    }

    @Override
    public Node standardize() {
        GammaNode newLeft = new GammaNode();
        newLeft.left = N;
        newLeft.right = E1;

        GammaNode newNode = new GammaNode();
        newNode.left = newLeft;
        newNode.right = E2;

        return newNode;
    }

    @Override
    public void removeChildren() {
        E1 = null;
        N = null;
        E2 = null;
    }
}
