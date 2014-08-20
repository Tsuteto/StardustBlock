package tsuteto.stardustblock.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tsuteto.stardustblock.StardustBlock;
import tsuteto.stardustblock.param.StardustInfo;
import tsuteto.stardustblock.param.StardustUpgradeInfo;

public class StardustRecipeImpl
{
    private Item itemBlockStardust = Item.getItemFromBlock(StardustBlock.blockStardust);

    public final StardustInfo baseInfo;
    public NBTTagCompound resultNbt = null;

    public StardustRecipeImpl(StardustInfo info)
    {
        this.baseInfo = info;
    }

    public boolean matches(InventoryCrafting var1, World var2)
    {
        for (int i = 0; i < var1.getSizeInventory(); i++)
        {
            ItemStack target = var1.getStackInSlot(i);
            if (target != null && target.getItem() == itemBlockStardust)
            {
                resultNbt = target.copy().getTagCompound();
                break;
            }

        }

        if (resultNbt != null && baseInfo instanceof StardustUpgradeInfo)
        {
            return ((StardustUpgradeInfo) baseInfo).upgradeNBT(resultNbt);
        }
        else if (baseInfo != null)
        {
            resultNbt = new NBTTagCompound();
            baseInfo.writeToNBT(resultNbt);
            return true;
        }
        else
        {
            return false;
        }
    }

    public ItemStack applyToResult(ItemStack result)
    {
        result.setTagCompound(resultNbt);
        return result;
    }
}
