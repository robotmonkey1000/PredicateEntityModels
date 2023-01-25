package studio.robotmonkey.predicatecustommodels.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.EntityType;
import studio.robotmonkey.predicatecustommodels.util.UtilManager;

@Environment(EnvType.CLIENT)
public class PredicateCustomModelsClient implements ClientModInitializer {

    //TODO ADD LOGGER
    @Override
    public void onInitializeClient() {
        UtilManager.addSupport(EntityType.TRIDENT);
        UtilManager.addSupport("diamond_helmet");
        UtilManager.addSupport("netherite_helmet");
        UtilManager.addSupport("iron_helmet");
        UtilManager.addSupport("gold_helmet");

    }


}
