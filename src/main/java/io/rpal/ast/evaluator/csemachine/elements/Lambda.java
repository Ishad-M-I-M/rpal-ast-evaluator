package io.rpal.ast.evaluator.csemachine.elements;

public class Lambda extends Element {
    public Integer environmentTag;
    public int controlTag;
    public Element boundedVariable;

    public Lambda(int controlTag, Element boundedVariable) {
        this.environmentTag = null;
        this.controlTag = controlTag;
        if (boundedVariable instanceof Variable || boundedVariable instanceof Comma)
            this.boundedVariable = boundedVariable;
        else throw new IllegalArgumentException("Unsupported variable :" + boundedVariable.toString());
    }
}
