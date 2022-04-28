package moe.loliserver.utils;

import org.apache.commons.lang.ArrayUtils;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumHelper {
    private static final boolean isJDK8 = (Float.parseFloat(System.getProperty("java.class.version")) == 52.0);

    @Nullable
    public static <T extends Enum<? >> T addEnum0(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues)
    {
        return addEnum(enumType, enumName, paramTypes, paramValues);
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues) {
        return isJDK8 ? EnumHelperImpl$JDK8.addEnum(enumType, enumName, paramTypes, paramValues) : EnumHelperImpl$JDK9.addEnum(enumType, enumName, paramTypes, paramValues);
    }

    static class EnumHelperImpl$JDK8 {
        private static Object reflectionFactory = null;
        private static Method newConstructorAccessor = null;
        private static Method newInstance = null;
        private static Method newFieldAccessor = null;
        private static Method fieldAccessorSet = null;
        private static boolean isSetup = false;

        private static void setup() {
            if (isSetup) {
                return;
            }

            try {
                Method getReflectionFactory = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("getReflectionFactory");
                reflectionFactory = getReflectionFactory.invoke(null);
                newConstructorAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newConstructorAccessor", Constructor.class);
                newInstance = Class.forName("sun.reflect.ConstructorAccessor").getDeclaredMethod("newInstance", Object[].class);
                newFieldAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newFieldAccessor", Field.class, boolean.class);
                fieldAccessorSet = Class.forName("sun.reflect.FieldAccessor").getDeclaredMethod("set", Object.class, Object.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            isSetup = true;
        }

        /*
         * Everything below this is found at the site below, and updated to be able to compile in Eclipse/Java 1.6+
         * Also modified for use in decompiled code.
         * Found at: http://niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
         */
        private static Object getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
            Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
            parameterTypes[0] = String.class;
            parameterTypes[1] = int.class;
            System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
            return newConstructorAccessor.invoke(reflectionFactory, enumClass.getDeclaredConstructor(parameterTypes));
        }

        private static <T extends Enum<?>> T makeEnum(Class<T> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Exception {
            Object[] parms = new Object[additionalValues.length + 2];
            parms[0] = value;
            parms[1] = Integer.valueOf(ordinal);
            System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
            return enumClass.cast(newInstance.invoke(getConstructorAccessor(enumClass, additionalTypes), new Object[]{parms}));
        }

        public static void setFailsafeFieldValue(Field field, Object target, Object value) throws Exception {
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, field, false);
            fieldAccessorSet.invoke(fieldAccessor, target, value);
        }

        private static void blankField(Class<?> enumClass, String fieldName) throws Exception {
            for (Field field : Class.class.getDeclaredFields()) {
                if (field.getName().contains(fieldName)) {
                    field.setAccessible(true);
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

        @Nullable
        public static <T extends Enum<? >> T addEnum0(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues)
        {
            return addEnum(enumType, enumName, paramTypes, paramValues);
        }

        @Nullable
        public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues) {
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
                throw new RuntimeException(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
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

    static class EnumHelperImpl$JDK9 {
        private static MethodHandles.Lookup implLookup = null;
        private static boolean isSetup = false;
        private static sun.misc.Unsafe unsafe = null;

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
                unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                implLookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(implLookupField), unsafe.staticFieldOffset(implLookupField));
            } catch (Exception e) {
                e.printStackTrace();
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
            long offset;
            if (target != null) {
                offset = unsafe.objectFieldOffset(field);
                unsafe.getAndSetObject(target, offset, value);
            } else {
                offset = unsafe.staticFieldOffset(field);
                unsafe.getAndSetObject(field.getDeclaringClass(), offset, value);
            }
        }

        private static void blankField(Class<?> enumClass, String fieldName) throws Throwable {
            for (Field field : Class.class.getDeclaredFields()) {
                if (field.getName().contains(fieldName)) {
                    field.setAccessible(true);
                    setFailsafeFieldValue(field, enumClass, null);
                    break;
                }
            }
        }

        private static void cleanEnumCache(Class<?> enumClass) throws Throwable {
            blankField(enumClass, "enumConstantDirectory");
            blankField(enumClass, "enumConstants");
            //Open J9
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
                throw new RuntimeException(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
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
