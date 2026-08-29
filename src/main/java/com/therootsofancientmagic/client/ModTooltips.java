package com.therootsofancientmagic.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

import com.therootsofancientmagic.block.ModBlock;
import com.therootsofancientmagic.block.ModFlowerBlock;

public class ModTooltips {

    public static void register() {

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {

            if (stack.isOf(ModBlock.FURNACE_POWDER.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.furnace_powder.description"
                ));
            }

            if (stack.isOf(ModBlock.CRAFT_TABLE.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.craft_table.description"
                ));
            }

            if (stack.isOf(ModFlowerBlock.FLOWER_DARK.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.flower_dark.description"
                ));
            }

            if (stack.isOf(ModFlowerBlock.FLOWER_LIGHT.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.flower_light.description"
                ));
            }

            if (stack.isOf(ModFlowerBlock.FLOWER_AQUA.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.flower_aqua.description"
                ));
            }

            if (stack.isOf(ModFlowerBlock.FLOWER_FIRE.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.flower_fire.description"
                ));
            }

            if (stack.isOf(ModFlowerBlock.FLOWER_EARTH.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.flower_earth.description"
                ));
            }

            if (stack.isOf(ModFlowerBlock.FLOWER_AIR.asItem())) {
                lines.add(Text.translatable(
                    "item.the-roots-of-ancient-magic.flower_air.description"
                ));
            }
        });
    }
}