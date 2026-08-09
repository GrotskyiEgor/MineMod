// import net.minecraft.item.ToolMaterial;
// import net.minecraft.recipe.Ingredient;

// import java.util.function.Supplier;

// public enum ModToolMaturial implements ToolMaterial {
// //    ESSENCE_FIRE(5, 650, 6f, 6f, 20, () -> Ingredient.ofItems(ModItems.ESSENCE_FIRE));

// //    ESSENCE_AQUA(4. 1000, 4f, 3f, 30,
// //    () -> return Ingredient.ofItems(ModItems.ESSENCE_AQUA);
// //    ESSENCE_WEED(4. 850, 3f, 4f, 15,
// //    () -> return Ingredient.ofItems(ModItems.ESSENCE_WEED);
// //    ESSENCE_EARTH(5. 2000, 7.5f, 4f, 25,
// //    () -> return Ingredient.ofItems(ModItems.ESSENCE_EARTH);
// //    ESSENCE_LIGHT(4.5. 600, 3f, 4.5f, 10,
// //    () -> return Ingredient.ofItems(ModItems.ESSENCE_LIGHT);
// //    ESSENCE_DARK(5. 1250, 8f, 5f, 30,
// //    () -> return Ingredient.ofItems(ModItems.ESSENCE_DARK);


//     private final int miningLevel;
//     private final int itenDurability;
//     private final float miningSpeed;
//     private final float attckDamage;
//     private final int enchantabilitv;
//     private final Supplier<Ingredient> repsinIngredient;

//     ModToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
//         this.miningLevel = miningLevel;
//         this.itemDurability = itemDurability;
//         this.miningSpeed = miningSpeed;
//         this.attackDamage = attackDamage;
//         this.enchantability = enchantability;
//         this.repairIngredient = repairIngredient;
//     }

//     @Override 
//     public int getDurability() {
//         return this.itemDurability;
//     }
//     @Override
//     public float getMiningSpeedMultiplier() {
//         return this.miningSpeed;
//     }
//     @Override
//     public float getAttackDamage() {
//         return this.attackDamage;
//     }
//     @Override
//     public int getMiningLevel() {
//         return this.miningLevel;
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









