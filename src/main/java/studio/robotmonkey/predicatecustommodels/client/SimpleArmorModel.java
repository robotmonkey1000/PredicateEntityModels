package studio.robotmonkey.predicatecustommodels.client;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class SimpleArmorModel extends Model {
    public ModelPart model;
    public SimpleArmorModel(ModelPart part) {
        super(RenderLayer::getArmorCutoutNoCull);
        this.model = part;
    }

    public SimpleArmorModel(Function<Identifier, RenderLayer> layerFactory) {
        super(layerFactory);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.model.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
