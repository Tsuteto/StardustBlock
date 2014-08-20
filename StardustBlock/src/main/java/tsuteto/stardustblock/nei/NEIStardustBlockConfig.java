package tsuteto.stardustblock.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIStardustBlockConfig implements IConfigureNEI
{

    public void loadConfig()
    {
        {
            StardustBlockShapelessRecipeHandler recipeHandler = new StardustBlockShapelessRecipeHandler();
            API.registerRecipeHandler(recipeHandler);
            API.registerUsageHandler(recipeHandler);
        }

        {
            StardustBlockShapedRecipeHandler recipeHandler = new StardustBlockShapedRecipeHandler();
            API.registerRecipeHandler(recipeHandler);
            API.registerUsageHandler(recipeHandler);
        }
    }

    public String getName()
    {
        return "Stardust Block NEI Plugin";
    }

    public String getVersion()
    {
        return "0.0.1";
    }

}