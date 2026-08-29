package com.therootsofancientmagic.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import java.util.function.Supplier;

public enum ModToolMaturial implements ToolMaterial {
    ESSENCE_FIRE(5, 1500, 7.0f, 5.5f, 20, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_FIRE")))),
    ESSENCE_AQUA(4, 1250, 4.5f, 3.5f, 15, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_AQUA")))),
    ESSENCE_AIR(5, 1000, 6.0f, 5.0f, 20, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_AIR")))),
    ESSENCE_DARK(5, 1750, 8.0f, 5.0f, 30, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_DARK")))),
    ESSENCE_LIGHT(5, 700, 9.0f, 4.50f, 30, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_LIGHT")))),
    ESSENCE_EARTH(5, 2000, 7.5f, 5.0f, 25, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_EARTH")))),
    ESSENCE_HEROIC(6, 2500, 8.0f, 8f, 30, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "HEROIC_ESSENCE"))));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaturial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getDurability() { return this.itemDurability; }
    @Override public float getMiningSpeedMultiplier() { return this.miningSpeed; }
    @Override public float getAttackDamage() { return this.attackDamage; }
    @Override public int getMiningLevel() { return this.miningLevel; }
    @Override public int getEnchantability() { return this.enchantability; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
}