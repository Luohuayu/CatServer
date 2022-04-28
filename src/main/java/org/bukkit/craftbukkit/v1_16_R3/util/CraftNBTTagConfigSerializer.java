package org.bukkit.craftbukkit.v1_16_R3.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public class CraftNBTTagConfigSerializer {

    private static final Pattern ARRAY = Pattern.compile("^\\[.*]");
    private static final Pattern INTEGER = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)i", Pattern.CASE_INSENSITIVE); // Paper - fix regex
    private static final Pattern DOUBLE = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
    private static final JsonToNBT MOJANGSON_PARSER = new JsonToNBT(new StringReader(""));

    public static Object serialize(INBT base) {
        if (base instanceof CompoundNBT) {
            Map<String, Object> innerMap = new HashMap<>();
            for (String key : ((CompoundNBT) base).getAllKeys()) {
                innerMap.put(key, serialize(((CompoundNBT) base).get(key)));
            }

            return innerMap;
        } else if (base instanceof ListNBT) {
            List<Object> baseList = new ArrayList<>();
            for (int i = 0; i < ((CollectionNBT) base).size(); i++) {
                baseList.add(serialize((INBT) ((CollectionNBT) base).get(i)));
            }

            return baseList;
        } else if (base instanceof StringNBT) {
            return base.getAsString();
        } else if (base instanceof IntNBT) { // No need to check for doubles, those are covered by the double itself
            return base.toString() + "i";
        }

        return base.toString();
    }

    public static INBT deserialize(Object object) {
        if (object instanceof Map) {
            CompoundNBT compound = new CompoundNBT();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                compound.put(entry.getKey(), deserialize(entry.getValue()));
            }

            return compound;
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            if (list.isEmpty()) {
                return new ListNBT(); // Default
            }

            ListNBT tagList = new ListNBT();
            for (Object tag : list) {
                tagList.add(deserialize(tag));
            }

            return tagList;
        } else if (object instanceof String) {
            String string = (String) object;

            if (ARRAY.matcher(string).matches()) {
                try {
                    return new JsonToNBT(new StringReader(string)).readArrayTag();
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException("Could not deserialize found list ", e);
                }
            } else if (INTEGER.matcher(string).matches()) { //Read integers on our own
                return IntNBT.valueOf(Integer.parseInt(string.substring(0, string.length() - 1)));
            } else if (DOUBLE.matcher(string).matches()) {
                return DoubleNBT.valueOf(Double.parseDouble(string.substring(0, string.length() - 1)));
            } else {
                INBT nbtBase = MOJANGSON_PARSER.type(string);

                if (nbtBase instanceof IntNBT) { // If this returns an integer, it did not use our method from above
                    return StringNBT.valueOf(nbtBase.getAsString()); // It then is a string that was falsely read as an int
                } else if (nbtBase instanceof DoubleNBT) {
                    return StringNBT.valueOf(String.valueOf(((DoubleNBT) nbtBase).getAsDouble())); // Doubles add "d" at the end
                } else {
                    return nbtBase;
                }
            }
        }

        throw new RuntimeException("Could not deserialize INBT");
    }
}
