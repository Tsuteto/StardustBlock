package tsuteto.stardustblock.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import tsuteto.stardustblock.StardustBlock;
import tsuteto.stardustblock.param.StardustInfo;
import tsuteto.stardustblock.param.StardustUpgradeInfo;
import tsuteto.stardustblock.recipe.ShapedStardustRecipe;
import tsuteto.stardustblock.recipe.ShapelessStardustRecipe;

import java.util.ArrayList;
import java.util.List;

public class StardustBlockShapelessRecipeHandler extends ShapelessRecipeHandler
{
    public class CachedStardustRecipe extends CachedShapelessRecipe
    {
        private final StardustInfo info;

        public CachedStardustRecipe(StardustInfo info, List<Object> input, ItemStack output)
        {
//            for (int i = 0; i < input.size(); i++)
//            {
//                if (input.get(i) instanceof ItemStack && ((ItemStack)input.get(i)).getItem() == Item.getItemFromBlock(StardustBlock.blockStardust))
//                {
//                    input.set(i, this.makeSamples());
//                }
//            }

            setIngredients(input);
            setResult(output);
            this.info = info;
        }

//        private List<ItemStack> makeSamples()
//        {
//            Block blockStardust = StardustBlock.blockStardust;
//
//            List<ItemStack> list = Lists.newArrayList();
//
//            // No color
//            for (StardustInfo.Shape shape : StardustInfo.Shape.values())
//            {
//                NBTTagCompound nbt = new NBTTagCompound();
//                StardustInfo info = new StardustInfo().setDense(4);
//                info.setShape(shape).writeToNBT(nbt);
//                ItemStack stack = new ItemStack(blockStardust);
//                stack.setTagCompound(nbt);
//                list.add(stack);
//            }
//
//            // Colored
//            for (StardustInfo.Color color : StardustInfo.Color.values())
//            {
//                for (StardustInfo.Shape shape : StardustInfo.Shape.values())
//                {
//                    NBTTagCompound nbt = new NBTTagCompound();
//                    StardustInfo info = new StardustInfo().setDense(4);
//                    info.setShape(shape).addColor(color).writeToNBT(nbt);
//                    ItemStack stack = new ItemStack(blockStardust);
//                    stack.setTagCompound(nbt);
//                    list.add(stack);
//                }
//            }
//            return list;
//        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            NBTTagCompound nbtTag = new NBTTagCompound();
            if (info instanceof StardustUpgradeInfo)
            {
                StardustInfo.DEFAULT.writeToNBT(nbtTag);
                ((StardustUpgradeInfo)info).upgradeNBT(nbtTag);
            }
            else
            {
                info.writeToNBT(nbtTag);
            }
            result.item.setTagCompound(nbtTag);

            return result;
        }
    }

    public StardustBlockShapelessRecipeHandler()
    {
        super();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals("item") && getClass() == StardustBlockShapelessRecipeHandler.class)
        {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes)
            {
                CachedStardustRecipe recipe = null;
                if (irecipe instanceof ShapelessStardustRecipe)
                {
                    ShapelessStardustRecipe stardustRecipe = (ShapelessStardustRecipe) irecipe;
                    recipe = forgeShapelessRecipe(stardustRecipe);
                }

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadUsageRecipes(ItemStack ingredient)
    {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes)
        {
            CachedStardustRecipe recipe = null;
            if (irecipe instanceof ShapelessStardustRecipe)
            {
                recipe = forgeShapelessRecipe((ShapelessStardustRecipe) irecipe);
            }

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            if (recipe.contains(recipe.ingredients, ingredient))
            {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
    {
        return currenttip;
    }

    public CachedStardustRecipe forgeShapelessRecipe(ShapelessStardustRecipe recipe)
    {
        ArrayList<Object> items = recipe.getOreRecipe().getInput();

        for (Object item : items)
            if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                return null;

        return new CachedStardustRecipe(recipe.getInfo(), items, recipe.getRecipeOutput().copy());
    }
}
