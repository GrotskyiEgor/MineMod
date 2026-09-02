package com.therootsofancientmagic.screen;

import com.therootsofancientmagic.recipe.ModRecipes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class CraftTableScreenHandler extends ScreenHandler {

    private final CraftingInventory input;
    private final CraftingResultInventory result = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public CraftTableScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public CraftTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity entity, PropertyDelegate delegate) {
        this(syncId, playerInventory, ScreenHandlerContext.create(entity.getWorld(), entity.getPos()));
    }

    public CraftTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.CRAFT_TABLE_SCREEN_HANDLER, syncId);

        this.context = context;
        this.player = playerInventory.player;
        this.input = new CraftingInventory(this, 3, 3);

        this.addSlot(new CraftTableResultSlot(
                playerInventory.player,
                this.input,
                this.result,
                0,
                124,
                35
        ));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(
                        this.input,
                        j + i * 3,
                        30 + j * 18,
                        17 + i * 18
                ));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(
                        playerInventory,
                        j + i * 9 + 9,
                        8 + j * 18,
                        84 + i * 18
                ));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(
                    playerInventory,
                    i,
                    8 + i * 18,
                    142
            ));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> {
            updateResult(
                    this,
                    world,
                    this.player,
                    this.input,
                    this.result
            );
        });
    }

    protected static void updateResult(
            ScreenHandler handler,
            World world,
            PlayerEntity player,
            CraftingInventory inventory,
            CraftingResultInventory result
    ) {
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
            ItemStack itemStack = ItemStack.EMPTY;

            Optional<CraftingRecipe> optional = world.getRecipeManager()
                    .getFirstMatch(
                            ModRecipes.CUSTOM_CRAFTING_TYPE,
                            inventory,
                            world
                    );

            if (optional.isPresent()) {
                CraftingRecipe recipe = optional.get();

                if (result.shouldCraftRecipe(world, serverPlayer, recipe)) {
                    itemStack = recipe.craft(
                            inventory,
                            world.getRegistryManager()
                    );
                }
            }

            result.setStack(0, itemStack);
        }
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        this.context.run((world, pos) -> {
            this.dropInventory(player, this.input);
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            originalStack = stack.copy();

            if (slotIndex == 0) {
                if (!this.insertItem(stack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(stack, originalStack);

            } else if (slotIndex >= 1 && slotIndex < 10) {
                if (!this.insertItem(stack, 10, 46, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (slotIndex >= 10 && slotIndex < 46) {
                if (!this.insertItem(stack, 1, 10, false)) {
                    return ItemStack.EMPTY;
                }

            } else {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (stack.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }

            if (slotIndex != 0) {
                slot.onTakeItem(player, stack);
            }

            return originalStack;
        }

        return ItemStack.EMPTY;
    }
}