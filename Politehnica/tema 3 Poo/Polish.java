

import java.util.Stack;

public class Polish {

    /**
     * 
     * @param s
     * @return true daca s este operatie
     */
    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    /**
     * Priority
     * @param s oparatorul
     * @return Priority
     */
    private static int precedence(String s) {
        switch (s) {
            case "+":
            case "-":
                return 0;
            case "*":
            case "/":
                return 1;
        }
        return -1;
    }

    /**
     * 
     * @param left
     * @param right
     * @param operator
     * @return 
     */
    private static int evaluate(int left, int right, String operator) {
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                return left / right;
        }
        return -1;
    }

    /**
     * Transforma expresia din infix in prefix 
     * @param infix expresia
     * @return prefix expresie
     */
    public static String infixToPrefix(String[] infix) {
        Stack<String> stack = new Stack<>();
        Stack<String> queue = new Stack<>();

        for (int i = infix.length - 1; i >= 0; i--) {
            if (infix[i].matches("\\w+")) {
                queue.push(infix[i]);
            } else if (isOperator(infix[i])) {
                while (!stack.isEmpty()
                        && precedence(infix[i]) < precedence(stack
                        .peek())
                        && precedence(infix[i]) <= precedence(stack
                        .peek())) {
                    queue.push(stack.pop());
                }
                stack.push(infix[i]);
            } else if (infix[i].equals(")")) {
                stack.push(infix[i]);
            } else if (infix[i].equals("(")) {
                while (!stack.peek().equals(")")) {
                    queue.push(stack.pop());
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            queue.push(stack.pop());
        }

        String prefix = "";
        while (!queue.isEmpty()) {
            prefix += queue.pop() + " ";
        }
        return prefix.trim();
    }

}
