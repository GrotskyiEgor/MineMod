package com.therootsofancientmagic.item;

// import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {
    // Железо
    ESSENCE_AQUA(
            "essence_aqua",
            825,
            new int[]{3, 8, 6, 3},
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            2.0f,
            0.1f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_AQUA)
    ),

    ESSENCE_AIR(
            "essence_air",
            825,
            new int[]{3, 7, 6, 3},
            20,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            1.0f,
            0.0f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_AIR)
    ),

    // Алмазная
    ESSENCE_FIRE(
            "essence_fire",
            35,
            new int[]{4, 9, 7, 4},
            1815,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            2.5f,
            0.1f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_FIRE)
    ),

    ESSENCE_EARTH(
            "essence_earth",
            1815,
            new int[]{5, 10, 8, 5},
            25,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            3.0f,
            0.2f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_EARTH)
    ),

    // Незеритавая 
    ESSENCE_DARK(
            "essence_dark",
            2500,
            new int[]{4, 9, 7, 4},
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            3.0f,
            0.2f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_DARK)
    ),

    ESSENCE_LIGHT(
            "essence_light",
            2500,
            new int[]{5, 10, 8, 5},
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            2.0f,
            0.1f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_LIGHT)
    ),

    // Чуть лучше Незеритовой
    ESSENCE_HEROIC(
            "essence_heroic",
            50,
            new int[]{5, 10, 8, 5},
            25,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            3.0f,
            0.2f,
            () -> Ingredient.ofItems(ModItem.ESSENCE_HEROIC)
    );

    private final String gameName;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterial(
            String gameName,
            int durabilityMultiplier,
            int[] protectionAmounts,
            int enchantability,
            SoundEvent equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        this.gameName = gameName;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public String getName() {
        return gameName;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        int[] baseDurability = {13, 15, 16, 11};
        return baseDurability[type.ordinal()] * durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}