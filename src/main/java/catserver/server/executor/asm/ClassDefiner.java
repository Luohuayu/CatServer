package catserver.server.executor.asm;

public interface ClassDefiner {

    /**
     * Returns if the defined classes can bypass access checks
     *
     * @return if classes bypass access checks
     */
    public default boolean isBypassAccessChecks() {
        return false;
    }

    /**
     * Define a class
     *
     * @param parentLoader the parent classloader
     * @param name         the name of the class
     * @param data         the class data to load
     * @return the defined class
     * @throws ClassFormatError     if the class data is invalid
     * @throws NullPointerException if any of the arguments are null
     */
    public Class<?> defineClass(ClassLoader parentLoader, String name, byte[] data);

    public static ClassDefiner getInstance() {
        return SafeClassDefiner.INSTANCE;
    }

}
