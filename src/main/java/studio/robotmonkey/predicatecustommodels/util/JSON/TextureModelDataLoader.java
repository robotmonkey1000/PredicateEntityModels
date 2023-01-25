package studio.robotmonkey.predicatecustommodels.util.JSON;

import com.google.gson.annotations.Expose;
import com.google.gson.internal.LinkedTreeMap;
import studio.robotmonkey.predicatecustommodels.util.UtilManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TextureModelDataLoader {

    public static TextureModel LoadFromJson(InputStream stream)
    {
        return UtilManager.getGson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), TextureModel.class);
    }



    public class TextureModel {
        @Expose public String texture;
        @Expose public int[] texturesize;
        @Expose public ModelGroup root;


    }

    public class ModelGroup {
        @Expose public String name;
        @Expose public ModelPivot pivot;

        @Expose public List<ModelGroup> children;

        @Expose public List<ModelCuboid> cuboids;
    }
    public class ModelPivot {
        @Expose public float pivotX;
        @Expose public float pivotY;
        @Expose public float pivotZ;
    }

    public class ModelCuboid {
        @Expose public CuboidUV UV;
        @Expose public CuboidSize size;
        @Expose public CuboidOffset offset;

    }

    public class CuboidUV {
        @Expose public int X;
        @Expose public int Y;
    }

    public class CuboidSize {
        @Expose public float sizeX;
        @Expose public float sizeY;
        @Expose public float sizeZ;
    }

    public class CuboidOffset {
        @Expose public float offsetX;
        @Expose public float offsetY;
        @Expose public float offsetZ;
    }

}
