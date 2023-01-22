package studio.robotmonkey.predicatecustommodels.mixin;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import studio.robotmonkey.predicatecustommodels.util.ModelRegistryManager;

@Mixin(TridentEntityModel.class)
public class TridentTexturedModelDataMixin {

    @Inject(method= "getTexturedModelData()Lnet/minecraft/client/model/TexturedModelData;", at = @At("HEAD"), cancellable = true)
    private static void getTextureModelData(CallbackInfoReturnable<TexturedModelData> cir)
    {
        //cir.setReturnValue(ModelRegistryManager.GetModelFromIndex(EntityType.TRIDENT, 0));
        //cir.cancel();

    }

}
