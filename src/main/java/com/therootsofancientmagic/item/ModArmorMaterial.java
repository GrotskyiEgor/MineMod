// package com.therootsofancientmagic.item;

// import net.minecraft.item.ArmorMaterial;
// import net.minecraft.recipe.Ingredient;
// import net.minecraft.util.Identifier;
// import net.minecraft.registry.Registries;
// import java.util.function.Supplier;

// public enum ModArmorMaterial implements ArmorMaterial {
//     ESSENCE_FIRE(1500, 7.0f, 5.5f, 20, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "essence_fire")))),
//     ESSENCE_AQUA(1250, 4.5f, 3.5f, 15, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "essence_aqua")))),
//     ESSENCE_WEED(1000, 6.0f, 5.0f, 20, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "essence_weed")))),
//     ESSENCE_DARK(1750, 8.0f, 5.0f, 30, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "essence_dark")))),
//     ESSENCE_LIGHT(700, 9.0f, 4.50f, 30, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "essence_light")))),
//     ESSENCE_EARTH(52000, 7.5f, 5.0f, 25, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "essence_earth"))));


//     private final int durability;
//     private final int protection;
//     private final float toughness;
//     private final int enchantability;
//     private final Supplier<Ingredient> repairIngredient;

//     ModArmorMaterial(int durability, int protection, float toughness, int enchantability, Supplier<Ingredient> repairIngredient) {
//         this.durability = durability;
//         this.protection = protection;
//         this.toughness = toughness;
//         this.enchantability = enchantability;
//         this.repairIngredient = repairIngredient;
//     }

//     @Override 
//     public int getDurability() { 
//         return this.durability; 
//     }
    
//     @Override 
//     public float getProtectionAmount() { 
//         return this.protection; 
//     }

//     @Override 
//     public int getKnockbackResistance() { 
//         return 0; 
//     }

//     @Override 
//     public int getEnchantability() { 
//         return this.enchantability; 
//     }
    
//     @Override 
//     public Ingredient getRepairIngredient() { 
//         return this.repairIngredient.get(); 
//     }
// }