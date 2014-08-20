package tsuteto.stardustblock.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tsuteto.stardustblock.param.StardustInfo;
import tsuteto.stardustblock.tileentity.TileEntityStardustBlock;

public class BlockStardust extends BlockContainer
{
    public BlockStardust()
    {
        super(Material.glass);
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer)
    {
        if (par6EntityPlayer.capabilities.isCreativeMode)
        {
            // Set 8 for no drops
            par5 |= 8;
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par5, 4);
        }
        
        // Force to drop items here before removing tileentity
        dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
        super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if ((metadata & 8) == 0) // no drops when the metadata is 8
        {
            // Called twice, but drops items once when the tileentity is alive. Based on BlockSkull
            TileEntityStardustBlock te = (TileEntityStardustBlock)world.getTileEntity(x, y, z);
            if (te != null)
            {
                 Item item = getItemDropped(metadata, world.rand, fortune);
                if (item != null)
                {
                    ret.add(te.getItemStack(item));
                }
            }
        }
        return ret;
    }
    
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        Item item = getItem(world, x, y, z);

        if (item == null)
        {
            return null;
        }

        TileEntity tileentity = world.getTileEntity(x, y, z);
        if (tileentity != null && tileentity instanceof TileEntityStardustBlock)
        {
            return ((TileEntityStardustBlock) tileentity).getItemStack(item);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        AxisAlignedBB axisalignedbb1 = super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
        
        if (axisalignedbb1 != null && par5AxisAlignedBB.intersectsWith(axisalignedbb1)
                && par7Entity != null && par7Entity instanceof EntityLivingBase)
        {
            EntityLivingBase living = (EntityLivingBase)par7Entity;
            ItemStack boots = living.getEquipmentInSlot(1);
            if (boots != null && boots.getItem() == Items.diamond_boots)
            {
                par6List.add(super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4));
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    public void onBlockPlacedBy(World p_149689_1_, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par6)
    {
        TileEntityStardustBlock te = (TileEntityStardustBlock) p_149689_1_.getTileEntity(par2, par3, par4);

        StardustInfo info = StardustInfo.loadFromItem(par6);
        if (info == null)
        {
            // For old version support
            int meta = p_149689_1_.getBlockMetadata(par2, par3, par4);
            info = StardustInfo.readFromMetadata(meta);
        }
        te.info = info;
        te.init();
    }

    @Override
    public TileEntity createNewTileEntity(World par1World, int par2)
    {
        return new TileEntityStardustBlock();
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        // No color
        for (StardustInfo.Shape shape : StardustInfo.Shape.values())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            StardustInfo info = new StardustInfo().setDense(4);
            info.setShape(shape).writeToNBT(nbt);
            ItemStack stack = new ItemStack(par1);
            stack.setTagCompound(nbt);
            par3List.add(stack);
        }

        // Colored
        for (StardustInfo.Color color : StardustInfo.Color.values())
        {
            for (StardustInfo.Shape shape : StardustInfo.Shape.values())
            {
                NBTTagCompound nbt = new NBTTagCompound();
                StardustInfo info = new StardustInfo().setDense(4);
                info.setShape(shape).addColor(color).writeToNBT(nbt);
                ItemStack stack = new ItemStack(par1);
                stack.setTagCompound(nbt);
                par3List.add(stack);
            }
        }
    }
}
