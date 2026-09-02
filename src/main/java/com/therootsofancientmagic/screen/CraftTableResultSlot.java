package com.therootsofancientmagic.screen;

import com.therootsofancientmagic.recipe.ModRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Optional;

public class CraftTableResultSlot extends Slot {

    private final PlayerEntity player;
    private final CraftingInventory input;

    public CraftTableResultSlot(
            PlayerEntity player,
            CraftingInventory input,
            CraftingResultInventory result,
            int index,
            int x,
            int y
    ) {
        super(result, index, x, y);
        this.player = player;
        this.input = input;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        World world = player.getWorld();

        if (world.isClient) {
            return;
        }

        Optional<CraftingRecipe> optional = world.getRecipeManager()
                .getFirstMatch(
                        ModRecipes.CUSTOM_CRAFTING_TYPE,
                        this.input,
                        world
                );

        if (optional.isEmpty()) {
            return;
        }

        CraftingRecipe recipe = optional.get();

        DefaultedList<ItemStack> remainder = recipe.getRemainder(this.input);

        for (int i = 0; i < this.input.size(); i++) {
            ItemStack inputStack = this.input.getStack(i);

            if (!inputStack.isEmpty()) {
                this.input.removeStack(i, 1);
            }

            ItemStack remainderStack = remainder.get(i);

            if (!remainderStack.isEmpty()) {
                if (this.input.getStack(i).isEmpty()) {
                    this.input.setStack(i, remainderStack.copy());
                } else if (ItemStack.canCombine(
                        this.input.getStack(i),
                        remainderStack
                )) {
                    this.input.getStack(i).increment(remainderStack.getCount());
                } else {
                    player.getInventory().insertStack(remainderStack.copy());
                }
            }
        }

        this.onCrafted(stack);
    }
}