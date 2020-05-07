package catserver.server.remapper;

public class ArrayHandle {
    public String arrayStart = "";
    public String arrayEnd = "";
    public String originClassName = "";

    public ArrayHandle(String className) {
        boolean start = true;
        for (char c : className.toCharArray()) {
            if (start) {
                if (c == '[') {
                    arrayStart += '[';
                } else if (c == 'L') {
                    arrayStart += 'L';
                    start = false;
                }
            } else {
                if (c == ';') {
                    if (!arrayEnd.isEmpty()) throw new IllegalArgumentException();
                    arrayEnd += ";";
                } else {
                    if (!arrayEnd.isEmpty()) throw new IllegalArgumentException();
                    originClassName += c;
                }
            }
        }

        if (arrayStart.isEmpty()) throw new IllegalArgumentException();
        if (arrayEnd.isEmpty()) throw new IllegalArgumentException();
        if (originClassName.isEmpty()) throw new IllegalArgumentException();
    }

    public static boolean isArray(String className) {
        return className.startsWith("[L") && className.endsWith(";");
    }
}
