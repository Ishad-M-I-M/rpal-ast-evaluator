package io.rpal.ast.evaluator.csemachine.elements;


public class Primitive extends Element {
    public Object value;

    public Primitive(Object value) {
        this.value = value;
    }
}
