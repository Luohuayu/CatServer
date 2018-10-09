package luohuayu.CatServer.capture;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class WorldCapture{

    public World mWorld;

    public ArrayList<BlockSnapshot> mCapturedBlockSnapshots=new ArrayList<BlockSnapshot>();
    public ArrayList<ItemSnapshot> mCapturedItem=new ArrayList<ItemSnapshot>();
    public ArrayList<EntitySnapshot> mCapturedEntity=new ArrayList<EntitySnapshot>();

    protected EntityPlayer mPlayer=null;
    protected final ItemStack[] mHandItem=new ItemStack[2];
    protected ItemStack mCurrentItem=null;

    protected EnumFacing mSide=null;
    protected EnumHand mHand=null;

    public WorldCapture(World pWorld,ArrayList<BlockSnapshot> pCaptureBlockList){
        this.mWorld=pWorld;
        this.mCapturedBlockSnapshots=pCaptureBlockList;
    }

    public void startCapture(EntityPlayer pPlayer,ItemStack pCurrentItem){
        this.startCapture(pPlayer,pCurrentItem,EnumFacing.DOWN,EnumHand.MAIN_HAND);
    }

    public void startCapture(EntityPlayer pPlayer,ItemStack pCurrentItem,EnumFacing pSide,EnumHand pHand){
        this.mPlayer=pPlayer;
        this.mHandItem[0]=ItemStack.copyItemStack(pPlayer.getHeldItemMainhand());
        this.mHandItem[1]=ItemStack.copyItemStack(pPlayer.getHeldItemOffhand());

        if(pCurrentItem!=null) this.mCurrentItem=ItemStack.copyItemStack(pCurrentItem);
        this.mHand=pHand;
        this.mSide=pSide;

        this.mWorld.captureBlockSnapshots=true;

        this.mCapturedBlockSnapshots.clear();
        this.mCapturedItem.clear();
        this.mCapturedEntity.clear();
    }

    public void addCaptureItem(EntityPlayer pPlayer,ItemStack pItem){
        if(this.mWorld.restoringBlockSnapshots) return;
        this.mCapturedItem.add(new ItemSnapshot(pPlayer,pItem));
    }

    public void addCaptureEntity(World pWorld,Entity pEntity,SpawnReason pReason){
        if(this.mWorld.restoringBlockSnapshots) return;
        this.mCapturedEntity.add(new EntitySnapshot(pWorld,pEntity,pReason));
    }

    public boolean isCapture(){
        return this.mWorld.captureBlockSnapshots;
    }

    public boolean isRestoring(){
        return this.mWorld.restoringBlockSnapshots;
    }

    public void endCapture(){
        this.mWorld.captureBlockSnapshots=false;
    }

    public EnumActionResult handleCapture(ItemStack pAfterUse){
        EnumActionResult tResult=EnumActionResult.SUCCESS;
        // save new item data
        int newMeta=pAfterUse.getItemDamage();
        int newSize=pAfterUse.stackSize;
        NBTTagCompound newNBT=null;
        if(pAfterUse.getTagCompound()!=null){
            newNBT=(NBTTagCompound)pAfterUse.getTagCompound().copy();
        }
        net.minecraftforge.event.world.BlockEvent.PlaceEvent placeEvent=null;
        @SuppressWarnings("unchecked")
        List<net.minecraftforge.common.util.BlockSnapshot> blockSnapshots=(List<BlockSnapshot>)this.mCapturedBlockSnapshots.clone();
        this.mCapturedBlockSnapshots.clear();

        // make sure to set pre-placement item data for event
        pAfterUse.setItemDamage(this.mCurrentItem.getItemDamage());
        pAfterUse.stackSize=this.mCurrentItem.stackSize;
        if(this.mCurrentItem.hasTagCompound()){
            pAfterUse.setTagCompound(this.mCurrentItem.getTagCompound());
        }
        if(blockSnapshots.size()>1){
            placeEvent=ForgeEventFactory.onPlayerMultiBlockPlace(this.mPlayer,blockSnapshots,this.mSide,this.mHand);
        }else if(blockSnapshots.size()==1){
            placeEvent=ForgeEventFactory.onPlayerBlockPlace(this.mPlayer,blockSnapshots.get(0),this.mSide,this.mHand);
        }

        if(placeEvent!=null&&(placeEvent.isCanceled())){
            tResult=EnumActionResult.FAIL; // cancel placement
            // revert back all captured blocks
            //for (BlockSnapshot blocksnapshot : blockSnapshots) {
            for(int i=blockSnapshots.size()-1;i>=0;i--){
                BlockSnapshot blocksnapshot=blockSnapshots.get(i);
                this.mWorld.restoringBlockSnapshots=true;
                blocksnapshot.restore(true,false);
                this.mWorld.restoringBlockSnapshots=false;
            }

            this.mPlayer.setHeldItem(EnumHand.MAIN_HAND,this.mHandItem[0]);
            this.mPlayer.setHeldItem(EnumHand.OFF_HAND,this.mHandItem[1]);
        }else{
            // Change the stack to its new content
            pAfterUse.setItemDamage(newMeta);
            pAfterUse.stackSize=newSize;
            if(this.mCurrentItem.hasTagCompound()){
                pAfterUse.setTagCompound(newNBT);
            }

            for(BlockSnapshot snap : blockSnapshots){
                int updateFlag=snap.getFlag();
                IBlockState oldBlock=snap.getReplacedBlock();
                IBlockState newBlock=this.mWorld.getBlockState(snap.getPos());
                if(newBlock!=null&&!(newBlock.getBlock().hasTileEntity(newBlock))) // Containers get placed automatically
                {
                    newBlock.getBlock().onBlockAdded(this.mWorld,snap.getPos(),newBlock);
                }

                this.mWorld.markAndNotifyBlock(snap.getPos(),null,oldBlock,newBlock,updateFlag);
            }

            for(int i=this.mCapturedItem.size()-1;i>=0;i--){
                this.mCapturedItem.get(i).apply();
            }

            for(int i=this.mCapturedEntity.size()-1;i>=0;i--){
                this.mCapturedEntity.get(i).apply();
            }

            this.mPlayer.addStat(StatList.getObjectUseStats(pAfterUse.getItem()));
        }

        this.mCapturedItem.clear();
        this.mCapturedEntity.clear();

        return tResult;
    }

}
