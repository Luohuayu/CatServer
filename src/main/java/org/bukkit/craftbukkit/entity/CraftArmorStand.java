// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.util.math.Rotations;
import org.bukkit.util.EulerAngle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ArmorStand;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand
{
    public CraftArmorStand(final CraftServer server, final EntityArmorStand entity) {
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
        return (EntityArmorStand)super.getHandle();
    }
    
    @Override
    public ItemStack getItemInHand() {
        return this.getEquipment().getItemInHand();
    }
    
    @Override
    public void setItemInHand(final ItemStack item) {
        this.getEquipment().setItemInHand(item);
    }
    
    @Override
    public ItemStack getBoots() {
        return this.getEquipment().getBoots();
    }
    
    @Override
    public void setBoots(final ItemStack item) {
        this.getEquipment().setBoots(item);
    }
    
    @Override
    public ItemStack getLeggings() {
        return this.getEquipment().getLeggings();
    }
    
    @Override
    public void setLeggings(final ItemStack item) {
        this.getEquipment().setLeggings(item);
    }
    
    @Override
    public ItemStack getChestplate() {
        return this.getEquipment().getChestplate();
    }
    
    @Override
    public void setChestplate(final ItemStack item) {
        this.getEquipment().setChestplate(item);
    }
    
    @Override
    public ItemStack getHelmet() {
        return this.getEquipment().getHelmet();
    }
    
    @Override
    public void setHelmet(final ItemStack item) {
        this.getEquipment().setHelmet(item);
    }
    
    @Override
    public EulerAngle getBodyPose() {
        return fromNMS(this.getHandle().bodyRotation);
    }
    
    @Override
    public void setBodyPose(final EulerAngle pose) {
        this.getHandle().setBodyRotation(toNMS(pose));
    }
    
    @Override
    public EulerAngle getLeftArmPose() {
        return fromNMS(this.getHandle().leftArmRotation);
    }
    
    @Override
    public void setLeftArmPose(final EulerAngle pose) {
        this.getHandle().setLeftArmRotation(toNMS(pose));
    }
    
    @Override
    public EulerAngle getRightArmPose() {
        return fromNMS(this.getHandle().rightArmRotation);
    }
    
    @Override
    public void setRightArmPose(final EulerAngle pose) {
        this.getHandle().setRightArmRotation(toNMS(pose));
    }
    
    @Override
    public EulerAngle getLeftLegPose() {
        return fromNMS(this.getHandle().leftLegRotation);
    }
    
    @Override
    public void setLeftLegPose(final EulerAngle pose) {
        this.getHandle().setLeftLegRotation(toNMS(pose));
    }
    
    @Override
    public EulerAngle getRightLegPose() {
        return fromNMS(this.getHandle().rightLegRotation);
    }
    
    @Override
    public void setRightLegPose(final EulerAngle pose) {
        this.getHandle().setRightLegRotation(toNMS(pose));
    }
    
    @Override
    public EulerAngle getHeadPose() {
        return fromNMS(this.getHandle().headRotation);
    }
    
    @Override
    public void setHeadPose(final EulerAngle pose) {
        this.getHandle().setHeadRotation(toNMS(pose));
    }
    
    @Override
    public boolean hasBasePlate() {
        return !this.getHandle().hasNoBasePlate();
    }
    
    @Override
    public void setBasePlate(final boolean basePlate) {
        this.getHandle().setNoBasePlate(!basePlate);
    }
    
    @Override
    public void setGravity(final boolean gravity) {
        super.setGravity(gravity);
        this.getHandle().noClip = !gravity;
    }
    
    @Override
    public boolean isVisible() {
        return !this.getHandle().isInvisible();
    }
    
    @Override
    public void setVisible(final boolean visible) {
        this.getHandle().setInvisible(!visible);
    }
    
    @Override
    public boolean hasArms() {
        return this.getHandle().getShowArms();
    }
    
    @Override
    public void setArms(final boolean arms) {
        this.getHandle().setShowArms(arms);
    }
    
    @Override
    public boolean isSmall() {
        return this.getHandle().isSmall();
    }
    
    @Override
    public void setSmall(final boolean small) {
        this.getHandle().setSmall(small);
    }
    
    private static EulerAngle fromNMS(final Rotations old) {
        return new EulerAngle(Math.toRadians(old.getX()), Math.toRadians(old.getY()), Math.toRadians(old.getZ()));
    }
    
    private static Rotations toNMS(final EulerAngle old) {
        return new Rotations((float)Math.toDegrees(old.getX()), (float)Math.toDegrees(old.getY()), (float)Math.toDegrees(old.getZ()));
    }
    
    @Override
    public boolean isMarker() {
        return this.getHandle().hasMarker();
    }
    
    @Override
    public void setMarker(final boolean marker) {
        this.getHandle().setMarker(marker);
    }
}
