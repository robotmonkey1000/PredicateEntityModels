package studio.robotmonkey.predicatecustommodels.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.robotmonkey.predicatecustommodels.util.ModelRegistryManager;

@Mixin(TridentEntityRenderer.class)
public class TridentRenderMixin extends EntityRenderer<TridentEntity> {

    private final Identifier texture = new Identifier("textures/entity/trident.png");

    protected TridentRenderMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    //NOTE THIS IS FOR RENDERING IN WORLD
    @Inject(method="Lnet/minecraft/client/render/entity/TridentEntityRenderer;render(Lnet/minecraft/entity/projectile/TridentEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public void render(TridentEntity tridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci)
    {
        if(ModelRegistryManager.GetModelFromIndex(EntityType.TRIDENT, 0) != null)
        {
            matrixStack.push();
            //TODO FIGURE OUT MODEL SHIFT
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevYaw, tridentEntity.getYaw()) - 90.0F));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevPitch, tridentEntity.getPitch()) + 90.0F));
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, ModelRegistryManager.GetModelFromIndex(EntityType.TRIDENT, 0).getLayer( ModelRegistryManager.GetTextureFromIndex(EntityType.TRIDENT, 0)), false, tridentEntity.isEnchanted());
            ModelRegistryManager.GetModelFromIndex(EntityType.TRIDENT, 0).render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
            super.render(tridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
            ci.cancel();
        }
    }

    @Override
    public Identifier getTexture(TridentEntity entity) {
        return texture;
    }
}
