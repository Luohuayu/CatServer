package catserver.server.utils;

import catserver.server.CatServer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.bukkit.craftbukkit.util.UnsafeList;

public class UnsafeListEntityImpl extends UnsafeList<Entity> {
    private final static Field valuesField;
    private static ClassInheritanceMultiMap<Entity>[] cachedClassInheritanceMultiMap;
    private static UnsafeListEntityImpl[] cachedUnsafeListEntityImpl;

    static {
        try {
            valuesField = ClassInheritanceMultiMap.class.getDeclaredField(FMLLaunchHandler.isDeobfuscatedEnvironment() ? "values" : "field_181745_e");
            valuesField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("The exception shouldn't happen!", e);
        }
    }

    public static UnsafeListEntityImpl[] getUnsafeListEntityImpl(ClassInheritanceMultiMap<Entity>[] entityLists) {
        if (cachedClassInheritanceMultiMap == entityLists) {
            return cachedUnsafeListEntityImpl;
        } else {
            UnsafeListEntityImpl[] unsafeListImpl = new UnsafeListEntityImpl[entityLists.length];
            for (int i = 0; i < entityLists.length; i++) {
                unsafeListImpl[i] = new UnsafeListEntityImpl(entityLists[i]);
            }
            cachedClassInheritanceMultiMap = entityLists;
            cachedUnsafeListEntityImpl = unsafeListImpl;
            return unsafeListImpl;
        }
    }

    private final List<Entity> entityList;

    public UnsafeListEntityImpl(ClassInheritanceMultiMap<Entity> entityList) {
        super(1, 1);
        try {
            this.entityList = (List<Entity>) valuesField.get(entityList);
        } catch (Exception e) {
            throw new RuntimeException("The exception shouldn't happen!", e);
        }
    }

    public UnsafeListEntityImpl(List<Entity> entityList) {
        super(1, 1);
        this.entityList = entityList;
    }

    @Override
    public Entity get(int index) {
        rangeCheck(index);

        return entityList.get(index);
    }

    @Override
    public Entity unsafeGet(int index) {
        return entityList.get(index);
    }

    @Override
    public Entity set(int index, Entity element) {
        rangeCheck(index);

        return entityList.set(index, element);
    }

    @Override
    public boolean add(Entity element) {
        return entityList.add(element);
    }

    @Override
    public void add(int index, Entity element) {
        entityList.add(index, element);
    }

    @Override
    public Entity remove(int index) {
        rangeCheck(index);

        return entityList.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        int index = entityList.indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }

        return false;
    }

    @Override
    public int indexOf(Object o) {
        return entityList.indexOf(o);
    }

    @Override
    public boolean contains(Object o) {
        return entityList.contains(o);
    }

    @Override
    public void clear() {
        entityList.clear();
    }

    @Override
    public void trimToSize() {
        CatServer.log.warn("Method trimToSize is not supported");
    }

    @Override
    public int size() {
        return entityList.size();
    }

    @Override
    public boolean isEmpty() {
        return entityList.isEmpty();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new UnsafeListEntityImpl(new ArrayList<>(entityList));
    }

    @Override
    public Iterator<Entity> iterator() {
        return entityList.iterator();
    }
}
