package com.therootsofancientmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.therootsofancientmagic.biome.ModBiomes;
import com.therootsofancientmagic.block.ModBlock;
import com.therootsofancientmagic.block.ModFlowerBlock;
import com.therootsofancientmagic.block.entity.ModBlockEntities;
import com.therootsofancientmagic.entity.ModEntities;
import com.therootsofancientmagic.init.worldgen.PlacedFeatureInit;
import com.therootsofancientmagic.item.ModItem;
import com.therootsofancientmagic.item.ModItemGroup;
import com.therootsofancientmagic.network.RobeAbilityServerHandler;
import com.therootsofancientmagic.recipe.ModRecipes;
import com.therootsofancientmagic.screen.CraftTableScreen;
import com.therootsofancientmagic.screen.FurnacePowderScreen;
import com.therootsofancientmagic.screen.ModScreenHandlers;
import com.therootsofancientmagic.util.AccessoryHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
// import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class TheRootsOfAncientMagic implements ModInitializer {
    public static final String MOD_ID = "the-roots-of-ancient-magic";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        
        com.therootsofancientmagic.network.ModMessages.registerC2SPackets();
        ModItemGroup.registerItemGroups();


        ModItem.registerModItems();
        ModBlock.registerModBlocks();
        ModFlowerBlock.registerModBlocks();
        ModFlowerBlock.RenderFlowers();
        ModRecipes.registerRecipes();

        ModEntities.registerModEntities();
        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerScreenHandlers();

        RobeAbilityServerHandler.register();

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                AccessoryHandler.applyAccessoryEffects(player);
                com.therootsofancientmagic.mana.PlayerMana.regenerateMana(player);
            }
        });
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            net.minecraft.server.network.ServerPlayerEntity player = handler.getPlayer();
            if (player instanceof com.therootsofancientmagic.util.IEntityDataSaver dataSaver) {
                int currentMana = dataSaver.getPersistentData().getInt("mana");
                com.therootsofancientmagic.network.ModMessages.sendToClient(player, currentMana);
            }
        });

        HandledScreens.register(ModScreenHandlers.FURNACE_POWDER_SCREEN_HANDLER, FurnacePowderScreen::new);
        HandledScreens.register(ModScreenHandlers.CRAFT_TABLE_SCREEN_HANDLER, CraftTableScreen::new);
        
        
	    registerFlowers();
    }

    
    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
    
    public void registerFlowers() {
        registerFlowerInBiome(PlacedFeatureInit.FLOWER_DARK_PLACED_KEY, ModBiomes.DARK);
        registerFlowerInBiome(PlacedFeatureInit.FLOWER_LIGHT_PLACED_KEY, ModBiomes.LIGHT);
        registerFlowerInBiome(PlacedFeatureInit.FLOWER_WEED_PLACED_KEY, ModBiomes.WEED);
        registerFlowerInBiome(PlacedFeatureInit.FLOWER_EARTH_PLACED_KEY, ModBiomes.EARTH);
        registerFlowerInBiome(PlacedFeatureInit.FLOWER_FIRE_PLACED_KEY, ModBiomes.FIRE);
        registerFlowerInBiome(PlacedFeatureInit.FLOWER_AQUA_PLACED_KEY, ModBiomes.AQUA);
    }

    private void registerFlowerInBiome(RegistryKey<PlacedFeature> flowerKey, RegistryKey<Biome> biomeKey) {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(biomeKey),
                GenerationStep.Feature.VEGETAL_DECORATION,
                flowerKey
        );
    }
}