package io.rpal.ast.evaluator.ast;

import io.rpal.ast.evaluator.ast.nodes.InnerNode;
import io.rpal.ast.evaluator.ast.nodes.LeafNode;
import io.rpal.ast.evaluator.ast.nodes.Node;
import io.rpal.ast.evaluator.ast.nodes.Standardizable;

import java.util.ArrayList;
import java.util.List;

public class ASTTree {
    public Node root;

    public ASTTree(Node root) {
        this.root = root;
    }

    /**
     * Print the tree by inorder traversing
     */
    public void traverse() {
        if (root instanceof LeafNode) {
            System.out.println(root.name);
        } else {
            System.out.println(root.name);
            for (Node child :
                    ((InnerNode) root).getChildren()) {
                traverse(child, ".");
            }
        }

    }

    private void traverse(Node root, String prefix) {
        if (root instanceof LeafNode) {
            System.out.println(prefix + root.name);
        } else {
            System.out.println(prefix + root.name);
            for (Node child :
                    ((InnerNode) root).getChildren()) {
                traverse(child, prefix + ".");
            }
        }
    }

    /**
     * Standardize the tree
     */
    public Node standardize() {
        standardizeChildren(root);
        if (root instanceof Standardizable) this.root = ((Standardizable) root).standardize();
        return root;
    }

    private Node standardize(Node node) {
        standardizeChildren(node);
        if (node instanceof Standardizable) return ((Standardizable) node).standardize();
        else return node;
    }

    private void standardizeChildren(Node root) {
        if (root instanceof InnerNode) {
            List<Node> nodes = new ArrayList<>();
            for (Node child :
                    ((InnerNode) root).getChildren()) {
                nodes.add(standardize(child));
            }

            ((InnerNode) root).removeChildren();
            for (Node newNode :
                    nodes) {
                ((InnerNode) root).setChild(newNode);
            }

        }
    }

}
