package luohuayu.CatServer.remapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import org.objectweb.asm.Type;

public class RemapUtils {
    // Classes
    public static String reverseMapExternal(Class<?> name) {
        return reverseMap(name).replace('$', '.').replace('/', '.');
    }

    public static String reverseMap(Class<?> name) {
        return reverseMap(Type.getInternalName(name));
    }

    public static String reverseMap(String check) {
        for (Entry<String, String> entry : Transformer.jarMapping.classes.entrySet()) {
            if (entry.getValue().equals(check))
            {
                
                return entry.getKey();}
        }
        return check;
    }

    // Methods
    public static String mapMethod(Class<?> inst, String name, Class<?>... parameterTypes) {
        String result = mapMethodInternal(inst, name, parameterTypes);
        if (result != null) {
            return result;
        }
        return name;
    }

    /**
     * Recursive method for finding a method from superclasses/interfaces
     */
    public static String mapMethodInternal(Class<?> inst, String name, Class<?>... parameterTypes) {
        String match = reverseMap(inst) + "/" + name + " ";

        for (Entry<String, String> entry : Transformer.jarMapping.methods.entrySet()) {
            if (entry.getKey().startsWith(match)) {
                // Check type to see if it matches
                String[] str = entry.getKey().split("\\s+");
                int i = 0;
                for (Type type : Type.getArgumentTypes(str[1])) {
                    if (i >= parameterTypes.length || !type.getClassName().equals(reverseMapExternal(parameterTypes[i]))) {
                        i=-1;
                        break;
                    }
                    i++;
                }

                if (i >= parameterTypes.length)
                    return entry.getValue();
            }
        }

        // Search interfaces
        ArrayList<Class<?>> parents = new ArrayList<Class<?>>();
        parents.add(inst.getSuperclass());
        parents.addAll(Arrays.asList(inst.getInterfaces()));

        for (Class<?> superClass : parents) {
            if (superClass == null) continue;
            mapMethodInternal(superClass, name, parameterTypes);
        }

        return null;
    }
}
