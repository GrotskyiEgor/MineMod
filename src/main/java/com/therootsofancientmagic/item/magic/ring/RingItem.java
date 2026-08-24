package com.therootsofancientmagic.item.magic.ring;

import net.minecraft.item.Item;

import java.util.UUID;

public class RingItem extends Item {
    public static final UUID RING_1_MODIFIER_ID = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d");
    public static final UUID RING_2_MODIFIER_ID = UUID.fromString("f1e2d3c4-b5a6-9f8e-7d6c-5b4a3f2e1d0c");

    public RingItem(Settings settings) {
        super(settings);
    }
}