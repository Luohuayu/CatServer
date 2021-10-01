package catserver.server.launch;

import com.google.common.collect.Lists;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.fml.common.EnhancedRuntimeException;
import org.apache.commons.lang.ArrayUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class Java11Support {
    public static boolean enable = false;
    public static sun.misc.Unsafe unsafe;

    public static void setup() {
        enable = true;
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class FieldHelper {
        public static void set(Object obj, Field field, Object value) throws ReflectiveOperationException {
            if (obj == null) {
                setStatic(field, value);
            } else {
                try {
                    unsafe.putObject(obj, unsafe.objectFieldOffset(field), value);
                } catch (Exception e) {
                    throw new ReflectiveOperationException(e);
                }
            }
        }

        public static void setStatic(Field field, Object value) throws ReflectiveOperationException {
            try {
                unsafe.ensureClassInitialized(field.getDeclaringClass());
                unsafe.putObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field), value);
            } catch (Exception e) {
                throw new ReflectiveOperationException(e);
            }
        }

        public static <T> T get(Object obj, Field field) throws ReflectiveOperationException {
            if (obj == null) {
                return getStatic(field);
            } else {
                try {
                    return (T)unsafe.getObject(obj, unsafe.objectFieldOffset(field));
                } catch (Exception e) {
                    throw new ReflectiveOperationException(e);
                }
            }
        }

        public static <T> T getStatic(Field field) throws ReflectiveOperationException {
            try {
                unsafe.ensureClassInitialized(field.getDeclaringClass());
                return (T)unsafe.getObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field));
            } catch (Exception e) {
                throw new ReflectiveOperationException(e);
            }
        }
    }

    public static class EnumHelper {
        private static MethodHandles.Lookup implLookup = null;
        private static boolean isSetup = false;

        private static void setup() {
            if (isSetup) {
                return;
            }

            try {
                Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                implLookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(implLookupField), unsafe.staticFieldOffset(implLookupField));
            } catch (Exception e) {
                e.printStackTrace();
            }

            isSetup = true;
        }

        private static MethodHandle getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
            Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
            parameterTypes[0] = String.class;
            parameterTypes[1] = int.class;
            System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
            return implLookup.findConstructor(enumClass, MethodType.methodType(void.class, parameterTypes));
        }

        private static <T extends Enum<?>> T makeEnum(Class<T> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Exception {
            int additionalParamsCount = additionalValues == null ? 0 : additionalValues.length;
            Object[] params = new Object[additionalParamsCount + 2];
            params[0] = value;
            params[1] = ordinal;
            if (additionalValues != null) {
                System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
            }
            try {
                return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).invokeWithArguments(params));
            } catch (Throwable throwable) {
                throw new Exception(throwable);
            }
        }

        public static void setFailsafeFieldValue(Field field, Object target, Object value) throws Exception {
            long offset;
            if (target != null) {
                offset = unsafe.objectFieldOffset(field);
                unsafe.getAndSetObject(target, offset, value);
            } else {
                offset = unsafe.staticFieldOffset(field);
                unsafe.getAndSetObject(field.getDeclaringClass(), offset, value);
            }
        }

        private static void blankField(Class<?> enumClass, String fieldName) throws Exception {
            for (Field field : Class.class.getDeclaredFields()) {
                if (field.getName().contains(fieldName)) {
                    setFailsafeFieldValue(field, enumClass, null);
                    break;
                }
            }
        }

        private static void cleanEnumCache(Class<?> enumClass) throws Exception {
            blankField(enumClass, "enumConstantDirectory");
            blankField(enumClass, "enumConstants");
            //Open J9
            blankField(enumClass, "enumVars");
        }

        @SuppressWarnings({"unchecked", "serial"})
        public static <T extends Enum<?>> T addEnum(boolean test, final Class<T> enumType, String enumName, final Class<?>[] paramTypes, Object[] paramValues) {
            if (!isSetup) {
                setup();
            }

            Field valuesField = null;
            Field[] fields = enumType.getDeclaredFields();

            for (Field field : fields) {
                String name = field.getName();
                if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) //Added 'ENUM$VALUES' because Eclipse's internal compiler doesn't follow standards
                {
                    valuesField = field;
                    break;
                }
            }

            int flags = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL | 0x1000 /*SYNTHETIC*/;
            if (valuesField == null) {
                String valueType = String.format("[L%s;", enumType.getName().replace('.', '/'));

                for (Field field : fields) {
                    if ((field.getModifiers() & flags) == flags &&
                            field.getType().getName().replace('.', '/').equals(valueType)) //Apparently some JVMs return .'s and some don't..
                    {
                        valuesField = field;
                        break;
                    }
                }
            }

            if (valuesField == null) {
                if (test) {
                    final List<String> lines = Lists.newArrayList();
                    lines.add(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
                    lines.add(String.format("Runtime Deobf: %s", FMLForgePlugin.RUNTIME_DEOBF));
                    lines.add(String.format("Flags: %s", String.format("%16s", Integer.toBinaryString(flags)).replace(' ', '0')));
                    lines.add(              "Fields:");
                    for (Field field : fields) {
                        String mods = String.format("%16s", Integer.toBinaryString(field.getModifiers())).replace(' ', '0');
                        lines.add(String.format("       %s %s: %s", mods, field.getName(), field.getType().getName()));
                    }

                    throw new EnhancedRuntimeException("Could not find $VALUES field for enum: " + enumType.getName()) {
                        @Override
                        protected void printStackTrace(WrappedPrintStream stream) {
                            for (String line : lines)
                                stream.println(line);
                        }
                    };
                }
                return null;
            }

            if (test) {
                Object ctr = null;
                Exception ex = null;
                try {
                    ctr = getConstructorAccessor(enumType, paramTypes);
                } catch (Exception e) {
                    ex = e;
                }
                if (ctr == null || ex != null) {
                    throw new EnhancedRuntimeException(String.format("Could not find constructor for Enum %s", enumType.getName()), ex) {
                        private String toString(Class<?>[] cls) {
                            StringBuilder b = new StringBuilder();
                            for (int x = 0; x < cls.length; x++) {
                                b.append(cls[x].getName());
                                if (x != cls.length - 1)
                                    b.append(", ");
                            }
                            return b.toString();
                        }

                        @Override
                        protected void printStackTrace(WrappedPrintStream stream) {
                            stream.println("Target Arguments:");
                            stream.println("    java.lang.String, int, " + toString(paramTypes));
                            stream.println("Found Constructors:");
                            for (Constructor<?> ctr : enumType.getDeclaredConstructors()) {
                                stream.println("    " + toString(ctr.getParameterTypes()));
                            }
                        }
                    };
                }
                return null;
            }

            valuesField.setAccessible(true);

            try {
                T[] previousValues = (T[]) valuesField.get(enumType);
                T newValue = makeEnum(enumType, enumName, previousValues.length, paramTypes, paramValues);
                setFailsafeFieldValue(valuesField, null, ArrayUtils.add(previousValues, newValue));
                cleanEnumCache(enumType);

                return newValue;
            } catch (Throwable t) {
                t.printStackTrace();
                throw new RuntimeException(t);
            }
        }

        static {
            if (!isSetup) {
                setup();
            }
        }
    }
}
