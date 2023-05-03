package io.rpal.ast.evaluator.ast.nodes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeFactory {
    /**
     * Class to create the Nodes.
     */
    private NodeFactory() {
    }

    private static final Set<String> AcceptedBops = new HashSet<String>(Arrays.asList(
            "+", "-", "*", "/", "**",                                   //arithmetic operators
            "or", "&", "gr", "ge", "ls", "le", "eq", "ne",             // boolean operators
            "aug"                                                       // tuple operation
    ));

    private static final Set<String> AcceptedUops = new HashSet<String>(Arrays.asList(
            "neg",
            "not"
    ));

    public static Node createNode(String type) throws IllegalArgumentException {
        Pattern idPattern = Pattern.compile("<ID:([a-zA-Z][a-zA-Z0-9_]*)>");
        Pattern primitivePattern = Pattern.compile("<([A-Z]*):(.+)>");

        if (idPattern.matcher(type).matches()) {
            Matcher matcher = idPattern.matcher(type);
            matcher.find();
            String name = matcher.group(1);
            return new IDNode(name);
        } else if (primitivePattern.matcher(type).matches()) {
            Matcher matcher = primitivePattern.matcher(type);
            matcher.find();
            String primitive = matcher.group(1);
            String value = matcher.group(2);
            return new PrimitiveNode(primitive, value.replace("'", ""));
        }


        if (AcceptedUops.contains(type)) return new UopNode(type);
        else if (AcceptedBops.contains(type)) return new BopNode(type);
        else {
            return switch (type) {
                case "let" -> new LetNode();
                case "where" -> new WhereNode();
                case "lambda" -> new LambdaNode();
                case "->" -> new ArrowNode();
                case "function_form" -> new FcnFormNode();
                case "gamma" -> new GammaNode();
                case "tau" -> new TauNode();
                case "@" -> new InfixNode();
                case "rec" -> new RecNode();
                case "<Y*>" -> new YNode();
                case "=" -> new EqualNode();
                case "," -> new CommaNode();
                case "within" -> new WithinNode();
                case "and" -> new AndNode();
                case "<nil>" -> new PrimitiveNode("STR", null);
                default -> throw new IllegalArgumentException("Not a valid argument : " + type);
            };
        }
    }
}
