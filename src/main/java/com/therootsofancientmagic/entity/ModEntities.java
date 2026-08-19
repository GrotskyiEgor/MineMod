package com.therootsofancientmagic.entity;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    @SuppressWarnings("unchecked")
    public static final EntityType<WindChargeEntity> WIND_CHARGE = (EntityType<WindChargeEntity>)(Object)Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TheRootsOfAncientMagic.MOD_ID, "wind_charge"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, WindChargeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.3125f, 0.3125f))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void registerModEntities() {
        // This method is called in the main mod initializer
    }
}
