package tsuteto.stardustblock;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tsuteto.stardustblock.block.BlockStardust;
import tsuteto.stardustblock.item.ItemStardustBlock;
import tsuteto.stardustblock.param.StardustInfo;
import tsuteto.stardustblock.param.StardustUpgradeInfo;
import tsuteto.stardustblock.recipe.ShapedStardustRecipe;
import tsuteto.stardustblock.recipe.ShapelessStardustRecipe;
import tsuteto.stardustblock.tileentity.TileEntityStardustBlock;
import tsuteto.stardustblock.tileentity.TileEntityStardustBlockRenderer;

@Mod(modid = StardustBlock.modid, name = "StardustBlock", version = "1.1.2-MC1.7.2")
public class StardustBlock
{
    public static final String modid = "StardustBlock";
    public static Block blockStardust;
    public static CreativeTabs creativeTab = new CreativeTabs(modid)
    {
        @Override
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(blockStardust);
        }
    };
    
    @SidedProxy(clientSide = "tsuteto.stardustblock.StardustBlock$ClientProxy", serverSide = "tsuteto.stardustblock.StardustBlock$ServerProxy")
    public static ISidedProxy sidedProxy;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
//        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
//        cfg.load();
//        cfg.save();
        
        blockStardust = new BlockStardust()
                .setHardness(0.3F)
                .setLightLevel(1.0F)
                .setStepSound(Block.soundTypeGlass)
                .setBlockName("blockStardust")
                .setCreativeTab(creativeTab)
                .setBlockTextureName("stardustblock:block_frame")
                ;
        GameRegistry.registerBlock(blockStardust, ItemStardustBlock.class, "stardustblock:stardustBlock", null, (Block)blockStardust);
        GameRegistry.registerTileEntity(TileEntityStardustBlock.class, "stardustblock.stardustBlock");
        
        sidedProxy.registerComponents();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        RecipeSorter.register("StardustBlock:shaped", ShapedStardustRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("StardustBlock:shapeless", ShapelessStardustRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        // Star
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.star).setDense(2), 12,
                " R ",
                " G ",
                "R R",
                'R', Items.redstone,
                'G', Items.glowstone_dust));
        
        // Circle
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.circle).setDense(2), 12,
                "RRR",
                " G ",
                "RRR",
                'R', Items.redstone,
                'G', Items.glowstone_dust));
        
        // Diamond
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.diamond).setDense(2), 12,
                "R R",
                " G ",
                " R ",
                'R', Items.redstone,
                'G', Items.glowstone_dust));
        
        // Heart
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.heart).setDense(2), 12,
                "R R",
                "RGR",
                " R ",
                'R', Items.redstone,
                'G', Items.glowstone_dust));

        // Creeper
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.creeper).setDense(2), 12,
                " R ",
                "RGR",
                "R R",
                'R', Items.redstone,
                'P', Items.gunpowder,
                'G', Items.glowstone_dust));

        // Cross
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.cross).setDense(2), 12,
                " R ",
                "RGR",
                " R ",
                'R', Items.redstone,
                'G', Items.glowstone_dust));

        // Flower
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.flower).setDense(2), 12,
                "R R",
                " G ",
                "R R",
                'R', Items.redstone,
                'G', Items.glowstone_dust));

        // Ring
        GameRegistry.addRecipe(new ShapedStardustRecipe(
                new StardustInfo().setShape(StardustInfo.Shape.ring).setDense(2), 12,
                "R R",
                "RGR",
                "R R",
                'R', Items.redstone,
                'G', Items.glowstone_dust));

        // Upgrade
        GameRegistry.addRecipe(new ShapelessStardustRecipe(
                new StardustUpgradeInfo().setDense(1), 1,
                blockStardust,
                Items.glowstone_dust));

        GameRegistry.addRecipe(new ShapelessStardustRecipe(
                new StardustUpgradeInfo().setSolid(true), 1,
                blockStardust,
                new ItemStack(Items.coal, 1, OreDictionary.WILDCARD_VALUE)));

        // Color variation
        for (int i = 0; i < 16; i++)
        {
            GameRegistry.addRecipe(new ShapelessStardustRecipe(
                    new StardustUpgradeInfo().addColor(StardustInfo.Color.getById(i + 1)), 1,
                    blockStardust,
                    new ItemStack(Items.dye, 1, i)));
        }
        GameRegistry.addRecipe(new ShapelessStardustRecipe(
                new StardustUpgradeInfo().addColor(StardustInfo.Color.pastel), 1,
                blockStardust,
                new ItemStack(Items.dye, 1, StardustInfo.Color.red.id - 1),
                new ItemStack(Items.dye, 1, StardustInfo.Color.green.id - 1),
                new ItemStack(Items.dye, 1, StardustInfo.Color.blue.id - 1)));

        // Clear color
        GameRegistry.addRecipe(new ShapelessStardustRecipe(
                new StardustUpgradeInfo().setToClearColor().setSolid(false), 1,
                blockStardust,
                Items.feather));
    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy implements ISidedProxy
    {
        @Override
        public void registerComponents()
        {
            ClientRegistry.registerTileEntity(TileEntityStardustBlock.class, "stardustblock.stardustBlockRen", new TileEntityStardustBlockRenderer());
        }
    }

    @SideOnly(Side.SERVER)
    public static class ServerProxy implements ISidedProxy
    {
        @Override
        public void registerComponents()
        {
        }
    }

    private static interface ISidedProxy
    {
        void registerComponents();
    }
}
