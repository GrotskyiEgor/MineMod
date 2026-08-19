package com.therootsofancientmagic.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
// import net.minecraft.item.ItemGroups;
import net.minecraft.item.PickaxeItem;
// import net.minecraft.item.ToolMaterials;

import com.therootsofancientmagic.item.necklace.NecklaceItem;
import com.therootsofancientmagic.item.ring.RingItem;
import com.therootsofancientmagic.item.robe.RobeItem;

// import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

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


    public static final Item WIZARD_ROBE = registerItem(
        "wizard_robe", 
        new RobeItem(new Item.Settings().maxCount(1)));

    public static final Item MAGIC_NECKLACE = registerItem(
        "magic_necklace",
        new NecklaceItem(new FabricItemSettings().maxCount(1)));
    public static final Item MAGIC_RING = registerItem(
        "magic_ring",
        new RingItem(new FabricItemSettings().maxCount(1)));
   
    public static final Item FIRE_PICKAXE = registerItem("fire_pickaxe",
            new PickaxeItem(ModToolMaturial.ESSENCE_FIRE, 1, -2.8f, new Item.Settings()));

    public static final Item AQUA_PICKAXE = registerItem("aqua_pickaxe",
            new PickaxeItem(ModToolMaturial.ESSENCE_AQUA, 1, -2.8f, new Item.Settings()));

    public static final Item WEED_PICKAXE = registerItem("weed_pickaxe",
            new PickaxeItem(ModToolMaturial.ESSENCE_WEED, 1, -2.8f, new Item.Settings()));
            
    public static final Item DARK_PICKAXE = registerItem("dark_pickaxe",
            new PickaxeItem(ModToolMaturial.ESSENCE_DARK, 1, -2.8f, new Item.Settings()));
            
    public static final Item LIGHT_PICKAXE = registerItem("light_pickaxe",
            new PickaxeItem(ModToolMaturial.ESSENCE_LIGHT, 1, -2.8f, new Item.Settings()));
            
    public static final Item EARTH_PICKAXE = registerItem("earth_pickaxe",
            new PickaxeItem(ModToolMaturial.ESSENCE_EARTH, 1, -2.8f, new Item.Settings()));

    public static Item registerItem(String name, Item item){
        return Registry.register(
            Registries.ITEM, 
            new Identifier("the-roots-of-ancient-magic", name),
            item
        );
    }

    public static void registerModItems(){
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
        //     entries.add(ESSENCE_AQUA);
        //     entries.add(ESSENCE_FIRE);
        //     entries.add(ESSENCE_WEED);
        //     entries.add(ESSENCE_EARTH);
        //     entries.add(ESSENCE_LIGHT);
        //     entries.add(ESSENCE_DARK);
            
        //     entries.add(FIRE_ESSENCE_PICKAXE);
        //     entries.add(AQUA_ESSENCE_PICKAXE);
        //     entries.add(WEED_ESSENCE_PICKAXE);
        //     entries.add(DARK_ESSENCE_PICKAXE);
        //     entries.add(LIGHT_ESSENCE_PICKAXE);
        //     entries.add(EARTH_ESSENCE_PICKAXE);
        // });
    }
}