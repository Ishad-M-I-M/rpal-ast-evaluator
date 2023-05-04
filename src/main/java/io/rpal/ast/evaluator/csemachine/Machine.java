package io.rpal.ast.evaluator.csemachine;

import io.rpal.ast.evaluator.ast.ASTTree;
import io.rpal.ast.evaluator.ast.nodes.*;
import io.rpal.ast.evaluator.csemachine.elements.*;

import java.util.*;

/**
 * Class to represent the CSE Machine
 */
public class Machine {
    private final ASTTree astTree;
    private final MachineControl control;
    private final MachineStack stack;
    private MachineEnvironment environment;

    public static final ArrayList<String> definedFunctions = new ArrayList<>(Arrays.asList("Print", "Conc", "Stern", "Stem", "Order", "Isstring"));

    /**
     * Input need to be a Standardized tree
     */
    public Machine(ASTTree astTree) {
        this.astTree = astTree;
        control = new MachineControl();
        stack = new MachineStack();
    }

    /**
     *
     */
    public void evaluate() {
        Map<Integer, Queue<Element>> controlStructures = controlStructureArrayToMap(getControlStructures());

        int envTag = 0;
        Environment initialEnv = new Environment(envTag++);
        this.environment = new MachineEnvironment(initialEnv);
        stack.push(initialEnv, environment);
        control.push(initialEnv);

        pushToControl(controlStructures.get(0));

        while (!control.isEmpty()) {
            Element element = control.pop();

            // Applying CSE rules accordingly

            // ********************** CSE Rule 1 **********************
            if (element instanceof Variable || element instanceof Primitive || element instanceof Y) {
                stack.push(element, environment);
            }

            // ********************** CSE Rule 2 **********************
            else if (element instanceof Lambda) {
                stack.push(element, environment);
            } else if (element instanceof Gamma) {
                Element operator = stack.pop();

                // ********************** CSE Rule 3 **********************
                if (operator instanceof Variable) {
                    // handling defined functions
                    String functionName = ((Variable) operator).name;
                    if (definedFunctions.contains(functionName)) functionCall(functionName);
                } else if (operator instanceof Lambda) {

                    // ********************** CSE Rule 4 **********************
                    if (((Lambda) operator).boundedVariable instanceof Variable) {
                        Element operand = stack.pop();

                        // Creating new Environment and adding the assignment
                        Environment newEnv = new Environment(envTag++);
                        newEnv.addAssignment(((Variable) ((Lambda) operator).boundedVariable).name, operand);
                        control.push(newEnv);
                        stack.push(newEnv, environment);
                        environment.addNewEnvironment(newEnv);

                        // pushing the control structure tagged by lambda
                        pushToControl(controlStructures.get(((Lambda) operator).controlTag));
                    }

                    // ********************** CSE Rule 11 **********************
                    else if (((Lambda) operator).boundedVariable instanceof Comma) {

                        // Creating new Environment and adding the assignments
                        Environment newEnv = new Environment(envTag++);
                        Tor tor = (Tor) stack.pop();
                        for (Variable v :
                                ((Comma) ((Lambda) operator).boundedVariable).children) {
                            Element operand = tor.children.remove(0);
                            newEnv.addAssignment(v.name, operand);
                        }
                        control.push(newEnv);
                        stack.push(newEnv, environment);
                        environment.addNewEnvironment(newEnv);


                        // pushing the control structure tagged by lambda
                        pushToControl(controlStructures.get(((Lambda) operator).controlTag));

                    } else throw new IllegalStateException("Lambda is bounded with unexpected Element");

                }

                // ********************** CSE Rule 10 **********************
                else if (operator instanceof Tor) {
                    // Take the next element and get the index needed.
                    // Push the variable in the given index
                    Primitive primitive = (Primitive) stack.pop();
                    int index = (int) primitive.value;
                    stack.push(((Tor) operator).children.get(index), environment);
                }

                // ********************** CSE Rule 12 **********************
                else if (operator instanceof Y) {
                    Lambda lambda = (Lambda) stack.pop();
                    Eta eta = new Eta(lambda.controlTag, lambda.environmentTag, lambda.boundedVariable);
                    stack.push(eta, environment);
                }

                // ********************** CSE Rule 13 **********************
                else if (operator instanceof Eta) {
                    control.push(new Gamma());
                    control.push(new Gamma());

                    stack.push(operator, environment);
                    Lambda lambda = new Lambda(((Eta) operator).controlTag, ((Eta) operator).boundedVariable);
                    lambda.environmentTag = ((Eta) operator).environmentTag;
                    stack.push(lambda, environment);
                } else {
                    throw new IllegalArgumentException("Unsupported operator " + operator.toString());
                }
            }


            // ********************** CSE Rule 5 **********************
            else if (element instanceof Environment) {
                Element value = stack.pop();

                // popping out the corresponding  env element
                Element environment1 = stack.pop();
                if (!(environment1 instanceof Environment))
                    throw new IllegalStateException("Unexpected element found while applying CSE Rule 5 :" + environment1.toString());

                stack.push(value, environment);
                environment.moveToParent();
            }

            // ********************** CSE Rule 6 **********************
            else if (element instanceof Bop) {
                Element op1 = stack.pop();
                Element op2 = stack.pop();

                Object operand1, operand2;
                if (op1 instanceof Variable) operand1 = ((Variable) op1).value;
                else if (op1 instanceof Primitive) operand1 = ((Primitive) op1).value;
                else if (op1 instanceof Tor) operand1 = op1;
                else throw new IllegalArgumentException("Unsupported operand");

                if (op1 instanceof Tor || op1 instanceof Primitive && ((Primitive) op1).value == null) operand2 = op2;
                else if (op2 instanceof Variable) operand2 = ((Variable) op2).value;
                else if (op2 instanceof Primitive) operand2 = ((Primitive) op2).value;
                else throw new IllegalArgumentException("Unsupported operand");

                Object result = ((Bop) element).apply(operand1, operand2);
                if (result instanceof Tor) stack.push((Element) result, environment);
                else stack.push(new Primitive(result), environment);
            }

            // ********************** CSE Rule 7 **********************
            else if (element instanceof Uop) {
                Element op1 = stack.pop();

                Object operand1;
                if (op1 instanceof Variable) operand1 = ((Variable) op1).value;
                else if (op1 instanceof Primitive) operand1 = ((Primitive) op1).value;
                else throw new IllegalArgumentException("Unsupported operand");

                Element result = new Primitive(((Uop) element).apply(operand1));
                stack.push(result, environment);
            }

            // ********************** CSE Rule 8 **********************
            else if (element instanceof Beta) {
                Delta falseDelta = (Delta) control.pop();
                Delta trueDelta = (Delta) control.pop();

                boolean result = (boolean) ((Primitive) stack.pop()).value;
                if (result) pushToControl(controlStructures.get(trueDelta.tag));
                else pushToControl(controlStructures.get(falseDelta.tag));
            }

            // ********************** CSE Rule 9 **********************
            else if (element instanceof Tor) {
                for (int i = 0; i < ((Tor) element).count; i++) {
                    ((Tor) element).children.add(stack.pop());
                }
                stack.push(element, environment);
            }
            else throw new IllegalArgumentException("Unsupported element " + element.toString());


        }

    }

    /**
     * @return flatten control structure as list of Deltas
     */
    public List<Delta> getControlStructures() {
        List<Delta> controlStructures = new ArrayList<>();
        Queue<Node> toBeParsed = new ArrayDeque<>();
        toBeParsed.add(astTree.root);

        int tag = 0;
        int lambdaTag = 1;
        while (!toBeParsed.isEmpty()) {
            Node curr = toBeParsed.poll();
            MachineControl controlStructure = new MachineControl();
            lambdaTag = flatten(lambdaTag, curr, controlStructure, toBeParsed);

            if (curr instanceof LambdaNode) lambdaTag++;

            controlStructures.add(new Delta(tag++, controlStructure));
        }

        return controlStructures;
    }

    private Map<Integer, Queue<Element>> controlStructureArrayToMap(List<Delta> array) {
        Map<Integer, Queue<Element>> map = new HashMap<>();
        for (Delta d :
                array) {
            ArrayDeque<Element> elements = new ArrayDeque<>();
            while (!d.control.isEmpty()) {
                elements.addFirst(d.control.pop());
            }
            map.put(d.tag, elements);
        }
        return map;
    }

    /**
     * push control structure to the control
     */
    private void pushToControl(Queue<Element> elements) {
        Queue<Element> curr = new ArrayDeque<>();
        curr.addAll(elements);
        while (!curr.isEmpty()) {
            control.push(curr.poll());
        }
    }


    /**
     * Traverse in depth first method
     * - Create a control structure for a Delta node with given tag
     * - If a Lambda node found add it to the `toBeParsed` queue
     * - Add the delta node to the `controlStructures` array
     *
     * @return tag after incrementing
     */
    private int flatten(int tag, Node curr, MachineControl control, Queue<Node> toBeParsed) {
        if (curr instanceof LeafNode) {
            control.push(getElement(curr, tag));
        } else {

            if (curr instanceof LambdaNode) {
                control.push(getElement(curr, tag));
                toBeParsed.add(((LambdaNode) curr).right);
            } else if (curr instanceof ArrowNode) {
                toBeParsed.add(((ArrowNode) curr).trueNode);
                control.push(new Delta(tag, null));
                toBeParsed.add(((ArrowNode) curr).falseNode);
                control.push(new Delta(tag + 1, null));
                control.push(getElement(curr, tag));
                tag = flatten(tag + 2, ((ArrowNode) curr).expression, control, toBeParsed);
            } else {
                control.push(getElement(curr, tag));
                for (Node child :
                        ((InnerNode) curr).getChildren()) {
                    tag = flatten(tag, child, control, toBeParsed);
                    if (child instanceof LambdaNode) tag++;
                }
            }
        }
        return tag;
    }

    /**
     * @return corresponding element for the Node
     */
    private Element getElement(Node node, int tag) throws IllegalArgumentException {
        if (node instanceof GammaNode) return new Gamma();
        else if (node instanceof LambdaNode) return new Lambda(tag, getVariable(((LambdaNode) node).left, tag));
        else if (node instanceof ArrowNode) return new Beta();
        else if (node instanceof UopNode) return new Uop(node.name);
        else if (node instanceof BopNode) return new Bop(node.name);
        else if (node instanceof TauNode) return new Tor(((TauNode) node).getChildren().size());
        else if (node instanceof IDNode) return new Variable(node.name);
        else if (node instanceof PrimitiveNode) return new Primitive(((PrimitiveNode) node).value);
        else if (node instanceof YNode) return new Y();
        else throw new IllegalArgumentException("Unexpected value: " + node.name);
    }

    /**
     * @return bounded variable for lambda
     */
    private Element getVariable(Node node, int tag) {
        if (node instanceof CommaNode) {
            List<Variable> children = new ArrayList<>();
            for (Node child :
                    ((CommaNode) node).getChildren()) {
                children.add((Variable) getElement(child, tag));
            }
            return new Comma(children);
        } else return new Variable(node.name);
    }

    /**
     * Apply defined Functions
     */
    private void functionCall(String functionName) {
        if (functionName.equals("Print")) {
            Element element = stack.pop();
            if (element instanceof Primitive) {
                System.out.println(((Primitive) element).value);
                stack.push(element, environment);
            } else if (element instanceof Tor) {
                List<String> values = ((Tor) element).children.stream().map(element1 -> ((Primitive) element1).value.toString()).toList();
                System.out.println("(" + String.join(", ", values) + ")");
                stack.push(element, environment);
            } else throw new IllegalArgumentException("Operation is not applicable");
        } else if (functionName.equals("Conc")) {
            // need two gammas to apply the operation
            Element e = control.pop();
            if (!(e instanceof Gamma))
                throw new IllegalStateException("`Conc` operation is not applicable in current state");

            Primitive primitive1 = (Primitive) stack.pop();
            Primitive primitive2 = (Primitive) stack.pop();
            String res = primitive1.value + (String) primitive2.value;
            stack.push(new Primitive(res), environment);
        } else if (functionName.equals("Stem")) {
            Primitive primitive1 = (Primitive) stack.pop();
            String res = String.valueOf(((String) primitive1.value).charAt(0));
            stack.push(new Primitive(res), environment);
        } else if (functionName.equals("Stern")) {
            Primitive primitive1 = (Primitive) stack.pop();
            String res = ((String) primitive1.value).substring(1);
            stack.push(new Primitive(res), environment);
        } else if (functionName.equals("Order")) {
            Element element = stack.pop();
            if (element instanceof Tor) {
                int order = ((Tor) element).children.size();
                stack.push(new Primitive(order), environment);
            } else throw new IllegalArgumentException("Operation is not applicable");
        } else if (functionName.equals("Isstring")) {
            Primitive primitive = (Primitive) stack.pop();
            if (primitive.value instanceof String) {
                stack.push(new Primitive(true), environment);
            } else {
                stack.push(new Primitive(false), environment);
            }

        } else {
            throw new IllegalArgumentException("Not a valid functions");
        }
    }
}
