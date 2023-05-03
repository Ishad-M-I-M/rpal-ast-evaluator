package io.rpal.ast.evaluator.ast.nodes;

import java.util.ArrayList;
import java.util.List;

public class FcnFormNode extends InnerNode implements Standardizable {
    public IDNode functionName;
    public List<Node> variables = new ArrayList<>();
    public Node expression;

    public FcnFormNode() {
        super("function_form");
    }


    @Override
    public void setChild(Node child) {
        if (functionName == null) this.functionName = (IDNode) child;
        else if (expression == null && (child instanceof LeafNode || child instanceof CommaNode)) variables.add(child);
        else if (expression == null) this.expression = child;
        else throw new IllegalStateException("Cannot assign more children to : " + this.name);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.add(functionName);
        children.addAll(variables);
        children.add(expression);
        return children;
    }

    @Override
    public Node standardize() {
        Node newRight = expression;
        while (!variables.isEmpty()) {
            LambdaNode lambdaNode = new LambdaNode();
            lambdaNode.left = variables.remove(variables.size() - 1);
            lambdaNode.right = newRight;
            newRight = lambdaNode;
        }

        EqualNode newNode = new EqualNode();
        newNode.left = functionName;
        newNode.right = newRight;

        return newNode;
    }

    @Override
    public void removeChildren() {
        this.functionName = null;
        this.variables = new ArrayList<>();
        this.expression = null;
    }
}
