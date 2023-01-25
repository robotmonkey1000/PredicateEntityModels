package studio.robotmonkey.predicatecustommodels.util;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import studio.robotmonkey.predicatecustommodels.client.SimpleArmorModel;
import studio.robotmonkey.predicatecustommodels.file.ModelFile;
import studio.robotmonkey.predicatecustommodels.util.Conditions.Condition;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ModelRegistryManager {
    //A map between the entity type and a list of custom models, make this so it contains predicates to get updated models
    private static final HashMap<EntityType<? extends Entity>, ModelFile> EntityReigstry = new HashMap<>();
    private static final HashMap<Item, ModelFile> OtherRegistry = new HashMap<>();
    private static final HashMap<EntityType<? extends Entity>, HashMap<Condition, ModelsWithTexture>> EntityModelRegistry = new HashMap<>();
    private static final HashMap<Item, HashMap<Condition, ModelsWithTexture>> OtherModelRegistry = new HashMap<>();
    public static void addRegistry(EntityType<? extends Entity> entityType, ModelFile file){
        if(file != null){
            EntityReigstry.put(entityType, file);
            EntityModelRegistry.put(entityType, GenerateModelsFromFile(entityType, file));
        }
    }

    public static void addOther(Item item, ModelFile file)
    {
        if(file != null)
        {
            OtherRegistry.put(item, file);
            OtherModelRegistry.put(item, GenerateModelsFromFile(item, file));
        }
    }
    public static HashMap<Condition, ModelsWithTexture> GenerateModelsFromFile(Item item, ModelFile file)
    {
        HashMap<Condition, ModelsWithTexture> conditionalModels = file.GetConditionalModels();
        for(ModelsWithTexture modelsWithTexture: conditionalModels.values())
        {
            if(item instanceof ArmorItem)
            {
                modelsWithTexture.model = new SimpleArmorModel(modelsWithTexture.modelPart);
            }
        }

        return conditionalModels;
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

    public static boolean hasOther(Item id){
        return OtherRegistry.containsKey(id);
    }

    public static ModelsWithTexture GetModelFromIndex(EntityType<? extends Entity> type, int index)
    {
        if (hasEntity(type))
        {
            int i = 0;
            for(ModelsWithTexture model : EntityModelRegistry.get(type).values())
            {
                if(i == index) return model;
                i++;
            }
        }
        return null;
    }


    public static ModelsWithTexture GetModelFromIndex(Item item, int index)
    {
        if (hasOther(item))
        {
            int i = 0;
            for(ModelsWithTexture model : OtherModelRegistry.get(item).values())
            {
                if(i == index) return model;
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

    public static ModelsWithTexture GetModelFromStack(ItemStack stack)
    {
        if(stack.getNbt() != null && hasOther(stack.getItem()))
        {
            for(Condition condition: OtherModelRegistry.get(stack.getItem()).keySet())
            {
                if(stack.getNbt().contains("CustomModelData"))
                {
//                    UtilManager.getLogger().info("Found Custom Model Data");
                    int modelData = stack.getNbt().getInt("CustomModelData");
                    //TODO ADD OTHER CONDITIONS
//                    UtilManager.getLogger().info(modelData);
                    if(condition.ConditionMet("CustomModelData", Integer.toString(modelData))) return  OtherModelRegistry.get(stack.getItem()).get(condition);
                }
            }
        }

        return null;
    }

    public static ModelsWithTexture GetModelFromStack(EntityType<? extends Entity> type, ItemStack stack)
    {

        if(stack.getNbt() != null && hasEntity(type))
        {
            for(Condition condition: EntityModelRegistry.get(type).keySet())
            {
                if(stack.getNbt().contains("CustomModelData"))
                {
//                    UtilManager.getLogger().info("Found Custom Model Data");
                    int modelData = stack.getNbt().getInt("CustomModelData");
                    //TODO ADD OTHER CONDITIONS
//                    UtilManager.getLogger().info(modelData);
                    if(condition.ConditionMet("CustomModelData", Integer.toString(modelData))) return  EntityModelRegistry.get(type).get(condition);
                }
            }
        }
        return null;
    }

    public static ModelsWithTexture GetModelFromEntity(EntityType<? extends Entity> type, TridentEntity entity) {
        if(entity == null || !hasEntity(type)) return null;

        for(Condition condition: EntityModelRegistry.get(type).keySet())
        {
            if(entity.getOwner() != null) {
                if(condition.ConditionMet("Owner", entity.getOwner().getName().getString()))
                {
                    return EntityModelRegistry.get(type).get(condition);
                }
            }
        }
        return null;
    }


    public static void clearRegistries(){
        EntityReigstry.clear();
        OtherRegistry.clear();
        EntityModelRegistry.clear();
        OtherModelRegistry.clear();
    }


}
