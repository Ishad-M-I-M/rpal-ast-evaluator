package io.rpal.ast.evaluator.ast.nodes;

/**
 * Interface to indicate a node need to standardize *  only upto able to apply CSE Machine rules. (All 13 ).
 */
public interface Standardizable {
    /**
     * @return standardize version of the node.
     * Not necessarily standardize upto depth of that node.
     */
    Node standardize();
}
