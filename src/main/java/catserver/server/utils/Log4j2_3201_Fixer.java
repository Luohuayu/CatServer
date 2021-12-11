package catserver.server.utils;

import java.util.regex.Pattern;

public class Log4j2_3201_Fixer {
    private final static Pattern REGEX = Pattern.compile("(?i)\\$\\{(jndi|ctx|date|env|event|java|jvmrunargs|log4j|lower|main|map|marker|bundle|sd|sys|upper|):[\\s\\S]*}");

    public static boolean match(String message) {
        if (message != null) {
            return REGEX.matcher(message.replaceAll("\u00a7[a-zA-Z0-9]", "").replaceAll("(\\s|\\n\\r)", "")).find();
        } else {
            return false;
        }
    }

    public static void matchThrowException(String message) throws RuntimeException {
        if (match(message)) {
            throw new RuntimeException("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024"));
        }
    }

    public static boolean matchPrintException(String message) {
        if (match(message)) {
            new RuntimeException("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024")).printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean matchPrintMessage(String message) {
        if (match(message)) {
            System.out.println("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024"));
            return true;
        }
        return false;
    }
}
