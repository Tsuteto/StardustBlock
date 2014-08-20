package tsuteto.stardustblock.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tsuteto.stardustblock.StardustBlock;
import tsuteto.stardustblock.param.StardustInfo;

public class ShapedStardustRecipe implements IRecipe
{
    private StardustRecipeImpl impl;
    private ShapedOreRecipe oreRecipe;

    public ShapedStardustRecipe(StardustInfo info, int num, Object... recipe)
    {
        oreRecipe = new ShapedOreRecipe(new ItemStack(StardustBlock.blockStardust, num), recipe);
        this.impl = new StardustRecipeImpl(info);
    }

    @Override
    public boolean matches(InventoryCrafting var1, World var2)
    {
        return oreRecipe.matches(var1, var2) && this.impl.matches(var1, var2);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
        ItemStack result = oreRecipe.getCraftingResult(var1);
        return impl.applyToResult(result);
    }

    @Override
    public int getRecipeSize()
    {
        return oreRecipe.getRecipeSize();
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return oreRecipe.getRecipeOutput();
    }

    public ShapedOreRecipe getOreRecipe()
    {
        return oreRecipe;
    }

    public StardustInfo getInfo()
    {
        return this.impl.baseInfo;
    }
}
