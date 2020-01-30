package catserver.server.inventory;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

public class CatForgeItemCap implements Cloneable {
    protected NBTTagCompound capNBT;

    public CatForgeItemCap(NBTTagCompound capNBT) {
        this.capNBT = capNBT;
    }

    public NBTTagCompound getItemCap() {
        return capNBT.copy();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CatForgeItemCap)) {
            return false;
        }

        return capNBT.equals(((CatForgeItemCap) obj).capNBT);
    }

    @Override
    public CatForgeItemCap clone() {
        try {
            return (CatForgeItemCap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public static void setItemCap(net.minecraft.item.ItemStack nmsItemStack, ItemStack bukkitItemStack) {
        if (nmsItemStack != null && nmsItemStack.capabilities != null) {
            NBTTagCompound capNBT = nmsItemStack.capabilities.serializeNBT();
            if (capNBT != null && !capNBT.hasNoTags()) {
                bukkitItemStack.setForgeItemCap(new CatForgeItemCap(capNBT));
            }
        }
    }
}
