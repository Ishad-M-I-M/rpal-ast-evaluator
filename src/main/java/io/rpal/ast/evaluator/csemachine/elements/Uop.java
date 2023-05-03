package io.rpal.ast.evaluator.csemachine.elements;

public class Uop extends Element {
    private final String operation;

    public Uop(String operation) {
        this.operation = operation;
    }

    public Object apply(Object operand) {
        if (operand instanceof Integer) {
            if (operation.equals("neg")) return -(int) operand;
            else throw new IllegalArgumentException("Unsupported operand");
        } else if (operand instanceof Boolean) {
            if (operation.equals("not")) return !(boolean) operand;
            else throw new IllegalArgumentException("Unsupported operand");
        } else throw new IllegalArgumentException("Unsupported operand");
    }
}
