package org.bukkit.craftbukkit.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class CraftMerchantCustom extends CraftMerchant {

    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
    }

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    private static class MinecraftMerchant implements IMerchant {

        private final String title;
        private final MerchantRecipeList trades = new MerchantRecipeList();
        private EntityPlayer tradingPlayer;

        public MinecraftMerchant(String title) {
            this.title = title;
        }

        @Override
        public void setCustomer(EntityPlayer entityhuman) {
            this.tradingPlayer = entityhuman;
        }

        @Override
        public EntityPlayer getCustomer() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantRecipeList getRecipes(EntityPlayer entityhuman) {
            return this.trades;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void setRecipes(@Nullable MerchantRecipeList recipeList) {

        }

        @Override
        public void useRecipe(MerchantRecipe merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.incrementToolUses();
        }

        @Override
        public void verifySellingItem(ItemStack itemstack) {

        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString(title);
        }

        @Override
        public World getWorld() {
            return null;
        }

        @Override
        public BlockPos getPos() {
            return null;
        }
    }
}
