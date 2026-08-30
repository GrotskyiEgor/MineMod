package com.therootsofancientmagic.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import java.util.function.Supplier;

public enum ModToolMaturial implements ToolMaterial {
    // Железо
    ESSENCE_AQUA(2, 250, 6.0f, 2.0f, 14, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_AQUA")))),
    ESSENCE_AIR(2, 250, 6.0f, 2.0f, 14, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_AIR")))),
    // Алмазы
    ESSENCE_FIRE(3, 1561, 8.0f, 3.0f, 10, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_FIRE")))),
    ESSENCE_EARTH(3, 1561, 8.0f, 3.0f, 10, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_EARTH")))),
    // Незеритовые
    ESSENCE_DARK(4, 2031, 9.0f, 4.0f, 15, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_DARK")))),
    ESSENCE_LIGHT(4, 2031, 9.0f, 4.50f, 15, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "ESSENCE_LIGHT")))),
    // Чуть лучше Незеритовых
    ESSENCE_HEROIC(4, 2400, 10.0f, 5f, 30, () -> Ingredient.ofItems(Registries.ITEM.get(new Identifier("the-roots-of-ancient-magic", "HEROIC_ESSENCE"))));

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