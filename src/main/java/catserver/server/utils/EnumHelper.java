package catserver.server.utils;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumHelper {
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
        return EnumJ17Helper.addEnum(enumType, enumName, paramTypes, paramValues);
    }

    static class EnumJ17Helper {
        private static MethodHandles.Lookup implLookup = null;
        private static boolean isSetup = false;
        private static Unsafe unsafe;

        private static void setup() {
            if (isSetup) {
                return;
            }

            try {
                /*
                 * After Java 9, sun.reflect package was moved to jdk.internal.reflect and it requires extra operations to access.
                 * After Java 12, all members in java.lang.reflect.Field class were added to jdk.internal.reflect.Reflection#fieldFilterMap so that it was unable to access by using reflection.
                 * So the most reasonable way is to use java.lang.invoke.MethodHandles$Lookup#IMPL_LOOKUP to access each member after Java 8.
                 * See: https://stackoverflow.com/questions/61141836/change-static-final-field-in-java-12
                 */
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                Unsafe unsafe = (Unsafe) unsafeField.get(null);
                Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                implLookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(implLookupField), unsafe.staticFieldOffset(implLookupField));
            } catch (Exception e) {
            }

            isSetup = true;
        }

        /*
         * Everything below this is found at the site below, and updated to be able to compile in Eclipse/Java 1.6+
         * Also modified for use in decompiled code.
         * Found at: http://niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
         */
        private static MethodHandle getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
            Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
            parameterTypes[0] = String.class;
            parameterTypes[1] = int.class;
            System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
            return implLookup.findConstructor(enumClass, MethodType.methodType(void.class, parameterTypes));
        }

        private static <T extends Enum<?>> T makeEnum(Class<T> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Throwable {
            int additionalParamsCount = additionalValues == null ? 0 : additionalValues.length;
            Object[] params = new Object[additionalParamsCount + 2];
            params[0] = value;
            params[1] = ordinal;
            if (additionalValues != null) {
                System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
            }
            return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).invokeWithArguments(params));
        }

        public static void setFailsafeFieldValue(Field field, Object target, Object value) throws Throwable {
            if (target != null) {
                implLookup.findSetter(field.getDeclaringClass(), field.getName(), field.getType()).invoke(target, value);
            } else {
                implLookup.findStaticSetter(field.getDeclaringClass(), field.getName(), field.getType()).invoke(value);
            }
        }

        private static void blankField(Class<?> enumClass, String fieldName) throws Throwable {
            for (Field field : Class.class.getDeclaredFields()) {
                if (field.getName().contains(fieldName)) {
                    setFailsafeFieldValue(field, enumClass, null);
                    break;
                }
            }
        }

        private static void cleanEnumCache(Class<?> enumClass) throws Throwable {
            blankField(enumClass, "enumConstantDirectory");
            blankField(enumClass, "enumConstants");
            blankField(enumClass, "enumVars");
        }

        public static <T extends Enum<?>> T addEnum0(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
            return addEnum(enumType, enumName, paramTypes, paramValues);
        }

        @SuppressWarnings({"unchecked", "serial"})
        public static <T extends Enum<?>> T addEnum(final Class<T> enumType, String enumName, final Class<?>[] paramTypes, Object[] paramValues) {
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

            int flags = (Modifier.PUBLIC) | Modifier.STATIC | Modifier.FINAL | 0x1000 /*SYNTHETIC*/;
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
                final List<String> lines = new ArrayList<>();
                lines.add(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
                //lines.add(String.format("Runtime Deobf: %s", FMLForgePlugin.RUNTIME_DEOBF));
                lines.add(String.format("Flags: %s", String.format("%16s", Integer.toBinaryString(flags)).replace(' ', '0')));
                lines.add("Fields:");
                for (Field field : fields) {
                    String mods = String.format("%16s", Integer.toBinaryString(field.getModifiers())).replace(' ', '0');
                    lines.add(String.format("       %s %s: %s", mods, field.getName(), field.getType().getName()));
                }

                return null;
            }

            valuesField.setAccessible(true);

            try {
                T[] previousValues = (T[]) valuesField.get(enumType);
                List<T> values = new ArrayList<T>(Arrays.asList(previousValues));
                T newValue = (T) makeEnum(enumType, enumName, values.size(), paramTypes, paramValues);
                values.add(newValue);
                setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));
                cleanEnumCache(enumType);

                return newValue;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new RuntimeException(throwable.getMessage(), throwable);
            }
        }

        static {
            if (!isSetup) {
                setup();
            }
        }

        public static void setField(Object obj, Object value, Field field) throws ReflectiveOperationException {
            if (obj == null) {
                setStaticField(field, value);
            } else {
                try {
                    unsafe.putObject(obj, unsafe.objectFieldOffset(field), value);
                } catch (Exception e) {
                    throw new ReflectiveOperationException(e);
                }
            }
        }

        public static void setStaticField(Field field, Object value) throws ReflectiveOperationException {
            try {
                implLookup.ensureInitialized(field.getDeclaringClass());
                unsafe.putObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field), value);
            } catch (Exception e) {
                throw new ReflectiveOperationException(e);
            }
        }

        public static <T> T getField(Object obj, Field field) throws ReflectiveOperationException {
            if (obj == null) {
                return getStaticField(field);
            } else {
                try {
                    return (T) unsafe.getObject(obj, unsafe.objectFieldOffset(field));
                } catch (Exception e) {
                    throw new ReflectiveOperationException(e);
                }
            }
        }

        public static <T> T getStaticField(Field field) throws ReflectiveOperationException {
            try {
                implLookup.ensureInitialized(field.getDeclaringClass());
                return (T) unsafe.getObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field));
            } catch (Exception e) {
                throw new ReflectiveOperationException(e);
            }
        }
    }
}