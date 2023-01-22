package studio.robotmonkey.predicatecustommodels.util;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import studio.robotmonkey.predicatecustommodels.file.ModelFile;
import studio.robotmonkey.predicatecustommodels.util.Conditions.Condition;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelRegistryManager {
    //A map between the entity type and a list of custom models, make this so it contains predicates to get updated models
    private static final HashMap<EntityType<? extends Entity>, ModelFile> EntityReigstry = new HashMap<>(); //TODO MAKE THIS NOT JUST ONE JEM FILE
    private static final HashMap<EntityType<? extends Entity>, HashMap<Condition, ModelsWithTexture>> EntityModelRegistry = new HashMap<>();
    public static void addRegistry(EntityType<? extends Entity> entityType, ModelFile file){
        if(file != null){
            EntityReigstry.put(entityType, file);
            EntityModelRegistry.put(entityType, GenerateModelsFromFile(entityType, file));
        }
    }

    public static HashMap<Condition, ModelsWithTexture> GenerateModelsFromFile(EntityType<? extends Entity> entityType, ModelFile file)
    {
        HashMap<Condition, ModelsWithTexture> conditionalModels = file.GetConditionalModels();

        for(ModelsWithTexture modelsWithTexture: conditionalModels.values())
        {
            if(entityType == EntityType.TRIDENT)
            {
                modelsWithTexture.model = new TridentEntityModel(modelsWithTexture.modelPart);
            }
        }

        return conditionalModels;
    }
    //Function to see if an entity contains a custom model in the registry
    public static boolean hasEntity(EntityType<? extends Entity> entityType){
        return EntityReigstry.containsKey(entityType);
    }

    public static Model GetModelFromIndex(EntityType<? extends Entity> type, int index)
    {
        if (hasEntity(type))
        {
            int i = 0;
            for(ModelsWithTexture model : EntityModelRegistry.get(type).values())
            {
                if(i == index) return model.model;
                i++;
            }
        }
        return null;
    }

    public static Identifier GetTextureFromIndex(EntityType<? extends Entity> type, int index)
    {
        if (hasEntity(type))
        {
            int i = 0;
            for(ModelsWithTexture model : EntityModelRegistry.get(type).values())
            {
                if(i == index) return model.texture;
                i++;
            }
        }
        return null;
    }

//    public static CemModelRegistry getRegistry(EntityType<? extends Entity> entityType){
//        return new CemModelRegistry(EntityReigstry.get(entityType));
//    }

    public static void clearRegistries(){
        EntityReigstry.clear();
    }


}
