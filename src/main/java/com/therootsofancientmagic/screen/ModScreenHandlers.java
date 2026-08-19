package com.therootsofancientmagic.screen;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<FurnacePowderScreenHandler> FURNACE_POWDER_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    new Identifier(TheRootsOfAncientMagic.MOD_ID, "furnace_powder"),
                    new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> 
                            new FurnacePowderScreenHandler(syncId, inventory, buf))
            );

    public static final ScreenHandlerType<CraftTableScreenHandler> CRAFT_TABLE_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    new Identifier(TheRootsOfAncientMagic.MOD_ID, "craft_table"),
                    new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> 
                            new CraftTableScreenHandler(syncId, inventory, buf))
            );
    
    

    public static void registerScreenHandlers() {
        TheRootsOfAncientMagic.LOGGER.info("Registering Screen Handlers for " + TheRootsOfAncientMagic.MOD_ID);
    }
}