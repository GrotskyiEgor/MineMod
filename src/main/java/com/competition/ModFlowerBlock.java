public class ModFlowerBlock {
    public static Block DARK = registerBlock(
        name="dark",
        block= new FlowerBlock(
            () -> MobEffects.LUCK,
            0,
            BlockBehaviour.Properties.copy(Blocks.DANDELION)
        );
    ) 
     
    public static Block registerBlock(String name, Block block){
        Registry.register(
            Registries.BLOCK, 
            Identifier.of("ompetition", name),
            block
        );
        //AbstractBlock.Settings.create().noCollision().breakInstantly().sounds(BlockSoundGroup.).
        Registry.register(
            Registries.ITEM, 
            Identifier.of("ompetition", name),
            new BlockItem(block, new Item.Settings())
        );

        return block
    }

    public static void registerModBlocks(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
            entries.add(ESSENCE_AQUA)
        });
    }
}
