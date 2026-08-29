package com.therootsofancientmagic.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.block.AbstractBlock;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.entity.effect.StatusEffects;

public class ModFlowerBlock {
    public static void RenderFlowers() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_DARK, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_LIGHT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_AQUA, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_FIRE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_EARTH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFlowerBlock.FLOWER_AIR, RenderLayer.getCutout());
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

    // public static final Block FLOWER_DARK_POT

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
        ) {
            @Override
            protected boolean canPlantOnTop(
                    BlockState floor,
                    BlockView world,
                    BlockPos pos
            ) {
                return floor.isOf(Blocks.SAND)
                        || floor.isOf(Blocks.RED_SAND)
                        || super.canPlantOnTop(floor, world, pos);
            }
        }
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

    public static final Block FLOWER_AIR = registerBlock(
        "flower_air",
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
    }
}
