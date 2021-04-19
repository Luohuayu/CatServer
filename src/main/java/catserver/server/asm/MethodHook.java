package catserver.server.asm;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MethodHook {
    public static void TypeAdapters$EnumTypeAdapter_Init(Class<?> classOfT, Map<String, Enum<?>> nameToConstant, Map<Enum<?>, String> constantToName) {
        for (Enum<?> constant : (Enum<?>[])classOfT.getEnumConstants()) {
            String name = constant.name();
            try {
                SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                if (annotation != null) {
                    name = annotation.value();
                    for (String alternate : annotation.alternate()) {
                        nameToConstant.put(alternate, constant);
                    }
                }
            } catch (NoSuchFieldException ignored) {}
            nameToConstant.put(name, constant);
            constantToName.put(constant, name);
        }
    }
}
