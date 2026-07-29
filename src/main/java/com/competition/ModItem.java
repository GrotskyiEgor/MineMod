public class ModItem{
    public static final Item ESSENCE_AQUA = registerItem(
        name="essence_aqua",
        item= new Item(new Item.Settings())
    )
    public static final Item ESSENCE_FIRE = registerItem(
        name="essence_fire",
        item= new Item(new Item.Settings())
    )
    public static final Item ESSENCE_WEED = registerItem(
        name="essence_weed",
        item= new Item(new Item.Settings())
    )
    public static final Item ESSENCE_EARTH = registerItem(
        name="essence_earth",
        item= new Item(new Item.Settings())
    )
    public static final Item ESSENCE_LIGHT = registerItem(
        name="essence_light",
        item= new Item(new Item.Settings())
    )
    public static final Item ESSENCE_DARK = registerItem(
        name="essence_dark",
        item= new Item(new Item.Settings())
    )

    public static final Item FLOWER_AQUA = registerItem(
        name="flower_aqua",
        item= new Item(new Item.Settings())
    )
    
    public static final Item FLOWER_FIRE = registerItem(
        name="flower_fire",
        item= new Item(new Item.Settings())
    )

    public static final Item FLOWER_WEED = registerItem(
        name="flower_weed",
        item= new Item(new Item.Settings())
    )
    
    public static final Item FLOWER_EARTH = registerItem(
        name="flower_earth",
        item= new Item(new Item.Settings())
    )

    public static final Item FLOWER_LIGHT = registerItem(
        name="flower_light",
        item= new Item(new Item.Settings())
    )
    
    public static final Item FLOWER_DARK = registerItem(
        name="flower_dark",
        item= new Item(new Item.Settings())
    )
    
    public static Item registerItem(String name, Item item){
        return Registry.register(
            Registries.ITEM, 
            Identifier.of("ompetition", name),
            item
        );
    }

    public static void registerModItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((entries) -> {
            entries.add(ESSENCE_AQUA);
            entries.add(ESSENCE_FIRE);
            entries.add(ESSENCE_WEED);
            entries.add(ESSENCE_EARTH);
            entries.add(ESSENCE_LIGHT);
            entries.add(ESSENCE_DARK);
            entries.add(FLOWER_AQUA);
            entries.add(FLOWER_FIRE);
            entries.add(FLOWER_WEED);
            entries.add(FLOWER_EARTH);
            entries.add(FLOWER_LIGHT);
            entries.add(FLOWER_DARK);
        });
    }
}