package studio.robotmonkey.predicatecustommodels.util;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.util.Identifier;

public class ModelsWithTexture {

    public ModelsWithTexture(ModelPart model, Identifier texture)
    {
        this.modelPart = model;
        this.texture = texture;
    }

    public ModelPart modelPart;
    public Identifier texture;

    public Model model;

    public void SetModel(Model model)
    {
        this.model = model;
    }
}
