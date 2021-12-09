package catserver.server.utils;

import java.util.regex.Pattern;

public class Log4j2_3201_Fixer {
    private final static Pattern REGEX = Pattern.compile("\\$\\{jndi:.*}");

    public static boolean match(String message) {
        return REGEX.matcher(message).find();
    }

    public static void matchThrowException(String message) throws RuntimeException {
        if (match(message)) {
            throw new RuntimeException("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024"));
        }
    }

    public static boolean matchPrintException(String message) throws RuntimeException {
        if (match(message)) {
            new RuntimeException("Detected log4j2 3201 bug! Message: " + message.replace("$", "\\u0024")).printStackTrace();
            return true;
        }
        return false;
    }
}
