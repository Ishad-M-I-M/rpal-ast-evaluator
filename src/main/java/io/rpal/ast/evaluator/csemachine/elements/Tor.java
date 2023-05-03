package io.rpal.ast.evaluator.csemachine.elements;

import java.util.ArrayList;

public class Tor extends Element {
    public int count;
    public ArrayList<Element> children;

    public Tor(int count) {
        this.count = count;
        children = new ArrayList<>();
    }
}
