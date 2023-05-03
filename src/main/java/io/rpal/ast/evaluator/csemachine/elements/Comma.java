package io.rpal.ast.evaluator.csemachine.elements;

import java.util.List;

/**
 * Class to support multi-bounded variables in Lambdas
 */
public class Comma extends Element {
    public List<Variable> children;

    public Comma(List<Variable> children) {
        this.children = children;
    }
}
