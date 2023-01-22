package studio.robotmonkey.predicatecustommodels.mixin;

import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntityModel.class)
public class TridentEntityModelMixin {

    //NOTE: THIS WILL CHANGE THE TEXTURE FOR ALL MODELS
    @Accessor("TEXTURE")
    public static void setTexture(Identifier identifier)
    {
        throw new AssertionError();
    }
}
