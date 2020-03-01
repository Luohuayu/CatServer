package org.bukkit;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.map.MapView;
import org.bukkit.material.Bed;
import org.bukkit.material.Button;
import org.bukkit.material.Cake;
import org.bukkit.material.Cauldron;
import org.bukkit.material.Chest;
import org.bukkit.material.Coal;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Command;
import org.bukkit.material.Comparator;
import org.bukkit.material.Crops;
import org.bukkit.material.DetectorRail;
import org.bukkit.material.Diode;
import org.bukkit.material.Dispenser;
import org.bukkit.material.Door;
import org.bukkit.material.Dye;
import org.bukkit.material.EnderChest;
import org.bukkit.material.FlowerPot;
import org.bukkit.material.Furnace;
import org.bukkit.material.Gate;
import org.bukkit.material.Hopper;
import org.bukkit.material.Ladder;
import org.bukkit.material.Leaves;
import org.bukkit.material.Lever;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;
import org.bukkit.material.MonsterEggs;
import org.bukkit.material.Mushroom;
import org.bukkit.material.NetherWarts;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.PistonExtensionMaterial;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.PressurePlate;
import org.bukkit.material.Pumpkin;
import org.bukkit.material.Rails;
import org.bukkit.material.RedstoneTorch;
import org.bukkit.material.RedstoneWire;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Sapling;
import org.bukkit.material.Sign;
import org.bukkit.material.Skull;
import org.bukkit.material.SmoothBrick;
import org.bukkit.material.SpawnEgg;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.Torch;
import org.bukkit.material.TrapDoor;
import org.bukkit.material.Tree;
import org.bukkit.material.Tripwire;
import org.bukkit.material.TripwireHook;
import org.bukkit.material.Vine;
import org.bukkit.material.Wood;
import org.bukkit.material.WoodenStep;
import org.bukkit.material.Wool;

import com.google.common.collect.Maps;

import org.bukkit.material.Banner;
import org.bukkit.material.Observer;

import javax.annotation.Nullable;

/**
 * An enum of all material IDs accepted by the official server and client
 */
public enum Material {
    AIR(0, 0),
    STONE(1),
    GRASS(2),
    DIRT(3),
    COBBLESTONE(4),
    WOOD(5, Wood.class),
    SAPLING(6, Sapling.class),
    BEDROCK(7),
    WATER(8, MaterialData.class),
    STATIONARY_WATER(9, MaterialData.class),
    LAVA(10, MaterialData.class),
    STATIONARY_LAVA(11, MaterialData.class),
    SAND(12),
    GRAVEL(13),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    LOG(17, Tree.class),
    LEAVES(18, Leaves.class),
    SPONGE(19),
    GLASS(20),
    LAPIS_ORE(21),
    LAPIS_BLOCK(22),
    DISPENSER(23, Dispenser.class),
    SANDSTONE(24, Sandstone.class),
    NOTE_BLOCK(25),
    BED_BLOCK(26, Bed.class),
    POWERED_RAIL(27, PoweredRail.class),
    DETECTOR_RAIL(28, DetectorRail.class),
    PISTON_STICKY_BASE(29, PistonBaseMaterial.class),
    WEB(30),
    LONG_GRASS(31, LongGrass.class),
    DEAD_BUSH(32),
    PISTON_BASE(33, PistonBaseMaterial.class),
    PISTON_EXTENSION(34, PistonExtensionMaterial.class),
    WOOL(35, Wool.class),
    PISTON_MOVING_PIECE(36),
    YELLOW_FLOWER(37),
    RED_ROSE(38),
    BROWN_MUSHROOM(39),
    RED_MUSHROOM(40),
    GOLD_BLOCK(41),
    IRON_BLOCK(42),
    DOUBLE_STEP(43, Step.class),
    STEP(44, Step.class),
    BRICK(45),
    TNT(46),
    BOOKSHELF(47),
    MOSSY_COBBLESTONE(48),
    OBSIDIAN(49),
    TORCH(50, Torch.class),
    FIRE(51),
    MOB_SPAWNER(52),
    WOOD_STAIRS(53, Stairs.class),
    CHEST(54, Chest.class),
    REDSTONE_WIRE(55, RedstoneWire.class),
    DIAMOND_ORE(56),
    DIAMOND_BLOCK(57),
    WORKBENCH(58),
    CROPS(59, Crops.class),
    SOIL(60, MaterialData.class),
    FURNACE(61, Furnace.class),
    BURNING_FURNACE(62, Furnace.class),
    SIGN_POST(63, 64, Sign.class),
    WOODEN_DOOR(64, Door.class),
    LADDER(65, Ladder.class),
    RAILS(66, Rails.class),
    COBBLESTONE_STAIRS(67, Stairs.class),
    WALL_SIGN(68, 64, Sign.class),
    LEVER(69, Lever.class),
    STONE_PLATE(70, PressurePlate.class),
    IRON_DOOR_BLOCK(71, Door.class),
    WOOD_PLATE(72, PressurePlate.class),
    REDSTONE_ORE(73),
    GLOWING_REDSTONE_ORE(74),
    REDSTONE_TORCH_OFF(75, RedstoneTorch.class),
    REDSTONE_TORCH_ON(76, RedstoneTorch.class),
    STONE_BUTTON(77, Button.class),
    SNOW(78),
    ICE(79),
    SNOW_BLOCK(80),
    CACTUS(81, MaterialData.class),
    CLAY(82),
    SUGAR_CANE_BLOCK(83, MaterialData.class),
    JUKEBOX(84),
    FENCE(85),
    PUMPKIN(86, Pumpkin.class),
    NETHERRACK(87),
    SOUL_SAND(88),
    GLOWSTONE(89),
    PORTAL(90),
    JACK_O_LANTERN(91, Pumpkin.class),
    CAKE_BLOCK(92, 64, Cake.class),
    DIODE_BLOCK_OFF(93, Diode.class),
    DIODE_BLOCK_ON(94, Diode.class),
    STAINED_GLASS(95),
    TRAP_DOOR(96, TrapDoor.class),
    MONSTER_EGGS(97, MonsterEggs.class),
    SMOOTH_BRICK(98, SmoothBrick.class),
    HUGE_MUSHROOM_1(99, Mushroom.class),
    HUGE_MUSHROOM_2(100, Mushroom.class),
    IRON_FENCE(101),
    THIN_GLASS(102),
    MELON_BLOCK(103),
    PUMPKIN_STEM(104, MaterialData.class),
    MELON_STEM(105, MaterialData.class),
    VINE(106, Vine.class),
    FENCE_GATE(107, Gate.class),
    BRICK_STAIRS(108, Stairs.class),
    SMOOTH_STAIRS(109, Stairs.class),
    MYCEL(110),
    WATER_LILY(111),
    NETHER_BRICK(112),
    NETHER_FENCE(113),
    NETHER_BRICK_STAIRS(114, Stairs.class),
    NETHER_WARTS(115, NetherWarts.class),
    ENCHANTMENT_TABLE(116),
    BREWING_STAND(117, MaterialData.class),
    CAULDRON(118, Cauldron.class),
    ENDER_PORTAL(119),
    ENDER_PORTAL_FRAME(120),
    ENDER_STONE(121),
    DRAGON_EGG(122),
    REDSTONE_LAMP_OFF(123),
    REDSTONE_LAMP_ON(124),
    WOOD_DOUBLE_STEP(125, Wood.class),
    WOOD_STEP(126, WoodenStep.class),
    COCOA(127, CocoaPlant.class),
    SANDSTONE_STAIRS(128, Stairs.class),
    EMERALD_ORE(129),
    ENDER_CHEST(130, EnderChest.class),
    TRIPWIRE_HOOK(131, TripwireHook.class),
    TRIPWIRE(132, Tripwire.class),
    EMERALD_BLOCK(133),
    SPRUCE_WOOD_STAIRS(134, Stairs.class),
    BIRCH_WOOD_STAIRS(135, Stairs.class),
    JUNGLE_WOOD_STAIRS(136, Stairs.class),
    COMMAND(137, Command.class),
    BEACON(138),
    COBBLE_WALL(139),
    FLOWER_POT(140, FlowerPot.class),
    CARROT(141, Crops.class),
    POTATO(142, Crops.class),
    WOOD_BUTTON(143, Button.class),
    SKULL(144, Skull.class),
    ANVIL(145),
    TRAPPED_CHEST(146, Chest.class),
    GOLD_PLATE(147),
    IRON_PLATE(148),
    REDSTONE_COMPARATOR_OFF(149, Comparator.class),
    REDSTONE_COMPARATOR_ON(150, Comparator.class),
    DAYLIGHT_DETECTOR(151),
    REDSTONE_BLOCK(152),
    QUARTZ_ORE(153),
    HOPPER(154, Hopper.class),
    QUARTZ_BLOCK(155),
    QUARTZ_STAIRS(156, Stairs.class),
    ACTIVATOR_RAIL(157, PoweredRail.class),
    DROPPER(158, Dispenser.class),
    STAINED_CLAY(159),
    STAINED_GLASS_PANE(160),
    LEAVES_2(161, Leaves.class),
    LOG_2(162, Tree.class),
    ACACIA_STAIRS(163, Stairs.class),
    DARK_OAK_STAIRS(164, Stairs.class),
    SLIME_BLOCK(165),
    BARRIER(166),
    IRON_TRAPDOOR(167, TrapDoor.class),
    PRISMARINE(168),
    SEA_LANTERN(169),
    HAY_BLOCK(170),
    CARPET(171),
    HARD_CLAY(172),
    COAL_BLOCK(173),
    PACKED_ICE(174),
    DOUBLE_PLANT(175),
    STANDING_BANNER(176, Banner.class),
    WALL_BANNER(177, Banner.class),
    DAYLIGHT_DETECTOR_INVERTED(178),
    RED_SANDSTONE(179),
    RED_SANDSTONE_STAIRS(180, Stairs.class),
    DOUBLE_STONE_SLAB2(181),
    STONE_SLAB2(182),
    SPRUCE_FENCE_GATE(183, Gate.class),
    BIRCH_FENCE_GATE(184, Gate.class),
    JUNGLE_FENCE_GATE(185, Gate.class),
    DARK_OAK_FENCE_GATE(186, Gate.class),
    ACACIA_FENCE_GATE(187, Gate.class),
    SPRUCE_FENCE(188),
    BIRCH_FENCE(189),
    JUNGLE_FENCE(190),
    DARK_OAK_FENCE(191),
    ACACIA_FENCE(192),
    SPRUCE_DOOR(193, Door.class),
    BIRCH_DOOR(194, Door.class),
    JUNGLE_DOOR(195, Door.class),
    ACACIA_DOOR(196, Door.class),
    DARK_OAK_DOOR(197, Door.class),
    END_ROD(198),
    CHORUS_PLANT(199),
    CHORUS_FLOWER(200),
    PURPUR_BLOCK(201),
    PURPUR_PILLAR(202),
    PURPUR_STAIRS(203, Stairs.class),
    PURPUR_DOUBLE_SLAB(204),
    PURPUR_SLAB(205),
    END_BRICKS(206),
    BEETROOT_BLOCK(207, Crops.class),
    GRASS_PATH(208),
    END_GATEWAY(209),
    COMMAND_REPEATING(210, Command.class),
    COMMAND_CHAIN(211, Command.class),
    FROSTED_ICE(212),
    MAGMA(213),
    NETHER_WART_BLOCK(214),
    RED_NETHER_BRICK(215),
    BONE_BLOCK(216),
    STRUCTURE_VOID(217),
    OBSERVER(218, Observer.class),
    WHITE_SHULKER_BOX(219, 1),
    ORANGE_SHULKER_BOX(220, 1),
    MAGENTA_SHULKER_BOX(221, 1),
    LIGHT_BLUE_SHULKER_BOX(222, 1),
    YELLOW_SHULKER_BOX(223, 1),
    LIME_SHULKER_BOX(224, 1),
    PINK_SHULKER_BOX(225, 1),
    GRAY_SHULKER_BOX(226, 1),
    SILVER_SHULKER_BOX(227, 1),
    CYAN_SHULKER_BOX(228, 1),
    PURPLE_SHULKER_BOX(229, 1),
    BLUE_SHULKER_BOX(230, 1),
    BROWN_SHULKER_BOX(231, 1),
    GREEN_SHULKER_BOX(232, 1),
    RED_SHULKER_BOX(233, 1),
    BLACK_SHULKER_BOX(234, 1),
    WHITE_GLAZED_TERRACOTTA(235),
    ORANGE_GLAZED_TERRACOTTA(236),
    MAGENTA_GLAZED_TERRACOTTA(237),
    LIGHT_BLUE_GLAZED_TERRACOTTA(238),
    YELLOW_GLAZED_TERRACOTTA(239),
    LIME_GLAZED_TERRACOTTA(240),
    PINK_GLAZED_TERRACOTTA(241),
    GRAY_GLAZED_TERRACOTTA(242),
    SILVER_GLAZED_TERRACOTTA(243),
    CYAN_GLAZED_TERRACOTTA(244),
    PURPLE_GLAZED_TERRACOTTA(245),
    BLUE_GLAZED_TERRACOTTA(246),
    BROWN_GLAZED_TERRACOTTA(247),
    GREEN_GLAZED_TERRACOTTA(248),
    RED_GLAZED_TERRACOTTA(249),
    BLACK_GLAZED_TERRACOTTA(250),
    CONCRETE(251),
    CONCRETE_POWDER(252),
    STRUCTURE_BLOCK(255),
    // ----- Item Separator -----
    IRON_SPADE(256, 1, 250),
    IRON_PICKAXE(257, 1, 250),
    IRON_AXE(258, 1, 250),
    FLINT_AND_STEEL(259, 1, 64),
    APPLE(260),
    BOW(261, 1, 384),
    ARROW(262),
    COAL(263, Coal.class),
    DIAMOND(264),
    IRON_INGOT(265),
    GOLD_INGOT(266),
    IRON_SWORD(267, 1, 250),
    WOOD_SWORD(268, 1, 59),
    WOOD_SPADE(269, 1, 59),
    WOOD_PICKAXE(270, 1, 59),
    WOOD_AXE(271, 1, 59),
    STONE_SWORD(272, 1, 131),
    STONE_SPADE(273, 1, 131),
    STONE_PICKAXE(274, 1, 131),
    STONE_AXE(275, 1, 131),
    DIAMOND_SWORD(276, 1, 1561),
    DIAMOND_SPADE(277, 1, 1561),
    DIAMOND_PICKAXE(278, 1, 1561),
    DIAMOND_AXE(279, 1, 1561),
    STICK(280),
    BOWL(281),
    MUSHROOM_SOUP(282, 1),
    GOLD_SWORD(283, 1, 32),
    GOLD_SPADE(284, 1, 32),
    GOLD_PICKAXE(285, 1, 32),
    GOLD_AXE(286, 1, 32),
    STRING(287),
    FEATHER(288),
    SULPHUR(289),
    WOOD_HOE(290, 1, 59),
    STONE_HOE(291, 1, 131),
    IRON_HOE(292, 1, 250),
    DIAMOND_HOE(293, 1, 1561),
    GOLD_HOE(294, 1, 32),
    SEEDS(295),
    WHEAT(296),
    BREAD(297),
    LEATHER_HELMET(298, 1, 55),
    LEATHER_CHESTPLATE(299, 1, 80),
    LEATHER_LEGGINGS(300, 1, 75),
    LEATHER_BOOTS(301, 1, 65),
    CHAINMAIL_HELMET(302, 1, 165),
    CHAINMAIL_CHESTPLATE(303, 1, 240),
    CHAINMAIL_LEGGINGS(304, 1, 225),
    CHAINMAIL_BOOTS(305, 1, 195),
    IRON_HELMET(306, 1, 165),
    IRON_CHESTPLATE(307, 1, 240),
    IRON_LEGGINGS(308, 1, 225),
    IRON_BOOTS(309, 1, 195),
    DIAMOND_HELMET(310, 1, 363),
    DIAMOND_CHESTPLATE(311, 1, 528),
    DIAMOND_LEGGINGS(312, 1, 495),
    DIAMOND_BOOTS(313, 1, 429),
    GOLD_HELMET(314, 1, 77),
    GOLD_CHESTPLATE(315, 1, 112),
    GOLD_LEGGINGS(316, 1, 105),
    GOLD_BOOTS(317, 1, 91),
    FLINT(318),
    PORK(319),
    GRILLED_PORK(320),
    PAINTING(321),
    GOLDEN_APPLE(322),
    SIGN(323, 16),
    WOOD_DOOR(324, 64),
    BUCKET(325, 16),
    WATER_BUCKET(326, 1),
    LAVA_BUCKET(327, 1),
    MINECART(328, 1),
    SADDLE(329, 1),
    IRON_DOOR(330, 64),
    REDSTONE(331),
    SNOW_BALL(332, 16),
    BOAT(333, 1),
    LEATHER(334),
    MILK_BUCKET(335, 1),
    CLAY_BRICK(336),
    CLAY_BALL(337),
    SUGAR_CANE(338),
    PAPER(339),
    BOOK(340),
    SLIME_BALL(341),
    STORAGE_MINECART(342, 1),
    POWERED_MINECART(343, 1),
    EGG(344, 16),
    COMPASS(345),
    FISHING_ROD(346, 1, 64),
    WATCH(347),
    GLOWSTONE_DUST(348),
    RAW_FISH(349),
    COOKED_FISH(350),
    INK_SACK(351, Dye.class),
    BONE(352),
    SUGAR(353),
    CAKE(354, 1),
    BED(355, 1),
    DIODE(356),
    COOKIE(357),
    /**
     * @see MapView
     */
    MAP(358, MaterialData.class),
    SHEARS(359, 1, 238),
    MELON(360),
    PUMPKIN_SEEDS(361),
    MELON_SEEDS(362),
    RAW_BEEF(363),
    COOKED_BEEF(364),
    RAW_CHICKEN(365),
    COOKED_CHICKEN(366),
    ROTTEN_FLESH(367),
    ENDER_PEARL(368, 16),
    BLAZE_ROD(369),
    GHAST_TEAR(370),
    GOLD_NUGGET(371),
    NETHER_STALK(372),
    POTION(373, 1, MaterialData.class),
    GLASS_BOTTLE(374),
    SPIDER_EYE(375),
    FERMENTED_SPIDER_EYE(376),
    BLAZE_POWDER(377),
    MAGMA_CREAM(378),
    BREWING_STAND_ITEM(379),
    CAULDRON_ITEM(380),
    EYE_OF_ENDER(381),
    SPECKLED_MELON(382),
    MONSTER_EGG(383, 64, SpawnEgg.class),
    EXP_BOTTLE(384, 64),
    FIREBALL(385, 64),
    BOOK_AND_QUILL(386, 1),
    WRITTEN_BOOK(387, 16),
    EMERALD(388, 64),
    ITEM_FRAME(389),
    FLOWER_POT_ITEM(390),
    CARROT_ITEM(391),
    POTATO_ITEM(392),
    BAKED_POTATO(393),
    POISONOUS_POTATO(394),
    EMPTY_MAP(395),
    GOLDEN_CARROT(396),
    SKULL_ITEM(397),
    CARROT_STICK(398, 1, 25),
    NETHER_STAR(399),
    PUMPKIN_PIE(400),
    FIREWORK(401),
    FIREWORK_CHARGE(402),
    ENCHANTED_BOOK(403, 1),
    REDSTONE_COMPARATOR(404),
    NETHER_BRICK_ITEM(405),
    QUARTZ(406),
    EXPLOSIVE_MINECART(407, 1),
    HOPPER_MINECART(408, 1),
    PRISMARINE_SHARD(409),
    PRISMARINE_CRYSTALS(410),
    RABBIT(411),
    COOKED_RABBIT(412),
    RABBIT_STEW(413, 1),
    RABBIT_FOOT(414),
    RABBIT_HIDE(415),
    ARMOR_STAND(416, 16),
    IRON_BARDING(417, 1),
    GOLD_BARDING(418, 1),
    DIAMOND_BARDING(419, 1),
    LEASH(420),
    NAME_TAG(421),
    COMMAND_MINECART(422, 1),
    MUTTON(423),
    COOKED_MUTTON(424),
    BANNER(425, 16),
    END_CRYSTAL(426),
    SPRUCE_DOOR_ITEM(427),
    BIRCH_DOOR_ITEM(428),
    JUNGLE_DOOR_ITEM(429),
    ACACIA_DOOR_ITEM(430),
    DARK_OAK_DOOR_ITEM(431),
    CHORUS_FRUIT(432),
    CHORUS_FRUIT_POPPED(433),
    BEETROOT(434),
    BEETROOT_SEEDS(435),
    BEETROOT_SOUP(436, 1),
    DRAGONS_BREATH(437),
    SPLASH_POTION(438, 1),
    SPECTRAL_ARROW(439),
    TIPPED_ARROW(440),
    LINGERING_POTION(441, 1),
    SHIELD(442, 1, 336),
    ELYTRA(443, 1, 431),
    BOAT_SPRUCE(444, 1),
    BOAT_BIRCH(445, 1),
    BOAT_JUNGLE(446, 1),
    BOAT_ACACIA(447, 1),
    BOAT_DARK_OAK(448, 1),
    TOTEM(449, 1),
    SHULKER_SHELL(450),
    IRON_NUGGET(452),
    KNOWLEDGE_BOOK(453, 1),
    GOLD_RECORD(2256, 1),
    GREEN_RECORD(2257, 1),
    RECORD_3(2258, 1),
    RECORD_4(2259, 1),
    RECORD_5(2260, 1),
    RECORD_6(2261, 1),
    RECORD_7(2262, 1),
    RECORD_8(2263, 1),
    RECORD_9(2264, 1),
    RECORD_10(2265, 1),
    RECORD_11(2266, 1),
    RECORD_12(2267, 1),
    ;

    private final int id;
    private final Constructor<? extends MaterialData> ctor;
    private static Material[] byId = new Material[32000];
    private static Material[] blockById = new Material[32000];
    private final static Map<String, Material> BY_NAME = Maps.newHashMap();
    private final static Map<String, Material> BLOCK_BY_NAME = Maps.newHashMap();
    private final int maxStack;
    private final short durability;

    private Material(final int id) {
        this(id, 64);
    }

    private Material(final int id, final int stack) {
        this(id, stack, MaterialData.class);
    }

    private Material(final int id, final int stack, final int durability) {
        this(id, stack, durability, MaterialData.class);
    }

    private Material(final int id, final Class<? extends MaterialData> data) {
        this(id, 64, data);
    }

    private Material(final int id, final int stack, final Class<? extends MaterialData> data) {
        this(id, stack, 0, data);
    }

    private Material(final int id, final int stack, final int durability, final Class<? extends MaterialData> data) {
        this.id = id;
        this.durability = (short) durability;
        this.maxStack = stack;
        // try to cache the constructor for this material
        try {
            this.ctor = data.getConstructor(int.class, byte.class);
        } catch (NoSuchMethodException ex) {
            throw new AssertionError(ex);
        } catch (SecurityException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * Gets the item ID or block ID of this Material
     *
     * @return ID of this material
     * @deprecated Magic value
     */
    @Deprecated
    public int getId() {
        return id;
    }

    /**
     * Gets the maximum amount of this material that can be held in a stack
     *
     * @return Maximum stack size for this material
     */
    public int getMaxStackSize() {
        return maxStack;
    }

    /**
     * Gets the maximum durability of this material
     *
     * @return Maximum durability for this material
     */
    public short getMaxDurability() {
        return durability;
    }

    /**
     * Gets the MaterialData class associated with this Material
     *
     * @return MaterialData associated with this Material
     */
    public Class<? extends MaterialData> getData() {
        return ctor.getDeclaringClass();
    }

    /**
     * Constructs a new MaterialData relevant for this Material, with the
     * given initial data
     *
     * @param raw Initial data to construct the MaterialData with
     * @return New MaterialData with the given data
     * @deprecated Magic value
     */
    @Deprecated
    public MaterialData getNewData(final byte raw) {
        try {
            return ctor.newInstance(id, raw);
        } catch (InstantiationException ex) {
            final Throwable t = ex.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new AssertionError(t);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    /**
     * Checks if this Material is a placable block
     *
     * @return true if this material is a block
     */
    public boolean isBlock() {
        for (Material material : blockById) {
            if (this == material) return true;
        }
        return false;
    }

    /**
     * Checks if this Material is edible.
     *
     * @return true if this Material is edible.
     */
    public boolean isEdible() {
        switch (this) {
            case BREAD:
            case CARROT_ITEM:
            case BAKED_POTATO:
            case POTATO_ITEM:
            case POISONOUS_POTATO:
            case GOLDEN_CARROT:
            case PUMPKIN_PIE:
            case COOKIE:
            case MELON:
            case MUSHROOM_SOUP:
            case RAW_CHICKEN:
            case COOKED_CHICKEN:
            case RAW_BEEF:
            case COOKED_BEEF:
            case RAW_FISH:
            case COOKED_FISH:
            case PORK:
            case GRILLED_PORK:
            case APPLE:
            case GOLDEN_APPLE:
            case ROTTEN_FLESH:
            case SPIDER_EYE:
            case RABBIT:
            case COOKED_RABBIT:
            case RABBIT_STEW:
            case MUTTON:
            case COOKED_MUTTON:
            case BEETROOT:
            case CHORUS_FRUIT:
            case BEETROOT_SOUP:
                return true;
            default:
                return false;
        }
    }

    /**
     * Attempts to get the Material with the given ID
     *
     * @param id ID of the material to get
     * @return Material if found, or null
     * @deprecated Magic value
     */
    @Deprecated
    public static Material getMaterial(final int id) {
        if (byId.length > id && id >= 0) {
            return byId[id];
        } else {
            return null;
        }
    }

    /**
     * Attempts to get the Material with the given name.
     * <p>
     * This is a normal lookup, names must be the precise name they are given
     * in the enum.
     *
     * @param name Name of the material to get
     * @return Material if found, or null
     */
    public static Material getMaterial(final String name) {
        return BY_NAME.get(name);
    }

    /**
     * Attempts to match the Material with the given name.
     * <p>
     * This is a match lookup; names will be converted to uppercase, then
     * stripped of special characters in an attempt to format it like the
     * enum.
     * <p>
     * Using this for match by ID is deprecated.
     *
     * @param name Name of the material to get
     * @return Material if found, or null
     */
    public static Material matchMaterial(final String name) {
        Validate.notNull(name, "Name cannot be null");

        Material result = null;

        try {
            result = getMaterial(Integer.parseInt(name));
        } catch (NumberFormatException ex) {}

        if (result == null) {
            String filtered = name.toUpperCase(java.util.Locale.ENGLISH);

            filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
            result = BY_NAME.get(filtered);
        }

        return result;
    }

    static {
        for (Material material : values()) {
            if (byId.length > material.id) {
                byId[material.id] = material;
            } else {
                byId = Arrays.copyOfRange(byId, 0, material.id + 2);
                byId[material.id] = material;
            }
            BY_NAME.put(material.name(), material);
        }
    }

    /**
     * @return True if this material represents a playable music disk.
     */
    public boolean isRecord() {
        return id >= GOLD_RECORD.id && id <= RECORD_12.id;
    }

    /**
     * Check if the material is a block and solid (can be built upon)
     *
     * @return True if this material is a block and solid
     */
    public boolean isSolid() {
        if (!isBlock() || id == 0) {
            return false;
        }
        switch (this) {
            case STONE:
            case GRASS:
            case DIRT:
            case COBBLESTONE:
            case WOOD:
            case BEDROCK:
            case SAND:
            case GRAVEL:
            case GOLD_ORE:
            case IRON_ORE:
            case COAL_ORE:
            case LOG:
            case LEAVES:
            case SPONGE:
            case GLASS:
            case LAPIS_ORE:
            case LAPIS_BLOCK:
            case DISPENSER:
            case SANDSTONE:
            case NOTE_BLOCK:
            case BED_BLOCK:
            case PISTON_STICKY_BASE:
            case PISTON_BASE:
            case PISTON_EXTENSION:
            case WOOL:
            case PISTON_MOVING_PIECE:
            case GOLD_BLOCK:
            case IRON_BLOCK:
            case DOUBLE_STEP:
            case STEP:
            case BRICK:
            case TNT:
            case BOOKSHELF:
            case MOSSY_COBBLESTONE:
            case OBSIDIAN:
            case MOB_SPAWNER:
            case WOOD_STAIRS:
            case CHEST:
            case DIAMOND_ORE:
            case DIAMOND_BLOCK:
            case WORKBENCH:
            case SOIL:
            case FURNACE:
            case BURNING_FURNACE:
            case SIGN_POST:
            case WOODEN_DOOR:
            case COBBLESTONE_STAIRS:
            case WALL_SIGN:
            case STONE_PLATE:
            case IRON_DOOR_BLOCK:
            case WOOD_PLATE:
            case REDSTONE_ORE:
            case GLOWING_REDSTONE_ORE:
            case ICE:
            case SNOW_BLOCK:
            case CACTUS:
            case CLAY:
            case JUKEBOX:
            case FENCE:
            case PUMPKIN:
            case NETHERRACK:
            case SOUL_SAND:
            case GLOWSTONE:
            case JACK_O_LANTERN:
            case CAKE_BLOCK:
            case STAINED_GLASS:
            case TRAP_DOOR:
            case MONSTER_EGGS:
            case SMOOTH_BRICK:
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2:
            case IRON_FENCE:
            case THIN_GLASS:
            case MELON_BLOCK:
            case FENCE_GATE:
            case BRICK_STAIRS:
            case SMOOTH_STAIRS:
            case MYCEL:
            case NETHER_BRICK:
            case NETHER_FENCE:
            case NETHER_BRICK_STAIRS:
            case ENCHANTMENT_TABLE:
            case BREWING_STAND:
            case CAULDRON:
            case ENDER_PORTAL_FRAME:
            case ENDER_STONE:
            case DRAGON_EGG:
            case REDSTONE_LAMP_OFF:
            case REDSTONE_LAMP_ON:
            case WOOD_DOUBLE_STEP:
            case WOOD_STEP:
            case SANDSTONE_STAIRS:
            case EMERALD_ORE:
            case ENDER_CHEST:
            case EMERALD_BLOCK:
            case SPRUCE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case COMMAND:
            case BEACON:
            case COBBLE_WALL:
            case ANVIL:
            case TRAPPED_CHEST:
            case GOLD_PLATE:
            case IRON_PLATE:
            case DAYLIGHT_DETECTOR:
            case REDSTONE_BLOCK:
            case QUARTZ_ORE:
            case HOPPER:
            case QUARTZ_BLOCK:
            case QUARTZ_STAIRS:
            case DROPPER:
            case STAINED_CLAY:
            case HAY_BLOCK:
            case HARD_CLAY:
            case COAL_BLOCK:
            case STAINED_GLASS_PANE:
            case LEAVES_2:
            case LOG_2:
            case ACACIA_STAIRS:
            case DARK_OAK_STAIRS:
            case PACKED_ICE:
            case RED_SANDSTONE:
            case SLIME_BLOCK:
            case BARRIER:
            case IRON_TRAPDOOR:
            case PRISMARINE:
            case SEA_LANTERN:
            case DOUBLE_STONE_SLAB2:
            case RED_SANDSTONE_STAIRS:
            case STONE_SLAB2:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case SPRUCE_FENCE:
            case BIRCH_FENCE:
            case JUNGLE_FENCE:
            case DARK_OAK_FENCE:
            case ACACIA_FENCE:
            case STANDING_BANNER:
            case WALL_BANNER:
            case DAYLIGHT_DETECTOR_INVERTED:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case PURPUR_STAIRS:
            case PURPUR_DOUBLE_SLAB:
            case PURPUR_SLAB:
            case END_BRICKS:
            case GRASS_PATH:
            case STRUCTURE_BLOCK:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN:
            case FROSTED_ICE:
            case MAGMA:
            case NETHER_WART_BLOCK:
            case RED_NETHER_BRICK:
            case BONE_BLOCK:
            case OBSERVER:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case WHITE_GLAZED_TERRACOTTA:
            case ORANGE_GLAZED_TERRACOTTA:
            case MAGENTA_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case YELLOW_GLAZED_TERRACOTTA:
            case LIME_GLAZED_TERRACOTTA:
            case PINK_GLAZED_TERRACOTTA:
            case GRAY_GLAZED_TERRACOTTA:
            case SILVER_GLAZED_TERRACOTTA:
            case CYAN_GLAZED_TERRACOTTA:
            case PURPLE_GLAZED_TERRACOTTA:
            case BLUE_GLAZED_TERRACOTTA:
            case BROWN_GLAZED_TERRACOTTA:
            case GREEN_GLAZED_TERRACOTTA:
            case RED_GLAZED_TERRACOTTA:
            case BLACK_GLAZED_TERRACOTTA:
            case CONCRETE:
            case CONCRETE_POWDER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and does not block any light
     *
     * @return True if this material is a block and does not block any light
     */
    public boolean isTransparent() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            case AIR:
            case SAPLING:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case LONG_GRASS:
            case DEAD_BUSH:
            case YELLOW_FLOWER:
            case RED_ROSE:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case TORCH:
            case FIRE:
            case REDSTONE_WIRE:
            case CROPS:
            case LADDER:
            case RAILS:
            case LEVER:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case STONE_BUTTON:
            case SNOW:
            case SUGAR_CANE_BLOCK:
            case PORTAL:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case VINE:
            case WATER_LILY:
            case NETHER_WARTS:
            case ENDER_PORTAL:
            case COCOA:
            case TRIPWIRE_HOOK:
            case TRIPWIRE:
            case FLOWER_POT:
            case CARROT:
            case POTATO:
            case WOOD_BUTTON:
            case SKULL:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case ACTIVATOR_RAIL:
            case CARPET:
            case DOUBLE_PLANT:
            case END_ROD:
            case CHORUS_PLANT:
            case CHORUS_FLOWER:
            case BEETROOT_BLOCK:
            case END_GATEWAY:
            case STRUCTURE_VOID:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and can catch fire
     *
     * @return True if this material is a block and can catch fire
     */
    public boolean isFlammable() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            case WOOD:
            case LOG:
            case LEAVES:
            case NOTE_BLOCK:
            case BED_BLOCK:
            case LONG_GRASS:
            case DEAD_BUSH:
            case WOOL:
            case TNT:
            case BOOKSHELF:
            case WOOD_STAIRS:
            case CHEST:
            case WORKBENCH:
            case SIGN_POST:
            case WOODEN_DOOR:
            case WALL_SIGN:
            case WOOD_PLATE:
            case JUKEBOX:
            case FENCE:
            case TRAP_DOOR:
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2:
            case VINE:
            case FENCE_GATE:
            case WOOD_DOUBLE_STEP:
            case WOOD_STEP:
            case SPRUCE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case TRAPPED_CHEST:
            case DAYLIGHT_DETECTOR:
            case CARPET:
            case LEAVES_2:
            case LOG_2:
            case ACACIA_STAIRS:
            case DARK_OAK_STAIRS:
            case DOUBLE_PLANT:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case SPRUCE_FENCE:
            case BIRCH_FENCE:
            case JUNGLE_FENCE:
            case DARK_OAK_FENCE:
            case ACACIA_FENCE:
            case STANDING_BANNER:
            case WALL_BANNER:
            case DAYLIGHT_DETECTOR_INVERTED:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and can burn away
     *
     * @return True if this material is a block and can burn away
     */
    public boolean isBurnable() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            case WOOD:
            case LOG:
            case LEAVES:
            case LONG_GRASS:
            case WOOL:
            case YELLOW_FLOWER:
            case RED_ROSE:
            case TNT:
            case BOOKSHELF:
            case WOOD_STAIRS:
            case FENCE:
            case VINE:
            case WOOD_DOUBLE_STEP:
            case WOOD_STEP:
            case SPRUCE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case HAY_BLOCK:
            case COAL_BLOCK:
            case LEAVES_2:
            case LOG_2:
            case CARPET:
            case DOUBLE_PLANT:
            case DEAD_BUSH:
            case FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case SPRUCE_FENCE:
            case BIRCH_FENCE:
            case JUNGLE_FENCE:
            case DARK_OAK_FENCE:
            case ACACIA_FENCE:
            case ACACIA_STAIRS:
            case DARK_OAK_STAIRS:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this Material can be used as fuel in a Furnace
     *
     * @return true if this Material can be used as fuel.
     */
    public boolean isFuel() {
        switch (this) {
            case LAVA_BUCKET:
            case COAL_BLOCK:
            case BLAZE_ROD:
            case COAL:
            case BOAT:
            case BOAT_ACACIA:
            case BOAT_BIRCH:
            case BOAT_DARK_OAK:
            case BOAT_JUNGLE:
            case BOAT_SPRUCE:
            case LOG:
            case LOG_2:
            case WOOD:
            case WOOD_PLATE:
            case FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case SPRUCE_FENCE:
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case WOOD_STAIRS:
            case ACACIA_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case DARK_OAK_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case TRAP_DOOR:
            case WORKBENCH:
            case BOOKSHELF:
            case CHEST:
            case TRAPPED_CHEST:
            case DAYLIGHT_DETECTOR:
            case JUKEBOX:
            case NOTE_BLOCK:
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2:
            case BANNER:
            case FISHING_ROD:
            case LADDER:
            case WOOD_SWORD:
            case WOOD_PICKAXE:
            case WOOD_AXE:
            case WOOD_SPADE:
            case WOOD_HOE:
            case BOW:
            case SIGN:
            case WOOD_DOOR:
            case ACACIA_DOOR_ITEM:
            case BIRCH_DOOR_ITEM:
            case DARK_OAK_DOOR_ITEM:
            case JUNGLE_DOOR_ITEM:
            case SPRUCE_DOOR_ITEM:
            case WOOD_STEP:
            case SAPLING:
            case BOWL:
            case STICK:
            case WOOD_BUTTON:
            case WOOL:
            case CARPET:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the material is a block and completely blocks vision
     *
     * @return True if this material is a block and completely blocks vision
     */
    public boolean isOccluding() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            case STONE:
            case GRASS:
            case DIRT:
            case COBBLESTONE:
            case WOOD:
            case BEDROCK:
            case SAND:
            case GRAVEL:
            case GOLD_ORE:
            case IRON_ORE:
            case COAL_ORE:
            case LOG:
            case SPONGE:
            case LAPIS_ORE:
            case LAPIS_BLOCK:
            case DISPENSER:
            case SANDSTONE:
            case NOTE_BLOCK:
            case WOOL:
            case GOLD_BLOCK:
            case IRON_BLOCK:
            case DOUBLE_STEP:
            case BRICK:
            case BOOKSHELF:
            case MOSSY_COBBLESTONE:
            case OBSIDIAN:
            case MOB_SPAWNER:
            case DIAMOND_ORE:
            case DIAMOND_BLOCK:
            case WORKBENCH:
            case FURNACE:
            case BURNING_FURNACE:
            case REDSTONE_ORE:
            case GLOWING_REDSTONE_ORE:
            case SNOW_BLOCK:
            case CLAY:
            case JUKEBOX:
            case PUMPKIN:
            case NETHERRACK:
            case SOUL_SAND:
            case JACK_O_LANTERN:
            case MONSTER_EGGS:
            case SMOOTH_BRICK:
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2:
            case MELON_BLOCK:
            case MYCEL:
            case NETHER_BRICK:
            case ENDER_STONE:
            case REDSTONE_LAMP_OFF:
            case REDSTONE_LAMP_ON:
            case WOOD_DOUBLE_STEP:
            case EMERALD_ORE:
            case EMERALD_BLOCK:
            case COMMAND:
            case QUARTZ_ORE:
            case QUARTZ_BLOCK:
            case DROPPER:
            case STAINED_CLAY:
            case HAY_BLOCK:
            case HARD_CLAY:
            case COAL_BLOCK:
            case LOG_2:
            case PACKED_ICE:
            case SLIME_BLOCK:
            case BARRIER:
            case PRISMARINE:
            case RED_SANDSTONE:
            case DOUBLE_STONE_SLAB2:
            case PURPUR_BLOCK:
            case PURPUR_PILLAR:
            case PURPUR_DOUBLE_SLAB:
            case END_BRICKS:
            case STRUCTURE_BLOCK:
            case COMMAND_REPEATING:
            case COMMAND_CHAIN:
            case MAGMA:
            case NETHER_WART_BLOCK:
            case RED_NETHER_BRICK:
            case BONE_BLOCK:
            case WHITE_GLAZED_TERRACOTTA:
            case ORANGE_GLAZED_TERRACOTTA:
            case MAGENTA_GLAZED_TERRACOTTA:
            case LIGHT_BLUE_GLAZED_TERRACOTTA:
            case YELLOW_GLAZED_TERRACOTTA:
            case LIME_GLAZED_TERRACOTTA:
            case PINK_GLAZED_TERRACOTTA:
            case GRAY_GLAZED_TERRACOTTA:
            case SILVER_GLAZED_TERRACOTTA:
            case CYAN_GLAZED_TERRACOTTA:
            case PURPLE_GLAZED_TERRACOTTA:
            case BLUE_GLAZED_TERRACOTTA:
            case BROWN_GLAZED_TERRACOTTA:
            case GREEN_GLAZED_TERRACOTTA:
            case RED_GLAZED_TERRACOTTA:
            case BLACK_GLAZED_TERRACOTTA:
            case CONCRETE:
            case CONCRETE_POWDER:
                return true;
            default:
                return false;
        }
    }

    /**
     * @return True if this material is affected by gravity.
     */
    public boolean hasGravity() {
        if (!isBlock()) {
            return false;
        }
        switch (this) {
            case SAND:
            case GRAVEL:
            case ANVIL:
            case CONCRETE_POWDER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if this Material is an obtainable item.
     *
     * @return true if this material is an item
     */
    public boolean isItem() {
        switch (this) {
            case ACACIA_DOOR:
            case BED_BLOCK:
            case BEETROOT_BLOCK:
            case BIRCH_DOOR:
            case BREWING_STAND:
            case BURNING_FURNACE:
            case CAKE_BLOCK:
            case CARROT:
            case CAULDRON:
            case COCOA:
            case CROPS:
            case DARK_OAK_DOOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case DOUBLE_STEP:
            case DOUBLE_STONE_SLAB2:
            case ENDER_PORTAL:
            case END_GATEWAY:
            case FIRE:
            case FLOWER_POT:
            case FROSTED_ICE:
            case GLOWING_REDSTONE_ORE:
            case IRON_DOOR_BLOCK:
            case JUNGLE_DOOR:
            case LAVA:
            case MELON_STEM:
            case NETHER_WARTS:
            case PISTON_EXTENSION:
            case PISTON_MOVING_PIECE:
            case PORTAL:
            case POTATO:
            case PUMPKIN_STEM:
            case PURPUR_DOUBLE_SLAB:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_LAMP_ON:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_WIRE:
            case SIGN_POST:
            case SKULL:
            case SPRUCE_DOOR:
            case STANDING_BANNER:
            case STATIONARY_LAVA:
            case STATIONARY_WATER:
            case SUGAR_CANE_BLOCK:
            case TRIPWIRE:
            case WALL_BANNER:
            case WALL_SIGN:
            case WATER:
            case WOODEN_DOOR:
            case WOOD_DOUBLE_STEP:
                return false;
            default:
                return true;
        }
    }

    // CatServer start
	@Nullable
    public static Material addMaterial(Material material) {
        if (byId[material.id] == null) {
            byId[material.id] = material;
            BY_NAME.put(material.name().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", ""), material);
            BY_NAME.put("X" + String.valueOf(material.id), material);
            return material;
        }
        return null;
    }

	@Nullable
    public static Material addBlockMaterial(Material material) {
        if (blockById[material.id] == null) {
            blockById[material.id] = material;
            BLOCK_BY_NAME.put(material.name().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", ""), material);
            BLOCK_BY_NAME.put("X" + String.valueOf(material.id), material);
            return material;
        }
        return null;
    }

    public static Material getBlockMaterial(final int id) {
        if (blockById.length > id && id >= 0) {
            return blockById[id];
        } else {
            return null;
        }
    }

    public static Material getBlockMaterial(final String name) {
        return BLOCK_BY_NAME.get(name);
    }
    // CatServer end
}
