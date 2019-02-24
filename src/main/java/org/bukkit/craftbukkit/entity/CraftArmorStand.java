package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.Rotations;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

    public CraftArmorStand(CraftServer server, EntityArmorStand entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public EntityArmorStand getHandle() {
        return (EntityArmorStand) super.getHandle();
    }

    @Override
    public ItemStack getItemInHand() {
        return getEquipment().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        getEquipment().setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment().getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        getEquipment().setHelmet(item);
    }

    @Override
    public EulerAngle getBodyPose() {
        return fromNMS(getHandle().bodyRotation);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        getHandle().setBodyRotation(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return fromNMS(getHandle().leftArmRotation);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        getHandle().setLeftArmRotation(toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return fromNMS(getHandle().rightArmRotation);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        getHandle().setRightArmRotation(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return fromNMS(getHandle().leftLegRotation);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        getHandle().setLeftLegRotation(toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return fromNMS(getHandle().rightLegRotation);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        getHandle().setRightLegRotation(toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return fromNMS(getHandle().headRotation);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        getHandle().setHeadRotation(toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return !getHandle().hasNoBasePlate();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        getHandle().setNoBasePlate(!basePlate);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setGravity(gravity);
        // Armor stands are special
        getHandle().noClip = !gravity;
    }

    @Override
    public boolean isVisible() {
        return !getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().setInvisible(!visible);
    }

    @Override
    public boolean hasArms() {
        return getHandle().getShowArms();
    }

    @Override
    public void setArms(boolean arms) {
        getHandle().setShowArms(arms);
    }

    @Override
    public boolean isSmall() {
        return getHandle().isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        getHandle().setSmall(small);
    }

    private static EulerAngle fromNMS(Rotations old) {
        return new EulerAngle(
            Math.toRadians(old.getX()),
            Math.toRadians(old.getY()),
            Math.toRadians(old.getZ())
        );
    }

    private static Rotations toNMS(EulerAngle old) {
        return new Rotations(
            (float) Math.toDegrees(old.getX()),
            (float) Math.toDegrees(old.getY()),
            (float) Math.toDegrees(old.getZ())
        );
    }

    @Override
    public boolean isMarker() {
        return getHandle().hasMarker();
    }

    @Override
    public void setMarker(boolean marker) {
        getHandle().setMarker(marker);
    }
}
