package tsuteto.stardustblock.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.StringUtils;
import tsuteto.stardustblock.param.StardustInfo;
import tsuteto.stardustblock.block.BlockStardust;

public class ItemStardustBlock extends ItemBlockWithMetadata
{
    private IIcon particle;
    private IIcon pastel;

    public ItemStardustBlock(Block block1, BlockStardust block2)
    {
        super(block1, block2);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    public IIcon getIcon(ItemStack stack, int pass)
    {
        if (pass == 0)
        {
            StardustInfo info = StardustInfo.loadFromItem(stack);

            if (info.colorList.contains(StardustInfo.Color.pastel))
            {
                return this.pastel;
            }
            else
            {
                return this.particle;
            }
        }
        else
        {
            return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return par2 > 0 ? 0xffffff : this.getColorFromInfo(par1ItemStack);
    }

    private int getColorFromInfo(ItemStack par1ItemStack)
    {
        StardustInfo info = StardustInfo.loadFromItem(par1ItemStack);

        if (info.colorList.contains(StardustInfo.Color.pastel))
        {
            return 0xffffff;
        }
        else
        {
            // Mixing colors as same as ItemFireworkCharge
            int[] colorPalette = info.makeColorPalette();
            if (colorPalette.length == 1)
            {
                return colorPalette[0];
            }
            else
            {
                int j = 0;
                int k = 0;
                int l = 0;

                for (int k1 : colorPalette)
                {
                    j += (k1 & 0xff0000) >> 16;
                    k += (k1 & 0x00ff00) >> 8;
                    l += k1 & 0x0000ff;
                }

                j /= colorPalette.length;
                k /= colorPalette.length;
                l /= colorPalette.length;
                return j << 16 | k << 8 | l;
            }
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        StardustInfo info = StardustInfo.loadFromItem(par1ItemStack);
        par3List.add(StatCollector.translateToLocalFormatted("tile.blockStardust.desc.shape",
                StatCollector.translateToLocal("tile.blockStardust." + info.shape.name())));
        par3List.add(StatCollector.translateToLocalFormatted("tile.blockStardust.desc.stars", 1 << info.dense));
        if (!info.colorList.isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            for (StardustInfo.Color c : info.colorList)
            {
                if (sb.length() > 0) sb.append(", ");
                sb.append(StatCollector.translateToLocal("tile.blockStardust." + c.name()));
            }
            par3List.add(StatCollector.translateToLocalFormatted("tile.blockStardust.desc.color", sb.toString()));
        }
        if (info.isSolid())
        {
            par3List.add(StatCollector.translateToLocal("tile.blockStardust.solid"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.particle = par1IconRegister.registerIcon("stardustblock:block_particle");
        this.pastel = par1IconRegister.registerIcon("stardustblock:block_pastel");
    }
}