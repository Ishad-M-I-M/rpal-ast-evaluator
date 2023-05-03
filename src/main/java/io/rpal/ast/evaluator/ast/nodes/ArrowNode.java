package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrowNode extends InnerNode {
    public Node expression;
    public Node trueNode;
    public Node falseNode;

    public ArrowNode() {
        super("->");
    }


    @Override
    public void setChild(Node child) {
        if (expression == null) this.expression = child;
        else if (trueNode == null) this.trueNode = child;
        else if (falseNode == null) this.falseNode = child;
        else throw new IllegalStateException("Cannot assign more children to : " + this.name);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<Node>(Arrays.asList(expression, trueNode, falseNode));
    }

    @Override
    public void removeChildren() {
        expression = null;
        trueNode = null;
        falseNode = null;
    }
}
