package io.rpal.ast.evaluator.csemachine.elements;

public class Eta extends Element {
    public int environmentTag;
    public int controlTag;
    public Element boundedVariable;

    public Eta(int controlTag, int environmentTag, Element boundedVariable) {
        this.controlTag = controlTag;
        this.environmentTag = environmentTag;
        if (boundedVariable instanceof Variable || boundedVariable instanceof Comma)
            this.boundedVariable = boundedVariable;
        else throw new IllegalArgumentException("Unsupported variable :" + boundedVariable.toString());
    }

}
