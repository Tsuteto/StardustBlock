package tsuteto.stardustblock.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import tsuteto.stardustblock.param.StardustInfo;

import java.util.Random;

public class TileEntityStardustBlock extends TileEntity
{
    public static Random rand = new Random();

    public Dust[] dustList;
    public StardustInfo info = new StardustInfo().setShape(StardustInfo.Shape.star).setDense(2);

    @SideOnly(Side.CLIENT)
    private int idxBlinking;
    
    @SideOnly(Side.CLIENT)
    private long prevWorldTime;
    
    public TileEntityStardustBlock() {}

    public void init()
    {
        dustList = new Dust[1 << this.info.dense];

        int[] colorPalette = info.makeColorPalette();

        for (int i = 0; i < dustList.length; i++)
        {
            Dust p = new Dust();

            p.posX = rand.nextFloat();
            p.posY = rand.nextFloat();
            p.posZ = rand.nextFloat();

            p.rotationYaw = rand.nextFloat() * 360 - 180;
            p.rotationPitch = rand.nextFloat() * 360 - 180;

            int c;
            if (colorPalette.length > 1)
            {
                c = rand.nextInt(colorPalette.length);
            }
            else
            {
                c = 0;
            }

            p.colorR = ((colorPalette[c] >> 16) & 255) / 255F;
            p.colorG = ((colorPalette[c] >> 8) & 255) / 255F;
            p.colorB = (colorPalette[c] & 255) / 255F;

            p.isBlinking = false;

            dustList[i] = p;
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        info.writeToNBT(par1NBTTagCompound);
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.info = StardustInfo.readFromNBT(par1NBTTagCompound);
        this.init();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, nbttagcompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public double getMaxRenderDistanceSquared()
    {
        return 512.0D;
    }

    @SideOnly(Side.CLIENT)
    public void updateBlinking()
    {
        if (info.isSolid()) return;

        int i = (int)(this.worldObj.getTotalWorldTime() - this.prevWorldTime);
        this.prevWorldTime = this.worldObj.getTotalWorldTime();

        if (i > 0)
        {
            dustList[idxBlinking].isBlinking = false;
            
            if (rand.nextInt(10) == 0)
            {
                idxBlinking = rand.nextInt(dustList.length);
                dustList[idxBlinking].isBlinking = true;
            }
        }
    }

    public ItemStack getItemStack(Item item)
    {
        ItemStack stack = new ItemStack(item);
        NBTTagCompound nbt = new NBTTagCompound();
        this.info.writeToNBT(nbt);
        stack.setTagCompound(nbt);
        return stack;
    }

    public static class Dust
    {
        public float posX;
        public float posY;
        public float posZ;
        
        public float rotationYaw;
        public float rotationPitch;
        
        public float colorR;
        public float colorG;
        public float colorB;
        
        boolean isBlinking;
    }
}
