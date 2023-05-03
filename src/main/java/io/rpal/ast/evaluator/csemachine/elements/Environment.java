package io.rpal.ast.evaluator.csemachine.elements;

import java.util.HashMap;
import java.util.Map;

public class Environment extends Element {
    public int tag;
    private final Map<String, Element> assignments;

    public Environment(int tag) {
        this.tag = tag;
        assignments = new HashMap<>();
    }

    /**
     * @return assignment value for the variable
     * If not found return null
     */
    public Element getValue(String variableName) {
        return assignments.getOrDefault(variableName, null);
    }

    /**
     * Add assignments to the environment
     */
    public void addAssignment(String name, Element value) {
        assignments.put(name, value);
    }
}
