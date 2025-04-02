# Expression Evaluator
I created this expression evaluator a year ago during a free hour as a personal project to develop an Android calculator with a friend @Mauricinio7, he create a nice GUI. However, like many fun projects, it eventually faded away. I’m planning to modify it and incorporate best practices with the new techniques I’ve learned since then. I’m not a computer science student, I’m a software engineering student. So my focus isn’t on algorithms and data structures; we concentrate more on the software development process and design patterns.

I decided to shared this project because I think it can be useful for students like me who are learning about expression evaluation and parsing. Or they just want to create a calculator that can evaluate mathematical expressions and not only accept numbers as input.

## How to test
I add a main function with an string array

---

```java
String[] test = {
            "2 + 3 * (4 + sqrt(16))",                    
            "log10(1000 + 10 * sqrt(9))",                
            "abs(cos(0) - exp(0))",                      
            "log(10 + abs(-10))",                       
            "sin(3.1416 / 2) + cos(0) + tan(0)",       
            "(abs(-2)^2 + sqrt(16)) / log10(100)",
            "2^3 + 4^2 + (abs(-3))^2"
        };
```

You can change the expressions to test the evaluator. 

---

As you can see, the constructor receive an expression.

```java
public ExpressionEvaluator(String mathExpression){
    mathRegex = mathExpression;
    result = "";
}

```

---

At that moment I had a homework about to create a postfix expression evaluator and I deliver this method to my teacher. Well this is a better solution than the one I delivered. But I think it can be improved. You can change this method to return a **String** instead of **void**, and return the result. I will do it in the future, but for now I am a bit busy with my classes and I don't want to change the structure.

```java
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
```
---
This method just return the result string
``` java
    public String getResult() {
        return result;
    }
```
--- 

If you want to add a feature, you can add here the new function

``` java
String regex = "-?\\d+(\\.\\d+)?|[\\[\\](*/^\\-\\+)]|log10|sqrt|sin|cos|tan|log|abs|exp";
```

And add here the new case
``` java
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
    
```
---

Hope this helps! Thank you for reading.
