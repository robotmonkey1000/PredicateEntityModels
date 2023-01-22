package studio.robotmonkey.predicatecustommodels.mixin;

import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.robotmonkey.predicatecustommodels.file.ModelFile;
import studio.robotmonkey.predicatecustommodels.util.Conditions.Condition;
import studio.robotmonkey.predicatecustommodels.util.Conditions.JSONModelFile;
import studio.robotmonkey.predicatecustommodels.util.ModelRegistryManager;
import studio.robotmonkey.predicatecustommodels.util.UtilManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

@Mixin(EntityModelLoader.class)
public abstract class EntityModelLoaderMixin{
    @Inject(method = "reload", at = @At("HEAD"))
    private void reloadMixin(ResourceManager manager, CallbackInfo ci){

        //Clear out loaded models
        ModelRegistryManager.clearRegistries();
        UtilManager.getLogger().info("Testing");
        //check robotmonkey folder for files that end with .rem
        manager.findResources("cem", path -> path.getPath().endsWith(".rem")).forEach((id, resource) -> loadResourceFromId(manager, id, "robotmonkey"));
    }

    private void loadResourceFromId(ResourceManager manager, Identifier id, String namespace){
        if(!id.getNamespace().equals(namespace)){
            return;
        }
        UtilManager.getLogger().info(id.toString());
        try(InputStream stream = manager.getResource(id).get().getInputStream()){
            //initialize the file
            JSONModelFile jsonModelFile = UtilManager.getGson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JSONModelFile.class);
            if(jsonModelFile == null){
                throw new Exception("Invalid File");
            }
            ModelFile file = new ModelFile(jsonModelFile, id, manager);

            String entityName = UtilManager.getEntityNameFromId(id);
            Optional<EntityType<?>> entityTypeOptional = EntityType.get(entityName);

            if(entityTypeOptional.isPresent()){
                EntityType<? extends Entity> entityType = entityTypeOptional.get();
                if(UtilManager.isUnsupported(entityType)){
                    throw new Exception("Entity \"" + EntityType.getId(entityType) + "\" is unsupported!");
                }

                UtilManager.getLogger().info("Adding Entity " + entityType);
                ModelRegistryManager.addRegistry(entityType, file);
            }
        } catch(Exception exception){
            UtilManager.getLogger().error(exception);
//            UtilManager.getLogger().error("Error parsing " + id + ":");
//            String message = exception.getMessage();
//            UtilManager.getLogger().error(exception);
//            if(message == null || message.trim().equals("")){
//                UtilManager.getLogger().error(exception.getStackTrace()[0]);
//                UtilManager.getLogger().error(exception.getStackTrace()[1]);
//                UtilManager.getLogger().error(exception.getStackTrace()[2]);
//            }
        }
    }
}