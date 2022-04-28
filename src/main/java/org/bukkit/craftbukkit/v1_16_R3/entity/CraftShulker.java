package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.ShulkerEntity;
import org.bukkit.DyeColor;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Shulker;

public class CraftShulker extends CraftGolem implements Shulker {

    public CraftShulker(CraftServer server, ShulkerEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftShulker";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHULKER;
    }

    @Override
    public ShulkerEntity getHandle() {
        return (ShulkerEntity) entity;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData(getHandle().getEntityData().get(ShulkerEntity.DATA_COLOR_ID));
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().getEntityData().set(ShulkerEntity.DATA_COLOR_ID, (color == null) ? 16 : color.getWoolData());
    }

    @Override
    public float getPeek() {
        return (float) getHandle().getRawPeekAmount() / 100; // PAIL rename getPeek
    }

    @Override
    public void setPeek(float value) {
        Preconditions.checkArgument(value >= 0 && value <= 1, "value needs to be in between or equal to 0 and 1");
        getHandle().setRawPeekAmount((int) (value * 100)); // PAIL rename setPeek
    }

    @Override
    public BlockFace getAttachedFace() {
        return CraftBlock.notchToBlockFace(getHandle().getAttachFace()); // PAIL rename getAttachedFace
    }

    @Override
    public void setAttachedFace(BlockFace face) {
        Preconditions.checkNotNull(face, "face cannot be null");
        Preconditions.checkArgument(face.isCartesian(), "%s is not a valid block face to attach a shulker to, a cartesian block face is expected", face);
        getHandle().getEntityData().set(ShulkerEntity.DATA_ATTACH_FACE_ID, CraftBlock.blockFaceToNotch(face));
    }
}
