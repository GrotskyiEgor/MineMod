package com.therootsofancientmagic.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.block.AbstractBlock;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffects;

public class ModFlowerBlock {
    public static void RenderFlowers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_DARK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_LIGHT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_AQUA, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_FIRE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_EARTH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_WEED, RenderLayer.getCutout());
    }

    public static final Block FLOWER_DARK = registerBlock(
        "flower_dark",
        new FlowerBlock(
            StatusEffects.LUCK,
            0,
            AbstractBlock.Settings.create()
                .noCollision()      
                .breakInstantly()   
                .nonOpaque()
        )
    );

    public static final Block FLOWER_LIGHT = registerBlock(
        "flower_light",
        new FlowerBlock(
            StatusEffects.LUCK,
            0,
            AbstractBlock.Settings.create()
                .noCollision()      
                .breakInstantly()
                .nonOpaque() 
        )
    );

    public static final Block FLOWER_AQUA = registerBlock(
        "flower_aqua",
        new FlowerBlock(
            StatusEffects.LUCK,
            0,
            AbstractBlock.Settings.create()
                .noCollision()      
                .breakInstantly()
                .nonOpaque() 
        )
    );

    public static final Block FLOWER_FIRE = registerBlock(
        "flower_fire",
        new FlowerBlock(
            StatusEffects.LUCK,
            0,
            AbstractBlock.Settings.create()
                .noCollision()      
                .breakInstantly()   
                .nonOpaque()
        )
    );

    public static final Block FLOWER_EARTH = registerBlock(
        "flower_earth",
        new FlowerBlock(
            StatusEffects.LUCK,
            0,
            AbstractBlock.Settings.create()
                .noCollision()      
                .breakInstantly()   
                .nonOpaque()
        )
    );

    public static final Block FLOWER_WEED = registerBlock(
        "flower_weed",
        new FlowerBlock(
            StatusEffects.LUCK,
            0,
            AbstractBlock.Settings.create()
                .noCollision()      
                .breakInstantly()   
                .nonOpaque()
        )
    );
        
    public static Block registerBlock(String name, Block block){
        Registry.register(
            Registries.BLOCK, 
            Identifier.of("the-roots-of-ancient-magic", name),
            block
        );
        
        Registry.register(
            Registries.ITEM, 
            Identifier.of("the-roots-of-ancient-magic", name),
            new BlockItem(block, new Item.Settings())
        );

        return block;
    }

    public static void registerModBlocks(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
            entries.add(FLOWER_DARK);
            entries.add(FLOWER_LIGHT);
            entries.add(FLOWER_AQUA);
            entries.add(FLOWER_FIRE);
            entries.add(FLOWER_EARTH);
            entries.add(FLOWER_WEED);
        });
    }
}
