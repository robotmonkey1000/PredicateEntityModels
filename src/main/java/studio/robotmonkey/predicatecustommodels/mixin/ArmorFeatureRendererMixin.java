package studio.robotmonkey.predicatecustommodels.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.robotmonkey.predicatecustommodels.client.SimpleArmorModel;
import studio.robotmonkey.predicatecustommodels.util.ModelRegistryManager;
import studio.robotmonkey.predicatecustommodels.util.ModelsWithTexture;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {


    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method= "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V", cancellable = true, at = @At("HEAD"))
    private void renderArmorInject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if((itemStack.getItem() instanceof ArmorItem))
        {
            ArmorItem armorItem = (ArmorItem)itemStack.getItem();
            if (armorItem.getSlotType() == armorSlot) {
//                ((BipedEntityModel)this.getContextModel()).copyBipedStateTo(model);
//                boolean bl = this.usesInnerModel(armorSlot);
                boolean glint = itemStack.hasGlint();
                ModelsWithTexture modelT = ModelRegistryManager.GetModelFromStack(itemStack);
                if(modelT != null)
                {
                    //Apply rotation of the parents head TODO this for all pieces based on slot.


                    switch(armorSlot){
                        case HEAD:
                            ((SimpleArmorModel)modelT.model).model.copyTransform(this.getContextModel().head);
                            break;
                        case CHEST:
                            break;
                        case LEGS:
                            break;
                        case FEET:
                            break;
                    }

                    //this.renderArmorParts(matrices, vertexConsumers, light, armorItem, bl2, model, bl, 1.0F, 1.0F, 1.0F, (String)null);
                    VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, modelT.model.getLayer(modelT.texture), false, glint);
                    modelT.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                    ci.cancel();
                }

            }
        }
    }

    @Shadow
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

    }
}
