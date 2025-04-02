import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ExpressionEvaluator {
    private final String mathRegex;
    private static final HashMap<String, Integer> tokenMap = new HashMap<>();
    private String result;

    static{
        tokenMap.put("+", 1);
        tokenMap.put("-", 1);
        tokenMap.put("*", 2);
        tokenMap.put("/", 2);
        tokenMap.put("^", 3);
        tokenMap.put("sin", 4);
        tokenMap.put("cos", 4);
        tokenMap.put("tan", 4);
        tokenMap.put("sqrt", 4);
        tokenMap.put("log", 4);
        tokenMap.put("abs", 4);
        tokenMap.put("log10", 4);
        tokenMap.put("exp", 4);
        tokenMap.put("(", 5);
        tokenMap.put(")", 0);
    }

    public ExpressionEvaluator(String mathExpression){
        mathRegex = mathExpression;
        result = "";
    }

    public void convertToPostfix(){
        Stack<String> stackRegex = new Stack<>();
        Queue<String> queueRegex = new LinkedList<>();
        String regex = "-?\\d+(\\.\\d+)?|[\\[\\](*/^\\-\\+)]|log10|sqrt|sin|cos|tan|log|abs|exp";


        Pattern patronCoincidencias = Pattern.compile(regex);
        Matcher coincidenceString = patronCoincidencias.matcher(mathRegex);
        String lastToken = "";
        while(coincidenceString.find()){
            String token = coincidenceString.group();
            if(!isToken(token)){
                if(lastToken.equals(")")){
                    addOperators(stackRegex, queueRegex, "*");
                }
                queueRegex.add(token);
            }else if(tokenMap.get(token) >= 4){
                if(lastToken.equals(")") || (!isToken(lastToken) && !lastToken.equals(""))){
                    addOperators(stackRegex, queueRegex, "*");
                }
                stackRegex.push(token);
            }else if(token.equals(")")){
                while(!stackRegex.isEmpty() && !stackRegex.peek().equals("(")){
                    queueRegex.add(stackRegex.pop());
                }
                if(!stackRegex.isEmpty()){
                    stackRegex.pop();
                }
            }else{
                addOperators(stackRegex, queueRegex, token);
            }
            lastToken = token;
        }
        while(!stackRegex.isEmpty()){
            if(!stackRegex.peek().equals("(")){
                queueRegex.add(stackRegex.pop());
            }
        }
        result = freeQueue(queueRegex);
    }

    public static String freeQueue (Queue<String> queueRegex){
        Stack<String> stackResult = new Stack<>();
        while(!queueRegex.isEmpty()){
            String token = queueRegex.poll();
            if(!isToken(token)){
                stackResult.add(token);
            }else{
                stackResult.add(resultString(token, stackResult));
            }
        }
        return stackResult.pop();
    }

    public static boolean isToken(String token){
        return tokenMap.containsKey(token);
    }
    
    public static String resultString(String token, Stack<String> stackResult){
        double number1 = Double.parseDouble(stackResult.pop()); 
        double number2 = 0; 
        boolean isBinary = false;
        if(stackResult.size() > 0 && valueToken(token) < 4){
            number2 = Double.parseDouble(stackResult.pop());
            isBinary = true;
        }
        switch (token) {
            case "+":
                return String.valueOf(number2 + number1); 
            case "-":
                if(isBinary){
                    return String.valueOf(number2 - number1); 
                }else{
                    return String.valueOf(-number1); 
                }
            case "*":
                if(isBinary){
                    return String.valueOf(number2 * number1);
                }else{
                    return "SYNTAX ERROR";
                }
            case "/":
                if(isBinary && number1 != 0){
                    return String.valueOf(number2 / number1); 
                }else{
                    return "SYNTAX ERROR";
                }
            case "^":
                if(isBinary){
                    return String.valueOf(Math.pow(number2, number1)); 
                }else{
                    return "SYNTAX ERROR";
                }
            case "sin":
                return String.valueOf(Math.sin(number1));
            case "cos":
                return String.valueOf(Math.cos((number1)));
            case "tan":
                return String.valueOf(Math.tan((number1)));
            case "sqrt":
                return String.valueOf(Math.sqrt(number1));
            case "log":
                return String.valueOf(Math.log(number1));
            case "abs":
                return String.valueOf(Math.abs(number1));
            case "log10":
                return String.valueOf(Math.log10(number1));
            case "exp":
                return String.valueOf(Math.exp(number1));

        }
        return "Error";
    }
    
    public static int valueToken(String token){
        return tokenMap.get(token);
    }
    public static void addOperators(Stack<String> stackRegex, Queue<String> queueRegex, String token){
        while(!stackRegex.isEmpty() && valueToken(stackRegex.peek()) > valueToken(token) && !stackRegex.peek().equals("(")) {
            queueRegex.add(stackRegex.pop());
        }
        stackRegex.push(token);
    }
    public String getResult() {
        return result;
    }
    
    public static void main(String[] args) {
        String[] test = {
            "2 + 3 * (4 + sqrt(16))",                    
            "log10(1000 + 10 * sqrt(9))",                
            "abs(cos(0) - exp(0))",                      
            "log(10 + abs(-10))",                       
            "sin(3.1416 / 2) + cos(0) + tan(0)",       
            "(abs(-2)^2 + sqrt(16)) / log10(100)",
            "2^3 + 4^2 + (abs(-3))^2"
        };
    
        for (int i = 0; i < test.length; i++) {
            String expression = test[i];
            ExpressionEvaluator operation = new ExpressionEvaluator(expression);
            operation.convertToPostfix();
            System.out.println("Result " + (i + 1) + ": " + expression + " = " + operation.getResult());
        }
    }
    
}