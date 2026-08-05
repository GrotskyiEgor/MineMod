package com.therootsofancientmagic.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;

public class ModBlock {
    public static final Block NEW_BLOCK = registerBlock(
        "new_block",
        new Block(AbstractBlock.Settings.copy(Blocks.STONE))
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
            entries.add(NEW_BLOCK);
        });
    }
}
