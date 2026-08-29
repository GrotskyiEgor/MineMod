package com.therootsofancientmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock;
import com.therootsofancientmagic.block.custom.FurnacePowderBlock;
import com.therootsofancientmagic.block.custom.CraftTableBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlock {
    public static final Block NEW_BLOCK = registerBlock(
        "new_block",
        new Block(AbstractBlock.Settings.copy(Blocks.STONE))
    );

    public static final Block FURNACE_POWDER = registerBlock("furnace_powder",
        new FurnacePowderBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque())
    );

    public static final Block CRAFT_TABLE = registerBlock("craft_table",
        new CraftTableBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).nonOpaque())
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
