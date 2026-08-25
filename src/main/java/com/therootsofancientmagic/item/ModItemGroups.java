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

public class ModItemGroups {
    public static final ItemGroup TOOLS = Registry.register(
            Registries.ITEM_GROUP,
            TheRootsOfAncientMagic.id("roots_of_ancient_magic_tools"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(
                            "itemGroup.the-roots-of-ancient-magic.tools"
                    ))
                    .icon(() -> new ItemStack(ModItem.HEROIC_SWORD))
                    .entries((context, entries) -> {
                        entries.add(ModItem.FIRE_STAFF);
                        entries.add(ModItem.WEED_STAFF);
                        entries.add(ModItem.EARTH_STAFF);
                        entries.add(ModItem.AQUA_STAFF);    
                        entries.add(ModItem.LIGHT_STAFF); 
                        entries.add(ModItem.DARK_STAFF); 

                        entries.add(ModItem.BATTLE_AXE);
                        entries.add(ModItem.HEROIC_SWORD);
                        entries.add(ModItem.HEROIC_PICKAXE);
                        entries.add(ModItem.HEROIC_AXE);
                        entries.add(ModItem.HEROIC_SHOVEL);
                        entries.add(ModItem.HEROIC_HOE);

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

                        entries.add(ModItem.AQUA_ROBE);
                        entries.add(ModItem.FIRE_ROBE);
                        entries.add(ModItem.WEED_ROBE);
                        entries.add(ModItem.EARTH_ROBE);
                        
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

                        entries.add(ModItem.AQUA_NECKLACE);
                        entries.add(ModItem.FIRE_NECKLACE);
                        entries.add(ModItem.EARTH_NECKLACE);
                        entries.add(ModItem.WEED_NECKLACE);

                        entries.add(ModItem.AQUA_RING);
                        entries.add(ModItem.FIRE_RING);
                        entries.add(ModItem.WEED_RING);
                        entries.add(ModItem.EARTH_RING);
                    })
                    .build()
    );

    public static final ItemGroup MAGIC_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            TheRootsOfAncientMagic.id("roots_of_ancient_magic_magic"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(
                            "itemGroup.the-roots-of-ancient-magic.magic"
                    ))
                    .icon(() -> new ItemStack(ModItem.ESSENCE_FIRE))
                    .entries((context, entries) -> {
                        entries.add(ModItem.ESSENCE_AQUA);
                        entries.add(ModItem.ESSENCE_FIRE);
                        entries.add(ModItem.ESSENCE_WEED);
                        entries.add(ModItem.ESSENCE_EARTH);
                        entries.add(ModItem.ESSENCE_LIGHT);
                        entries.add(ModItem.ESSENCE_DARK);
                        entries.add(ModItem.ESSENCE_HEROIC);
                        
                        entries.add(ModBlock.FURNACE_POWDER);
                        entries.add(ModBlock.CRAFT_TABLE);
                        
                        entries.add(ModFlowerBlock.FLOWER_DARK);
                        entries.add(ModFlowerBlock.FLOWER_LIGHT);
                        entries.add(ModFlowerBlock.FLOWER_AQUA);
                        entries.add(ModFlowerBlock.FLOWER_FIRE);
                        entries.add(ModFlowerBlock.FLOWER_EARTH);
                        entries.add(ModFlowerBlock.FLOWER_WEED);
                    })
                    .build()
    );

    public static final ItemGroup ARMOR_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            TheRootsOfAncientMagic.id("roots_of_ancient_magic_armor"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(
                            "itemGroup.the-roots-of-ancient-magic.armor"
                    ))
                    .icon(() -> new ItemStack(ModItem.FIRE_BOOTS))
                    .entries((context, entries) -> {                                             
                       entries.add(ModItem.FIRE_BOOTS);                            
                    })
                    .build()
    );

    public static void registerItemGroups() {}
}