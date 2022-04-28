/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R3.block.impl;

public final class CraftPistonExtension extends org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData implements org.bukkit.block.data.type.PistonHead, org.bukkit.block.data.type.TechnicalPiston, org.bukkit.block.data.Directional {

    public CraftPistonExtension() {
        super();
    }

    public CraftPistonExtension(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftPistonHead

    private static final net.minecraft.state.BooleanProperty SHORT = getBoolean(net.minecraft.block.PistonHeadBlock.class, "short");

    @Override
    public boolean isShort() {
        return get(SHORT);
    }

    @Override
    public void setShort(boolean _short) {
        set(SHORT, _short);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.type.CraftTechnicalPiston

    private static final net.minecraft.state.EnumProperty<?> TYPE = getEnum(net.minecraft.block.PistonHeadBlock.class, "type");

    @Override
    public Type getType() {
        return get(TYPE, Type.class);
    }

    @Override
    public void setType(Type type) {
        set(TYPE, type);
    }

    // org.bukkit.craftbukkit.v1_16_R2.block.data.CraftDirectional

    private static final net.minecraft.state.EnumProperty<?> FACING = getEnum(net.minecraft.block.PistonHeadBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }
}
