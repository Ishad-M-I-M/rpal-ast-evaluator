package io.rpal.ast.evaluator.csemachine.elements;

public class Variable extends Element {
    public String name;
    public Object value;

    public Variable(String name) {
        this.name = name;
    }
}
