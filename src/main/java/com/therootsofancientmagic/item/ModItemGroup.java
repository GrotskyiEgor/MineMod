package com.therootsofancientmagic.item;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.block.ModBlock;
import com.therootsofancientmagic.block.ModFlowerBlock;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroup {

    public static final ItemGroup ROOTS_OF_ANCIENT_MAGIC = Registry.register(
            Registries.ITEM_GROUP,
            TheRootsOfAncientMagic.id("roots_of_ancient_magic"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(
                            "itemGroup.the-roots-of-ancient-magic"
                    ))
                    .icon(() -> new ItemStack(ModFlowerBlock.FLOWER_DARK))
                    .entries((context, entries) -> {
                        entries.add(ModItem.FIRE_SFAFF);

                        entries.add(ModItem.ESSENCE_AQUA);
                        entries.add(ModItem.ESSENCE_FIRE);
                        entries.add(ModItem.ESSENCE_WEED);
                        entries.add(ModItem.ESSENCE_EARTH);
                        entries.add(ModItem.ESSENCE_LIGHT);
                        entries.add(ModItem.ESSENCE_DARK);
                        
                        entries.add(ModBlock.FURNACE_POWDER);
                        entries.add(ModBlock.CRAFT_TABLE);
                        
                        entries.add(ModFlowerBlock.FLOWER_DARK);
                        entries.add(ModFlowerBlock.FLOWER_LIGHT);
                        entries.add(ModFlowerBlock.FLOWER_AQUA);
                        entries.add(ModFlowerBlock.FLOWER_FIRE);
                        entries.add(ModFlowerBlock.FLOWER_EARTH);
                        entries.add(ModFlowerBlock.FLOWER_WEED);

                        entries.add(ModItem.FIRE_PICKAXE);
                        entries.add(ModItem.AQUA_PICKAXE);
                        entries.add(ModItem.WEED_PICKAXE);
                        entries.add(ModItem.DARK_PICKAXE);
                        entries.add(ModItem.LIGHT_PICKAXE);
                        entries.add(ModItem.EARTH_PICKAXE);

                        entries.add(ModItem.FIRE_AXE);
                        entries.add(ModItem.AQUA_AXE);
                        entries.add(ModItem.WEED_AXE);
                        entries.add(ModItem.DARK_AXE);
                        entries.add(ModItem.LIGHT_AXE);
                        entries.add(ModItem.EARTH_AXE);

                        entries.add(ModItem.FIRE_SWORD);
                        entries.add(ModItem.AQUA_SWORD);
                        entries.add(ModItem.WEED_SWORD);
                        entries.add(ModItem.DARK_SWORD);
                        entries.add(ModItem.LIGHT_SWORD);
                        entries.add(ModItem.EARTH_SWORD);

                        entries.add(ModItem.FIRE_SHOVEL);
                        entries.add(ModItem.AQUA_SHOVEL);
                        entries.add(ModItem.WEED_SHOVEL);
                        entries.add(ModItem.DARK_SHOVEL);
                        entries.add(ModItem.LIGHT_SHOVEL);
                        entries.add(ModItem.EARTH_SHOVEL);

                        entries.add(ModItem.FIRE_HOE);
                        entries.add(ModItem.AQUA_HOE);
                        entries.add(ModItem.WEED_HOE);
                        entries.add(ModItem.DARK_HOE);
                        entries.add(ModItem.LIGHT_HOE);
                        entries.add(ModItem.EARTH_HOE);
                    })
                    .build()
    );

    public static void registerItemGroups() {}
}