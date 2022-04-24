package org.bukkit.craftbukkit.v1_18_R2.persistence;

import com.google.common.primitives.Primitives;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.persistence.PersistentDataContainer;

/**
 * This class represents a registry that contains the used adapters for.
 */
public final class CraftPersistentDataTypeRegistry {

    private final Function<Class, TagAdapter> CREATE_ADAPTER = this::createAdapter;

    private class TagAdapter<T, Z extends Tag> {

        private final Function<T, Z> builder;
        private final Function<Z, T> extractor;

        private final Class<T> primitiveType;
        private final Class<Z> nbtBaseType;

        public TagAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
            this.primitiveType = primitiveType;
            this.nbtBaseType = nbtBaseType;
            this.builder = builder;
            this.extractor = extractor;
        }

        /**
         * This method will extract the value stored in the tag, according to
         * the expected primitive type.
         *
         * @param base the base to extract from
         *
         * @return the value stored inside of the tag
         *
         * @throws ClassCastException if the passed base is not an instanced of
         * the defined base type and therefore is not applicable to the
         * extractor function
         */
        T extract(Tag base) {
            if (!nbtBaseType.isInstance(base)) {
                throw new IllegalArgumentException(String.format("The provided Tag was of the type %s. Expected type %s", base.getClass().getSimpleName(), nbtBaseType.getSimpleName()));
            }
            return this.extractor.apply(nbtBaseType.cast(base));
        }

        /**
         * Builds a tag instance wrapping around the provided value object.
         *
         * @param value the value to store inside the created tag
         *
         * @return the new tag instance
         *
         * @throws ClassCastException if the passed value object is not of the
         * defined primitive type and therefore is not applicable to the builder
         * function
         */
        Z build(Object value) {
            if (!primitiveType.isInstance(value)) {
                throw new IllegalArgumentException(String.format("The provided value was of the type %s. Expected type %s", value.getClass().getSimpleName(), primitiveType.getSimpleName()));
            }
            return this.builder.apply(primitiveType.cast(value));
        }

        /**
         * Returns if the tag instance matches the adapters one.
         *
         * @param base the base to check
         *
         * @return if the tag was an instance of the set type
         */
        boolean isInstance(Tag base) {
            return this.nbtBaseType.isInstance(base);
        }
    }

    private final Map<Class, TagAdapter> adapters = new HashMap<>();

    /**
     * Creates a suitable adapter instance for the primitive class type
     *
     * @param type the type to create an adapter for
     * @param <T> the generic type of that class
     *
     * @return the created adapter instance
     *
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    private <T> TagAdapter createAdapter(Class<T> type) {
        if (!Primitives.isWrapperType(type)) {
            type = Primitives.wrap(type); //Make sure we will always "switch" over the wrapper types
        }

        /*
            Primitives
         */
        if (Objects.equals(Byte.class, type)) {
            return createAdapter(Byte.class, ByteTag.class, ByteTag::valueOf, ByteTag::getAsByte);
        }
        if (Objects.equals(Short.class, type)) {
            return createAdapter(Short.class, ShortTag.class, ShortTag::valueOf, ShortTag::getAsShort);
        }
        if (Objects.equals(Integer.class, type)) {
            return createAdapter(Integer.class, IntTag.class, IntTag::valueOf, IntTag::getAsInt);
        }
        if (Objects.equals(Long.class, type)) {
            return createAdapter(Long.class, LongTag.class, LongTag::valueOf, LongTag::getAsLong);
        }
        if (Objects.equals(Float.class, type)) {
            return createAdapter(Float.class, FloatTag.class, FloatTag::valueOf, FloatTag::getAsFloat);
        }
        if (Objects.equals(Double.class, type)) {
            return createAdapter(Double.class, DoubleTag.class, DoubleTag::valueOf, DoubleTag::getAsDouble);
        }

        /*
            String
         */
        if (Objects.equals(String.class, type)) {
            return createAdapter(String.class, StringTag.class, StringTag::valueOf, StringTag::getAsString);
        }

        /*
            Primitive Arrays
         */
        if (Objects.equals(byte[].class, type)) {
            return createAdapter(byte[].class, ByteArrayTag.class, array -> new ByteArrayTag(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsByteArray(), n.size()));
        }
        if (Objects.equals(int[].class, type)) {
            return createAdapter(int[].class, IntArrayTag.class, array -> new IntArrayTag(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsIntArray(), n.size()));
        }
        if (Objects.equals(long[].class, type)) {
            return createAdapter(long[].class, LongArrayTag.class, array -> new LongArrayTag(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsLongArray(), n.size()));
        }

        /*
            Complex Arrays
         */
        if (Objects.equals(PersistentDataContainer[].class, type)) {
            return createAdapter(PersistentDataContainer[].class, ListTag.class,
                    (containerArray) -> {
                        ListTag list = new ListTag();
                        for (int i = 0; i < containerArray.length; i++) {
                            list.add(((CraftPersistentDataContainer) containerArray[i]).toTagCompound());
                        }
                        return list;
                    },
                    (tag) -> {
                        PersistentDataContainer[] containerArray = new CraftPersistentDataContainer[tag.size()];
                        for (int i = 0; i < tag.size(); i++) {
                            CraftPersistentDataContainer container = new CraftPersistentDataContainer(this);
                            CompoundTag compound = tag.getCompound(i);
                            for (String key : compound.getAllKeys()) {
                                container.put(key, compound.get(key));
                            }
                            containerArray[i] = container;
                        }
                        return containerArray;
                    });
        }

        /*
            Note that this will map the interface PersistentMetadataContainer directly to the CraftBukkit implementation
            Passing any other instance of this form to the tag type registry will throw a ClassCastException as defined in TagAdapter#build
         */
        if (Objects.equals(PersistentDataContainer.class, type)) {
            return createAdapter(CraftPersistentDataContainer.class, CompoundTag.class, CraftPersistentDataContainer::toTagCompound, tag -> {
                CraftPersistentDataContainer container = new CraftPersistentDataContainer(this);
                for (String key : tag.getAllKeys()) {
                    container.put(key, tag.get(key));
                }
                return container;
            });
        }

        throw new IllegalArgumentException("Could not find a valid TagAdapter implementation for the requested type " + type.getSimpleName());
    }

    private <T, Z extends Tag> TagAdapter<T, Z> createAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
        return new TagAdapter<>(primitiveType, nbtBaseType, builder, extractor);
    }

    /**
     * Wraps the passed value into a tag instance.
     *
     * @param type the type of the passed value
     * @param value the value to be stored in the tag
     * @param <T> the generic type of the value
     *
     * @return the created tag instance
     *
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    public <T> Tag wrap(Class<T> type, T value) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).build(value);
    }

    /**
     * Returns if the tag instance matches the provided primitive type.
     *
     * @param type the type of the primitive value
     * @param base the base instance to check
     * @param <T> the generic type of the type
     *
     * @return if the base stores values of the primitive type passed
     *
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    public <T> boolean isInstanceOf(Class<T> type, Tag base) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).isInstance(base);
    }

    /**
     * Extracts the value out of the provided tag.
     *
     * @param type the type of the value to extract
     * @param tag the tag to extract the value from
     * @param <T> the generic type of the value stored inside the tag
     *
     * @return the extracted value
     *
     * @throws IllegalArgumentException if the passed base is not an instanced
     * of the defined base type and therefore is not applicable to the extractor
     * function
     * @throws IllegalArgumentException if the found object is not of type
     * passed
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    public <T> T extract(Class<T> type, Tag tag) throws ClassCastException, IllegalArgumentException {
        TagAdapter adapter = this.adapters.computeIfAbsent(type, CREATE_ADAPTER);
        if (!adapter.isInstance(tag)) {
            throw new IllegalArgumentException(String.format("`The found tag instance cannot store %s as it is a %s", type.getSimpleName(), tag.getClass().getSimpleName()));
        }

        Object foundValue = adapter.extract(tag);
        if (!type.isInstance(foundValue)) {
            throw new IllegalArgumentException(String.format("The found object is of the type %s. Expected type %s", foundValue.getClass().getSimpleName(), type.getSimpleName()));
        }
        return type.cast(foundValue);
    }
}
