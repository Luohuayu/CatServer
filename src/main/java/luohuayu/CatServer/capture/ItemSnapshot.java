package luohuayu.CatServer.capture;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemSnapshot{

    public final EntityPlayer mPlayer;
    public final ItemStack mItem;

    private boolean mApply=false;

    public ItemSnapshot(EntityPlayer pPlayer,ItemStack pItem){
        this.mPlayer=pPlayer;
        this.mItem=pItem;
    }

    /**
     * 将物品放到玩家身上
     * 
     * @return 如果背包满,将生成物品在地上并返回false
     */
    public boolean apply(){
        if(!this.mApply){
            this.mApply=true;

            if(!this.mPlayer.inventory.addItemStackToInventory(this.mItem)){
                this.mPlayer.worldObj.spawnEntityInWorld(
                        new EntityItem(this.mPlayer.worldObj,this.mPlayer.posX,this.mPlayer.posY,this.mPlayer.posZ,this.mItem));
                return false;
            }
        }
        return true;
    }

}
