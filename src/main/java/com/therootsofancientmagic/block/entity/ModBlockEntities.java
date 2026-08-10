package com.therootsofancientmagic.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.block.ModBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<FurnacePowderBlockEntity> FURNACE_POWDER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(TheRootsOfAncientMagic.MOD_ID, "furnace_powder_be"),
                    FabricBlockEntityTypeBuilder.create(FurnacePowderBlockEntity::new, ModBlock.FURNACE_POWDER).build());

    public static final BlockEntityType<CraftTableBlockEntity> CRAFT_TABLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(TheRootsOfAncientMagic.MOD_ID, "craft_table_be"),
                    FabricBlockEntityTypeBuilder.create(CraftTableBlockEntity::new, ModBlock.CRAFT_TABLE).build());

    public static void registerBlockEntities() {
        TheRootsOfAncientMagic.LOGGER.info("Registering Block Entities for " + TheRootsOfAncientMagic.MOD_ID);
    }
}