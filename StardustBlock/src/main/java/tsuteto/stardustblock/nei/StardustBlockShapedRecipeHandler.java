package tsuteto.stardustblock.nei;

import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tsuteto.stardustblock.param.StardustInfo;
import tsuteto.stardustblock.param.StardustUpgradeInfo;
import tsuteto.stardustblock.recipe.ShapedStardustRecipe;
import tsuteto.stardustblock.recipe.ShapelessStardustRecipe;

import java.util.ArrayList;
import java.util.List;

public class StardustBlockShapedRecipeHandler extends ShapedRecipeHandler
{
    public class CachedStardustRecipe extends CachedShapedRecipe
    {
        public CachedStardustRecipe(StardustInfo info, int width, int height, Object[] input, ItemStack output)
        {
            super(width, height, input, output);

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
            output.setTagCompound(nbtTag);

            result = new PositionedStack(output, 119, 24);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients(width, height, input);
        }
    }


    public StardustBlockShapedRecipeHandler()
    {
        super();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals("item") && getClass() == StardustBlockShapedRecipeHandler.class)
        {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes)
            {
                CachedStardustRecipe recipe = null;
                if (irecipe instanceof ShapedStardustRecipe)
                {
                    recipe = forgeShapedRecipe((ShapedStardustRecipe) irecipe);
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
            if (irecipe instanceof ShapedStardustRecipe)
            {
                recipe = forgeShapedRecipe((ShapedStardustRecipe) irecipe);
            }

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            recipe.computeVisuals();
            arecipes.add(recipe);
        }
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
    {
        return currenttip;
    }

    public CachedStardustRecipe forgeShapedRecipe(ShapedStardustRecipe recipe)
    {
        int width;
        int height;
        try
        {
            width = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe.getOreRecipe(), 4);
            height = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe.getOreRecipe(), 5);
        } catch (Exception e)
        {
            NEIClientConfig.logger.error("Error loading recipe", e);
            return null;
        }

        Object[] items = recipe.getOreRecipe().getInput();
        for (Object item : items)
            if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                return null;

        return new CachedStardustRecipe(recipe.getInfo(), width, height, items, recipe.getRecipeOutput());
    }
}
