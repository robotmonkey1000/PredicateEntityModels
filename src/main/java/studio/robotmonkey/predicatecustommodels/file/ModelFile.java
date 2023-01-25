package studio.robotmonkey.predicatecustommodels.file;

import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.client.model.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import studio.robotmonkey.predicatecustommodels.util.Conditions.Condition;
import studio.robotmonkey.predicatecustommodels.util.Conditions.JSONModelFile;
import studio.robotmonkey.predicatecustommodels.util.JSON.TextureModelDataLoader;
import studio.robotmonkey.predicatecustommodels.util.ModelsWithTexture;
import studio.robotmonkey.predicatecustommodels.util.UtilManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ModelFile {
    public static final Pattern allowTextureChars = Pattern.compile("^[a-z0-9/._\\-]+$");
    //private final Identifier texture;
    //private final ArrayList<Double> textureSize;
    //private final Float shadowsize;
    private final ArrayList<TextureModelDataLoader.TextureModel> textureModels;
    private final HashMap<Condition, ModelsWithTexture> texturedModelData;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ModelFile(JSONModelFile modelFile, ResourceManager resourceManager) throws Exception{

        this.textureModels = new ArrayList<TextureModelDataLoader.TextureModel>();
        this.texturedModelData = new HashMap<>();
        for(Condition condition : modelFile.Conditions)
        {
            UtilManager.getLogger().info(condition.model);

            //TODO VALIDATE FILE
            Identifier identifier = new Identifier("minecraft", "models/" + condition.model);
            UtilManager.getLogger().info(identifier);

            if(resourceManager.getResource(identifier).isPresent())
            {
                try(InputStream stream = resourceManager.getResource(identifier).get().getInputStream()) {
                    TextureModelDataLoader.TextureModel textureModel = TextureModelDataLoader.LoadFromJson(stream);
                    UtilManager.getLogger().info("Model Data Below");
                    UtilManager.getLogger().info(textureModel.texture);
                    textureModels.add(textureModel);
                    ModelPart modelPart = CreateTexturedModelData(condition, textureModel);
                } catch (Exception ex)
                {
                    UtilManager.getLogger().error(ex);
                }
            }


        }
    }

    public HashMap<Condition, ModelsWithTexture> GetConditionalModels() {
        return this.texturedModelData;
    }
    public ModelPart CreateTexturedModelData(Condition condition, TextureModelDataLoader.TextureModel textureModel) {
        ModelData modelData = new ModelData();
        ModelPartData rootpart = modelData.getRoot();
        ModelPartBuilder builder = ModelPartBuilder.create();
        GatherChildenModels(textureModel.root, rootpart);

        //TODO ADD TEXTURE SIZE AS PART OF THE MODEL INFORMATION
        ModelPart model = TexturedModelData.of(modelData, textureModel.texturesize[0], textureModel.texturesize[1]).createModel();
        texturedModelData.put(condition, new ModelsWithTexture(model, new Identifier("minecraft", "textures/" + textureModel.texture)));
        return model;
    }

    public ModelPart GetFromIndex(int index)
    {
        return texturedModelData.get(index).modelPart;
    }

    private void GatherChildenModels(TextureModelDataLoader.ModelGroup group, ModelPartData parent)
    {

        ModelPartBuilder builder = ModelPartBuilder.create();
        for(int i = 0; i < group.cuboids.size(); i++)
        {
            TextureModelDataLoader.CuboidUV UV = group.cuboids.get(i).UV;
            TextureModelDataLoader.CuboidOffset offset = group.cuboids.get(i).offset;
            TextureModelDataLoader.CuboidSize size = group.cuboids.get(i).size;
            builder.uv(UV.X, UV.Y).cuboid(offset.offsetX, offset.offsetY, offset.offsetZ, size.sizeX, size.sizeY, size.sizeZ, new Dilation(0)); //TODO CONSIDER PIVOT AND ROTATION

        }

        ModelPartData current = parent.addChild(group.name, builder, ModelTransform.pivot(group.pivot.pivotX, group.pivot.pivotY, group.pivot.pivotZ));
        if(group.children != null)
        {
            for(TextureModelDataLoader.ModelGroup child : group.children){
                GatherChildenModels(child, current);
            }
        }

    }
}
