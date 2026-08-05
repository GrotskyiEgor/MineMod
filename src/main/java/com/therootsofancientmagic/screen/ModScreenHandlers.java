package com.therootsofancientmagic.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FurnacePowderScreenHandler> FURNACE_POWDER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(TheRootsOfAncientMagic.MOD_ID, "furnace_powder"),
                    new ExtendedScreenHandlerType<>(FurnacePowderScreenHandler::new));

    public static void registerScreenHandlers() {
        TheRootsOfAncientMagic.LOGGER.info("Registering Screen Handlers for " + TheRootsOfAncientMagic.MOD_ID);
    }
}