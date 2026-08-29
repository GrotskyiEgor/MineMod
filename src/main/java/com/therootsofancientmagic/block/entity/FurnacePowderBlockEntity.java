package com.therootsofancientmagic.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import com.therootsofancientmagic.item.ModItem;
import com.therootsofancientmagic.block.ModFlowerBlock;
import com.therootsofancientmagic.util.ImplementedInventory;
import com.therootsofancientmagic.screen.FurnacePowderScreenHandler;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FurnacePowderBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public FurnacePowderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FURNACE_POWDER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FurnacePowderBlockEntity.this.progress;
                    case 1 -> FurnacePowderBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FurnacePowderBlockEntity.this.progress = value;
                    case 1 -> FurnacePowderBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Furnace Powder");
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("furnace_powder.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("furnace_powder.progress");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FurnacePowderScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if(world.isClient()) {
            return;
        }

        if(isOutputSlotEmptyOrReceivable()) {
            if(this.hasRecipe()) {
                this.increaseCraftProgress();
                markDirty(world, pos, state);

                if(hasCraftingFinished()) {
                    this.craftItem();
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
            markDirty(world, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        ItemStack result = getRecipeResult();

        if (!result.isEmpty()) {
            this.removeStack(INPUT_SLOT, 1);
            this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), getStack(OUTPUT_SLOT).getCount() + result.getCount()));
        }

        this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), getStack(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private ItemStack getRecipeResult() {
        Item input = getStack(INPUT_SLOT).getItem();

        if (input == ModFlowerBlock.FLOWER_FIRE.asItem()) {
            return new ItemStack(ModItem.ESSENCE_FIRE);
        } else if (input == ModFlowerBlock.FLOWER_AQUA.asItem()) {
            return new ItemStack(ModItem.ESSENCE_AQUA);
        } else if (input == ModFlowerBlock.FLOWER_AIR.asItem()) {
            return new ItemStack(ModItem.ESSENCE_AIR);
        } else if (input == ModFlowerBlock.FLOWER_EARTH.asItem()) {
            return new ItemStack(ModItem.ESSENCE_EARTH);
        } else if (input == ModFlowerBlock.FLOWER_DARK.asItem()) {
            return new ItemStack(ModItem.ESSENCE_DARK);
        } else if (input == ModFlowerBlock.FLOWER_LIGHT.asItem()) {
            return new ItemStack(ModItem.ESSENCE_LIGHT);
        }

        return ItemStack.EMPTY;
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        ItemStack result = new ItemStack(ModItem.ESSENCE_FIRE);

        boolean hasInput = getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_FIRE.asItem()
            || getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_AQUA.asItem()
            || getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_AIR.asItem()
            || getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_EARTH.asItem()
            || getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_DARK.asItem()
            || getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_LIGHT.asItem();
        
        if (getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_AQUA.asItem()) {
            result = new ItemStack(ModItem.ESSENCE_AQUA);
        } else if (getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_AIR.asItem()) {
            result = new ItemStack(ModItem.ESSENCE_AIR);
        } else if (getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_EARTH.asItem()) {
            result = new ItemStack(ModItem.ESSENCE_EARTH);
        } else if (getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_DARK.asItem()) {
            result = new ItemStack(ModItem.ESSENCE_DARK);
        } else if (getStack(INPUT_SLOT).getItem() == ModFlowerBlock.FLOWER_LIGHT.asItem()) {
            result = new ItemStack(ModItem.ESSENCE_LIGHT);
        }

        return hasInput && canInsertAmountIntoOutputSlot(result) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(OUTPUT_SLOT).getItem() == item || this.getStack(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() 
                || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }
}