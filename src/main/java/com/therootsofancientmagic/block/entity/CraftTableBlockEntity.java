package com.therootsofancientmagic.block.entity;

import com.therootsofancientmagic.screen.CraftTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CraftTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    public CraftTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFT_TABLE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Craft Table");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CraftTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos));
    }
}