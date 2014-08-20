package tsuteto.stardustblock.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import tsuteto.stardustblock.tileentity.TileEntityStardustBlock.Dust;

@SideOnly(Side.CLIENT)
public class TileEntityStardustBlockRenderer extends TileEntitySpecialRenderer
{
    public static TileEntityStardustBlockRenderer renderer;
    public static ResourceLocation texture = new ResourceLocation("stardustblock", "textures/dust.png");
    private final RenderManager renderManager = RenderManager.instance;

    public void renderTileEntityAt(TileEntityStardustBlock tileEntity, double par2, double par4, double par6, float par8)
    {
        this.func_82393_a(tileEntity, (float)par2, (float)par4, (float)par6);
    }

    /**
     * Associate a TileEntityRenderer with this TileEntitySpecialRenderer
     */
    @Override
    public void func_147497_a(TileEntityRendererDispatcher par1TileEntityRenderer)
    {
        super.func_147497_a(par1TileEntityRenderer);
        renderer = this;
    }

    public void func_82393_a(TileEntityStardustBlock tileEntity, float par1, float par2, float par3)
    {
        if (tileEntity.dustList == null) return;

        tileEntity.updateBlinking();
        
        int s = tileEntity.info.shape.id;
        
        float f1 = 0.325F / 3;
        
        float f2 = 1.0F / 64.0F;
        float f3 = (s * 5 + 0F) / 64.0F;
        float f4 = (s * 5 + 5F) / 64.0F;
        float f5 = 0F / 64.0F;
        float f6 = 5F / 64.0F;
        float f7 = 1.0F;
        float f8 = 0.5F;
        float f9 = 0.25F;
        
        Tessellator tessellator = Tessellator.instance;
        
        this.bindTexture(texture);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        for (Dust p : tileEntity.dustList)
        {
            boolean isBlinking = p.isBlinking;
            
            GL11.glPushMatrix();
            GL11.glTranslatef(par1 + p.posX, par2 + p.posY, par3 + p.posZ);
            GL11.glScalef(f1, f1, f1);
            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            
            if (isBlinking)
            {
                tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else
            {
                tessellator.setColorRGBA_F(p.colorR, p.colorG, p.colorB, 1.0F);
            }
            
            tessellator.addVertexWithUV((0.0F - f8), (0.0F - f9), 0.0D, f3, f6);
            tessellator.addVertexWithUV((f7 - f8), (0.0F - f9), 0.0D, f4, f6);
            tessellator.addVertexWithUV((f7 - f8), (1.0F - f9), 0.0D, f4, f5);
            tessellator.addVertexWithUV((0.0F - f8), (1.0F - f9), 0.0D, f3, f5);
            
            tessellator.draw();
            GL11.glPopMatrix();    
        }
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityAt((TileEntityStardustBlock)par1TileEntity, par2, par4, par6, par8);
    }
}
