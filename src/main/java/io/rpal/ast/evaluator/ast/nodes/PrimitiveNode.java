package io.rpal.ast.evaluator.ast.nodes;

public class PrimitiveNode extends LeafNode {
    public Object value;

    public PrimitiveNode(String type, String value) {
        super(value);
        switch (type) {
            case "STR" -> this.value = value;
            case "INT" -> this.value = Integer.parseInt(value);
            default -> throw new IllegalArgumentException("type doesn't belong to any of [INT, STR]");
        }

    }
}
