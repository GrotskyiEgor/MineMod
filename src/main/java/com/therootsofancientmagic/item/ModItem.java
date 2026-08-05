package com.therootsofancientmagic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;

public class ModItem{
    public static final Item ESSENCE_AQUA = registerItem(
        "essence_aqua",
        new Item(new Item.Settings())
    );

    public static final Item ESSENCE_FIRE = registerItem(
        "essence_fire",
        new Item(new Item.Settings())
    );

    public static final Item ESSENCE_WEED = registerItem(
        "essence_weed",
        new Item(new Item.Settings())
    );

    public static final Item ESSENCE_EARTH = registerItem(
        "essence_earth",
        new Item(new Item.Settings())
    );

    public static final Item ESSENCE_LIGHT = registerItem(
        "essence_light",
        new Item(new Item.Settings())
    );

    public static final Item ESSENCE_DARK = registerItem(
        "essence_dark",
        new Item(new Item.Settings())
    );

    public static final Item FIRE_STAFF = registerItem(
        "fire_staff",
        new Item(new Item.Settings())
    );


    // public static final Item FLOWER_AQUA = registerItem(
    //     "flower_aqua",
    //     new Item(new Item.Settings())
    // );
    
    // public static final Item FLOWER_FIRE = registerItem(
    //     "flower_fire",
    //     new Item(new Item.Settings())
    // );

    // public static final Item FLOWER_WEED = registerItem(
    //     "flower_weed",
    //     new Item(new Item.Settings())
    // );
    
    // public static final Item FLOWER_EARTH = registerItem(
    //     "flower_earth",
    //     new Item(new Item.Settings())
    // );

    // public static final Item FLOWER_LIGHT = registerItem(
    //     "flower_light",
    //     new Item(new Item.Settings())
    // );
    
    // public static final Item FLOWER_DARK = registerItem(
    //     "flower_dark",
    //     new Item(new Item.Settings())
    // );
    
    public static Item registerItem(String name, Item item){
        return Registry.register(
            Registries.ITEM, 
            new Identifier("the-roots-of-ancient-magic", name),
            item
        );
    }

    public static void registerModItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
            entries.add(ESSENCE_AQUA);
            entries.add(ESSENCE_FIRE);
            entries.add(ESSENCE_WEED);
            entries.add(ESSENCE_EARTH);
            entries.add(ESSENCE_LIGHT);
            entries.add(ESSENCE_DARK);

            entries.add(FIRE_STAFF);
        });
    }
}