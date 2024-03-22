package net.mehvahdjukaar.supplementaries.client.cannon;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;

public abstract class CannonChargeOverlay extends Gui {

    private final Minecraft minecraft;
    private final ItemRenderer itemRenderer;

    protected CannonChargeOverlay(Minecraft minecraft, ItemRenderer itemRenderer) {
        super(minecraft, itemRenderer);
        this.itemRenderer = itemRenderer;
        this.minecraft = minecraft;
    }

    public void renderBar(GuiGraphics graphics, float partialTicks, int screenWidth, int screenHeight) {
        if (!minecraft.options.hideGui && CannonController.isActive()) {

            setupOverlayRenderState();

            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, -90);

            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int w = 9;
            graphics.blit(ModTextures.CANNON_ICONS_TEXTURE, (screenWidth - w) / 2, (screenHeight - w) / 2,
                    0, 10, w, w);

            RenderSystem.defaultBlendFunc();


            graphics.pose().popPose();

            int i = screenWidth / 2 - 91;

            float c = 1 - CannonController.cannon.getCooldown();
            int k = (int) (c * 183.0F);
            int l = screenHeight - 32 + 3;
            graphics.blit(ModTextures.CANNON_ICONS_TEXTURE, i, l, 0, 0, 182, 5);

            float f = CannonController.cannon.getFireTimer();
            if (f > 0) RenderSystem.setShaderColor(1, 0.6f + 0.4f * f, f, 1.0F);

            graphics.blit(ModTextures.CANNON_ICONS_TEXTURE, i, l, 0, 5, k, 5);


            int power = CannonController.cannon.getFirePower();

            int color = switch (power) {
                default -> 0xffcc00;
                case 2 -> 0xffaa00;
                case 3 -> 0xff8800;
                case 4 -> 0xff6600;
            };

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            String s = String.valueOf(power);
            int i1 = (screenWidth - this.getFont().width(s)) / 2;
            int j1 = screenHeight - 31 - 4;
            graphics.drawString(this.getFont(), s, i1 + 1, j1, 0, false);
            graphics.drawString(this.getFont(), s, i1 - 1, j1, 0, false);
            graphics.drawString(this.getFont(), s, i1, j1 + 1, 0, false);
            graphics.drawString(this.getFont(), s, i1, j1 - 1, 0, false);
            graphics.drawString(this.getFont(), s, i1, j1, color, false);

        }
    }

    public void setupOverlayRenderState() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }
}