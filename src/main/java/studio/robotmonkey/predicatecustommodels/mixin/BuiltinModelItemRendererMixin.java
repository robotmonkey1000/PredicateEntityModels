package studio.robotmonkey.predicatecustommodels.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.robotmonkey.predicatecustommodels.util.ModelRegistryManager;
import studio.robotmonkey.predicatecustommodels.util.ModelsWithTexture;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Shadow
    private TridentEntityModel modelTrident;

    @Shadow
    private ShieldEntityModel modelShield;

    @Shadow
    private final EntityModelLoader entityModelLoader;

    public BuiltinModelItemRendererMixin(TridentEntityModel modelTrident, EntityModelLoader entityModelLoader) {
        this.modelTrident = modelTrident;
        this.entityModelLoader = entityModelLoader;
    }

    @Inject(method= "render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        //TODO MAKE SURE TO UPDATE THIS AFTER CHECKING PREDICATE

        if(stack.isOf(Items.TRIDENT))
        {
            ModelsWithTexture modelsWithTexture = ModelRegistryManager.GetModelFromStack(EntityType.TRIDENT, stack);
            if(modelsWithTexture != null)
            {
                this.modelTrident = (TridentEntityModel) modelsWithTexture.model;
            }
            else
            {
                this.modelTrident = new TridentEntityModel(this.entityModelLoader.getModelPart(EntityModelLayers.TRIDENT));
            }
        }
    }

    /*
        This method changes the BuildInModelItemRenderer call to get the texture and shine for the handheld trident model.
        If I fail to get the TEXTURE from the model manager fall back on the default models texture.
        TODO: Will need a condition check here to pull the correct texture based ont the item stack
     */
    @Redirect(method="render", at = @At(value="INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getDirectItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer GetConsumer(VertexConsumerProvider vertexConsumers, RenderLayer layer, boolean solid, boolean glint, ItemStack stack){

        //TODO DO RESOURCE CHECK TO SEE IF VALID IDENTIFIER FOR TEXTURE, OTHERWISE WILL CRASH
        if(stack.isOf(Items.TRIDENT))
        {
            ModelsWithTexture model = ModelRegistryManager.GetModelFromStack(EntityType.TRIDENT, stack);
            if (model != null)
            {
                RenderLayer newLayer = this.modelTrident.getLayer(ModelRegistryManager.GetTextureFromIndex(EntityType.TRIDENT, 0));
                return ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, newLayer, false, stack.hasGlint());
            }
        } else if(stack.isOf(Items.SHIELD))
        {
            boolean bl = BlockItem.getBlockEntityNbt(stack) != null;
            SpriteIdentifier spriteIdentifier = bl ? ModelLoader.SHIELD_BASE : ModelLoader.SHIELD_BASE_NO_PATTERN;
            return ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint());
        }

        return ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.modelTrident.getLayer(TridentEntityModel.TEXTURE), false, glint);
    }
}
