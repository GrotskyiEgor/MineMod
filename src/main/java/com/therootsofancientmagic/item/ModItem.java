package com.therootsofancientmagic.item;
import net.minecraft.item.Item;
// import net.minecraft.item.ItemGroups;
import net.minecraft.item.PickaxeItem;
// import net.minecraft.item.ToolMaterials;
import com.therootsofancientmagic.item.robe.RobeItem;
// import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import com.therootsofancientmagic.item.sword.ElementalSwordItem;
import com.therootsofancientmagic.item.pickaxe.ElementalPickaxeItem;
import com.therootsofancientmagic.item.shovel.ElementalShovelItem;
import com.therootsofancientmagic.item.axe.ElementalAxeItem;
import com.therootsofancientmagic.item.hoe.ElementalHoeItem;
import com.therootsofancientmagic.item.heroic_tools.HeroicAxeItem;
import com.therootsofancientmagic.item.heroic_tools.HeroicSwordItem;
import com.therootsofancientmagic.item.staff.FireStaff;
import com.therootsofancientmagic.item.staff.WeedStaff;
import net.minecraft.util.Identifier;

public class ModItem {
    public static final Item FIRE_STAFF = registerItem(
        "fire_staff",
        new FireStaff(new Item.Settings())
    );

    public static final Item WEED_STAFF = registerItem(
        "weed_staff",
        new WeedStaff(new Item.Settings())
    );

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
    public static final Item ESSENCE_HEROIC = registerItem(
        "essence_heroic",
        new Item(new Item.Settings())
    );

    public static final Item WIZARD_ROBE = registerItem(
        "wizard_robe", 
        new RobeItem(new Item.Settings().maxCount(1)));

    public static final Item HEROIC_AXE = registerItem("heroic_axe",
            new HeroicAxeItem(ModToolMaturial.ESSENCE_HEROIC, 3.0f, -2.4f, new Item.Settings()));
    public static final Item HEROIC_SWORD = registerItem("heroic_sword",
            new HeroicSwordItem(ModToolMaturial.ESSENCE_HEROIC, -3, -3.0f, new Item.Settings()));

    public static final Item FIRE_PICKAXE = registerItem("fire_pickaxe",
            new ElementalPickaxeItem(ModToolMaturial.ESSENCE_FIRE, 1, -2.8f, new Item.Settings()));

    public static final Item AQUA_PICKAXE = registerItem("aqua_pickaxe",
            new ElementalPickaxeItem(ModToolMaturial.ESSENCE_AQUA, 1, -2.8f, new Item.Settings()));

    public static final Item WEED_PICKAXE = registerItem("weed_pickaxe",
            new ElementalPickaxeItem(ModToolMaturial.ESSENCE_WEED, 1, -2.8f, new Item.Settings()));
            
    public static final Item EARTH_PICKAXE = registerItem("earth_pickaxe",
            new ElementalPickaxeItem(ModToolMaturial.ESSENCE_EARTH, 1, -2.8f, new Item.Settings()));

    public static final Item DARK_PICKAXE = registerItem("dark_pickaxe",
            new ElementalPickaxeItem(ModToolMaturial.ESSENCE_DARK, 1, -2.8f, new Item.Settings()));
            
    public static final Item LIGHT_PICKAXE = registerItem("light_pickaxe",
            new ElementalPickaxeItem(ModToolMaturial.ESSENCE_LIGHT, 1, -2.8f, new Item.Settings()));
            
    
    public static final Item FIRE_AXE = registerItem("fire_axe",
            new ElementalAxeItem(ModToolMaturial.ESSENCE_FIRE, 5.0f, -3.0f, new Item.Settings()));
    
    public static final Item AQUA_AXE = registerItem("aqua_axe",
            new ElementalAxeItem(ModToolMaturial.ESSENCE_AQUA, 5.0f, -3.0f, new Item.Settings()));
    
    public static final Item WEED_AXE = registerItem("weed_axe",
            new ElementalAxeItem(ModToolMaturial.ESSENCE_WEED, 5.0f, -3.0f, new Item.Settings()));
    
    public static final Item DARK_AXE = registerItem("dark_axe",
            new ElementalAxeItem(ModToolMaturial.ESSENCE_DARK, 5.0f, -3.0f, new Item.Settings()));
    
    public static final Item LIGHT_AXE = registerItem("light_axe",
            new ElementalAxeItem(ModToolMaturial.ESSENCE_LIGHT, 5.0f, -3.0f, new Item.Settings()));
         
    public static final Item EARTH_AXE = registerItem("earth_axe",
            new ElementalAxeItem(ModToolMaturial.ESSENCE_EARTH, 5.0f, -3.0f, new Item.Settings()));
    
        
    // поджигает противника.
    public static final Item FIRE_SWORD = registerItem("fire_sword",
            new ElementalSwordItem(ModToolMaturial.ESSENCE_FIRE, 3, -2.4f, new Item.Settings()));

    // замедляет противника
    public static final Item AQUA_SWORD = registerItem("aqua_sword",
            new ElementalSwordItem(ModToolMaturial.ESSENCE_AQUA, 3, -2.4f, new Item.Settings()));

    // отталкивает противника
    public static final Item WEED_SWORD = registerItem("weed_sword",
            new ElementalSwordItem(ModToolMaturial.ESSENCE_WEED, 3, -2.4f, new Item.Settings()));

    // подкидивание вверх
    public static final Item EARTH_SWORD = registerItem("earth_sword",
            new ElementalSwordItem(ModToolMaturial.ESSENCE_EARTH, 3, -2.4f, new Item.Settings()));

    // накладивает слепоту на противника
    public static final Item DARK_SWORD = registerItem("dark_sword",
            new ElementalSwordItem(ModToolMaturial.ESSENCE_DARK, 3, -2.4f, new Item.Settings()));

    // ефект спектральной стрели(свечения)
    public static final Item LIGHT_SWORD = registerItem("light_sword",
            new ElementalSwordItem(ModToolMaturial.ESSENCE_LIGHT, 3, -2.4f, new Item.Settings()));


    public static final Item FIRE_SHOVEL = registerItem("fire_shovel",
            new ElementalShovelItem(ModToolMaturial.ESSENCE_FIRE, 1, -2.8f, new Item.Settings()));

    public static final Item AQUA_SHOVEL = registerItem("aqua_shovel",
            new ElementalShovelItem(ModToolMaturial.ESSENCE_AQUA, 1, -2.8f, new Item.Settings()));

    public static final Item WEED_SHOVEL = registerItem("weed_shovel",
            new ElementalShovelItem(ModToolMaturial.ESSENCE_WEED, 1, -2.8f, new Item.Settings()));

    public static final Item DARK_SHOVEL = registerItem("dark_shovel",
            new ElementalShovelItem(ModToolMaturial.ESSENCE_DARK, 1, -2.8f, new Item.Settings()));

    public static final Item LIGHT_SHOVEL = registerItem("light_shovel",
            new ElementalShovelItem(ModToolMaturial.ESSENCE_LIGHT, 1, -2.8f, new Item.Settings()));

    public static final Item EARTH_SHOVEL = registerItem("earth_shovel",
            new ElementalShovelItem(ModToolMaturial.ESSENCE_EARTH, 1, -2.8f, new Item.Settings()));

    public static final Item FIRE_HOE = registerItem("fire_hoe",
            new ElementalHoeItem(ModToolMaturial.ESSENCE_FIRE, 1, -2.8f, new Item.Settings()));

    public static final Item AQUA_HOE = registerItem("aqua_hoe",
            new ElementalHoeItem(ModToolMaturial.ESSENCE_AQUA, 1, -2.8f, new Item.Settings()));

    public static final Item WEED_HOE = registerItem("weed_hoe",
            new ElementalHoeItem(ModToolMaturial.ESSENCE_WEED, 1, -2.8f, new Item.Settings()));

    public static final Item LIGHT_HOE = registerItem("light_hoe",
            new ElementalHoeItem(ModToolMaturial.ESSENCE_LIGHT, 1, -2.8f, new Item.Settings()));

    public static final Item DARK_HOE = registerItem("dark_hoe",
            new ElementalHoeItem(ModToolMaturial.ESSENCE_DARK, 1, -2.8f, new Item.Settings()));

    public static final Item EARTH_HOE = registerItem("earth_hoe",
            new ElementalHoeItem(ModToolMaturial.ESSENCE_EARTH, 1, -2.8f, new Item.Settings()));



    public static Item registerItem(String name, Item item){
        return Registry.register(
            Registries.ITEM, 
            new Identifier("the-roots-of-ancient-magic", name),
            item
        );
    }

    public static void registerModItems(){
    }
}
