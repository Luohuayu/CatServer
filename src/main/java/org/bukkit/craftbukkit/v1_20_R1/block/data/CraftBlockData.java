package org.bukkit.craftbukkit.v1_20_R1.block.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.craftbukkit.v1_20_R1.CraftSoundGroup;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlockStates;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlockSupport;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftLocation;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftBlockData implements BlockData {

    private BlockState state;
    private Map<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> parsedStates;

    protected CraftBlockData() {
        throw new AssertionError("Template Constructor");
    }

    protected CraftBlockData(BlockState state) {
        this.state = state;
    }

    @Override
    public Material getMaterial() {
        return CraftMagicNumbers.getMaterial(state.getBlock());
    }

    public BlockState getState() {
        return state;
    }

    /**
     * Get a given BlockStateEnum's value as its Bukkit counterpart.
     *
     * @param nms the NMS state to convert
     * @param bukkit the Bukkit class
     * @param <B> the type
     * @return the matching Bukkit type
     */
    protected <B extends Enum<B>> B get(EnumProperty<?> nms, Class<B> bukkit) {
        return toBukkit(state.getValue(nms), bukkit);
    }

    /**
     * Convert all values from the given BlockStateEnum to their appropriate
     * Bukkit counterpart.
     *
     * @param nms the NMS state to get values from
     * @param bukkit the bukkit class to convert the values to
     * @param <B> the bukkit class type
     * @return an immutable Set of values in their appropriate Bukkit type
     */
    @SuppressWarnings("unchecked")
    protected <B extends Enum<B>> Set<B> getValues(EnumProperty<?> nms, Class<B> bukkit) {
        ImmutableSet.Builder<B> values = ImmutableSet.builder();

        for (Enum<?> e : nms.getPossibleValues()) {
            values.add(toBukkit(e, bukkit));
        }

        return values.build();
    }

    /**
     * Set a given {@link net.minecraft.world.level.block.state.properties.EnumProperty} with the matching enum from Bukkit.
     *
     * @param nms the NMS BlockStateEnum to set
     * @param bukkit the matching Bukkit Enum
     * @param <B> the Bukkit type
     * @param <N> the NMS type
     */
    protected <B extends Enum<B>, N extends Enum<N> & StringRepresentable> void set(EnumProperty<N> nms, Enum<B> bukkit) {
        this.parsedStates = null;
        this.state = this.state.setValue(nms, toNMS(bukkit, nms.getValueClass()));
    }

    @Override
    public BlockData merge(BlockData data) {
        CraftBlockData craft = (CraftBlockData) data;
        Preconditions.checkArgument(craft.parsedStates != null, "Data not created via string parsing");
        Preconditions.checkArgument(this.state.getBlock() == craft.state.getBlock(), "States have different types (got %s, expected %s)", data, this);

        CraftBlockData clone = (CraftBlockData) this.clone();
        clone.parsedStates = null;

        for (net.minecraft.world.level.block.state.properties.Property parsed : craft.parsedStates.keySet()) {
            clone.state = clone.state.setValue(parsed, craft.state.getValue(parsed));
        }

        return clone;
    }

    @Override
    public boolean matches(BlockData data) {
        if (data == null) {
            return false;
        }
        if (!(data instanceof CraftBlockData)) {
            return false;
        }

        CraftBlockData craft = (CraftBlockData) data;
        if (this.state.getBlock() != craft.state.getBlock()) {
            return false;
        }

        // Fastpath an exact match
        boolean exactMatch = this.equals(data);

        // If that failed, do a merge and check
        if (!exactMatch && craft.parsedStates != null) {
            return this.merge(data).equals(this);
        }

        return exactMatch;
    }

    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> ENUM_VALUES = new HashMap<>();

    /**
     * Convert an NMS Enum (usually a BlockStateEnum) to its appropriate Bukkit
     * enum from the given class.
     *
     * @throws IllegalStateException if the Enum could not be converted
     */
    @SuppressWarnings("unchecked")
    private static <B extends Enum<B>> B toBukkit(Enum<?> nms, Class<B> bukkit) {
        if (nms instanceof Direction) {
            return (B) CraftBlock.notchToBlockFace((Direction) nms);
        }
        return (B) ENUM_VALUES.computeIfAbsent(bukkit, Class::getEnumConstants)[nms.ordinal()];
    }

    /**
     * Convert a given Bukkit enum to its matching NMS enum type.
     *
     * @param bukkit the Bukkit enum to convert
     * @param nms the NMS class
     * @return the matching NMS type
     * @throws IllegalStateException if the Enum could not be converted
     */
    @SuppressWarnings("unchecked")
    private static <N extends Enum<N> & StringRepresentable> N toNMS(Enum<?> bukkit, Class<N> nms) {
        if (bukkit instanceof BlockFace) {
            return (N) CraftBlock.blockFaceToNotch((BlockFace) bukkit);
        }
        return (N) ENUM_VALUES.computeIfAbsent(nms, Class::getEnumConstants)[bukkit.ordinal()];
    }

    /**
     * Get the current value of a given state.
     *
     * @param ibs the state to check
     * @param <T> the type
     * @return the current value of the given state
     */
    protected <T extends Comparable<T>> T get(net.minecraft.world.level.block.state.properties.Property<T> ibs) {
        // Straight integer or boolean getter
        return this.state.getValue(ibs);
    }

    /**
     * Set the specified state's value.
     *
     * @param ibs the state to set
     * @param v the new value
     * @param <T> the state's type
     * @param <V> the value's type. Must match the state's type.
     */
    public <T extends Comparable<T>, V extends T> void set(net.minecraft.world.level.block.state.properties.Property<T> ibs, V v) {
        // Straight integer or boolean setter
        this.parsedStates = null;
        this.state = this.state.setValue(ibs, v);
    }

    @Override
    public String getAsString() {
        return toString(state.getValues());
    }

    @Override
    public String getAsString(boolean hideUnspecified) {
        return (hideUnspecified && parsedStates != null) ? toString(parsedStates) : getAsString();
    }

    @Override
    public BlockData clone() {
        try {
            return (BlockData) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError("Clone not supported", ex);
        }
    }

    @Override
    public String toString() {
        return "CraftBlockData{" + getAsString() + "}";
    }

    // Mimicked from BlockDataAbstract#toString()
    public String toString(Map<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> states) {
        StringBuilder stateString = new StringBuilder(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());

        if (!states.isEmpty()) {
            stateString.append('[');
            stateString.append(states.entrySet().stream().map(StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            stateString.append(']');
        }

        return stateString.toString();
    }

    public CompoundTag toStates() {
        CompoundTag compound = new CompoundTag();

        for (Map.Entry<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
            net.minecraft.world.level.block.state.properties.Property iblockstate = (net.minecraft.world.level.block.state.properties.Property) entry.getKey();

            compound.putString(iblockstate.getName(), iblockstate.getName(entry.getValue()));
        }

        return compound;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftBlockData && state.equals(((CraftBlockData) obj).state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    protected static BooleanProperty getBoolean(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BooleanProperty getBoolean(String name, boolean optional) {
        throw new AssertionError("Template Method");
    }

    protected static EnumProperty<?> getEnum(String name) {
        throw new AssertionError("Template Method");
    }

    protected static IntegerProperty getInteger(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BooleanProperty getBoolean(Class<? extends Block> block, String name) {
        return (BooleanProperty) getState(block, name, false);
    }

    protected static BooleanProperty getBoolean(Class<? extends Block> block, String name, boolean optional) {
        return (BooleanProperty) getState(block, name, optional);
    }

    protected static EnumProperty<?> getEnum(Class<? extends Block> block, String name) {
        return (EnumProperty<?>) getState(block, name, false);
    }

    protected static IntegerProperty getInteger(Class<? extends Block> block, String name) {
        return (IntegerProperty) getState(block, name, false);
    }

    /**
     * Get a specified {@link net.minecraft.world.level.block.state.properties.Property} from a given block's class with a
     * given name
     *
     * @param block the class to retrieve the state from
     * @param name the name of the state to retrieve
     * @param optional if the state can be null
     * @return the specified state or null
     * @throws IllegalStateException if the state is null and {@code optional}
     * is false.
     */
    private static net.minecraft.world.level.block.state.properties.Property<?> getState(Class<? extends Block> block, String name, boolean optional) {
        net.minecraft.world.level.block.state.properties.Property<?> state = null;

        for (Block instance : BuiltInRegistries.BLOCK) {
            if (instance.getClass() == block) {
                if (state == null) {
                    state = instance.getStateDefinition().getProperty(name);
                } else {
                    net.minecraft.world.level.block.state.properties.Property<?> newState = instance.getStateDefinition().getProperty(name);

                    Preconditions.checkState(state == newState, "State mistmatch %s,%s", state, newState);
                }
            }
        }

        Preconditions.checkState(optional || state != null, "Null state for %s,%s", block, name);

        return state;
    }

    /**
     * Get the minimum value allowed by the BlockStateInteger.
     *
     * @param state the state to check
     * @return the minimum value allowed
     */
    protected static int getMin(IntegerProperty state) {
        return state.min;
    }

    /**
     * Get the maximum value allowed by the BlockStateInteger.
     *
     * @param state the state to check
     * @return the maximum value allowed
     */
    protected static int getMax(IntegerProperty state) {
        return state.max;
    }

    //
    private static final Map<Class<? extends Block>, Function<BlockState, CraftBlockData>> MAP = new HashMap<>();

    static {
        //<editor-fold desc="CraftBlockData Registration" defaultstate="collapsed">
        register(net.minecraft.world.level.block.AmethystClusterBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftAmethystCluster::new);
        register(net.minecraft.world.level.block.BigDripleafBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBigDripleaf::new);
        register(net.minecraft.world.level.block.BigDripleafStemBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBigDripleafStem::new);
        register(net.minecraft.world.level.block.AnvilBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftAnvil::new);
        register(net.minecraft.world.level.block.BambooStalkBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBamboo::new);
        register(net.minecraft.world.level.block.BannerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBanner::new);
        register(net.minecraft.world.level.block.WallBannerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBannerWall::new);
        register(net.minecraft.world.level.block.BarrelBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBarrel::new);
        register(net.minecraft.world.level.block.BedBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBed::new);
        register(net.minecraft.world.level.block.BeehiveBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBeehive::new);
        register(net.minecraft.world.level.block.BeetrootBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBeetroot::new);
        register(net.minecraft.world.level.block.BellBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBell::new);
        register(net.minecraft.world.level.block.BlastFurnaceBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBlastFurnace::new);
        register(net.minecraft.world.level.block.BrewingStandBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBrewingStand::new);
        register(net.minecraft.world.level.block.BubbleColumnBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBubbleColumn::new);
        register(net.minecraft.world.level.block.ButtonBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftButtonAbstract::new);
        register(net.minecraft.world.level.block.CactusBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCactus::new);
        register(net.minecraft.world.level.block.CakeBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCake::new);
        register(net.minecraft.world.level.block.CampfireBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCampfire::new);
        register(net.minecraft.world.level.block.CarrotBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCarrots::new);
        register(net.minecraft.world.level.block.ChainBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftChain::new);
        register(net.minecraft.world.level.block.ChestBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftChest::new);
        register(net.minecraft.world.level.block.TrappedChestBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftChestTrapped::new);
        register(net.minecraft.world.level.block.ChorusFlowerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftChorusFlower::new);
        register(net.minecraft.world.level.block.ChorusPlantBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftChorusFruit::new);
        register(net.minecraft.world.level.block.WallBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCobbleWall::new);
        register(net.minecraft.world.level.block.CocoaBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCocoa::new);
        register(net.minecraft.world.level.block.CommandBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCommand::new);
        register(net.minecraft.world.level.block.ComposterBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftComposter::new);
        register(net.minecraft.world.level.block.ConduitBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftConduit::new);
        register(net.minecraft.world.level.block.BaseCoralPlantBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCoralDead::new);
        register(net.minecraft.world.level.block.CoralFanBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCoralFan::new);
        register(net.minecraft.world.level.block.BaseCoralFanBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCoralFanAbstract::new);
        register(net.minecraft.world.level.block.CoralWallFanBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCoralFanWall::new);
        register(net.minecraft.world.level.block.BaseCoralWallFanBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCoralFanWallAbstract::new);
        register(net.minecraft.world.level.block.CoralPlantBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCoralPlant::new);
        register(net.minecraft.world.level.block.CropBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCrops::new);
        register(net.minecraft.world.level.block.DaylightDetectorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftDaylightDetector::new);
        register(net.minecraft.world.level.block.SnowyDirtBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftDirtSnow::new);
        register(net.minecraft.world.level.block.DispenserBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftDispenser::new);
        register(net.minecraft.world.level.block.DoorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftDoor::new);
        register(net.minecraft.world.level.block.DropperBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftDropper::new);
        register(net.minecraft.world.level.block.EndRodBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftEndRod::new);
        register(net.minecraft.world.level.block.EnderChestBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftEnderChest::new);
        register(net.minecraft.world.level.block.EndPortalFrameBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftEnderPortalFrame::new);
        register(net.minecraft.world.level.block.FenceBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftFence::new);
        register(net.minecraft.world.level.block.FenceGateBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftFenceGate::new);
        register(net.minecraft.world.level.block.FireBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftFire::new);
        register(net.minecraft.world.level.block.StandingSignBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftFloorSign::new);
        register(net.minecraft.world.level.block.LiquidBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftFluids::new);
        register(net.minecraft.world.level.block.FurnaceBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftFurnaceFurace::new);
        register(net.minecraft.world.level.block.GlazedTerracottaBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftGlazedTerracotta::new);
        register(net.minecraft.world.level.block.GrassBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftGrass::new);
        register(net.minecraft.world.level.block.GrindstoneBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftGrindstone::new);
        register(net.minecraft.world.level.block.HayBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftHay::new);
        register(net.minecraft.world.level.block.HopperBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftHopper::new);
        register(net.minecraft.world.level.block.HugeMushroomBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftHugeMushroom::new);
        register(net.minecraft.world.level.block.FrostedIceBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftIceFrost::new);
        register(net.minecraft.world.level.block.IronBarsBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftIronBars::new);
        register(net.minecraft.world.level.block.JigsawBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftJigsaw::new);
        register(net.minecraft.world.level.block.JukeboxBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftJukeBox::new);
        register(net.minecraft.world.level.block.KelpBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftKelp::new);
        register(net.minecraft.world.level.block.LadderBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLadder::new);
        register(net.minecraft.world.level.block.LanternBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLantern::new);
        register(net.minecraft.world.level.block.LeavesBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLeaves::new);
        register(net.minecraft.world.level.block.LecternBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLectern::new);
        register(net.minecraft.world.level.block.LeverBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLever::new);
        register(net.minecraft.world.level.block.LoomBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLoom::new);
        register(net.minecraft.world.level.block.DetectorRailBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftMinecartDetector::new);
        register(net.minecraft.world.level.block.RailBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftMinecartTrack::new);
        register(net.minecraft.world.level.block.MyceliumBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftMycel::new);
        register(net.minecraft.world.level.block.NetherWartBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftNetherWart::new);
        register(net.minecraft.world.level.block.NoteBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftNote::new);
        register(net.minecraft.world.level.block.ObserverBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftObserver::new);
        register(net.minecraft.world.level.block.NetherPortalBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPortal::new);
        register(net.minecraft.world.level.block.PotatoBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPotatoes::new);
        register(net.minecraft.world.level.block.PoweredRailBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPoweredRail::new);
        register(net.minecraft.world.level.block.PressurePlateBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPressurePlateBinary::new);
        register(net.minecraft.world.level.block.WeightedPressurePlateBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPressurePlateWeighted::new);
        register(net.minecraft.world.level.block.CarvedPumpkinBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPumpkinCarved::new);
        register(net.minecraft.world.level.block.ComparatorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRedstoneComparator::new);
        register(net.minecraft.world.level.block.RedstoneLampBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRedstoneLamp::new);
        register(net.minecraft.world.level.block.RedStoneOreBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRedstoneOre::new);
        register(net.minecraft.world.level.block.RedstoneTorchBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRedstoneTorch::new);
        register(net.minecraft.world.level.block.RedstoneWallTorchBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRedstoneTorchWall::new);
        register(net.minecraft.world.level.block.RedStoneWireBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRedstoneWire::new);
        register(net.minecraft.world.level.block.SugarCaneBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftReed::new);
        register(net.minecraft.world.level.block.RepeaterBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRepeater::new);
        register(net.minecraft.world.level.block.RespawnAnchorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRespawnAnchor::new);
        register(net.minecraft.world.level.block.RotatedPillarBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftRotatable::new);
        register(net.minecraft.world.level.block.SaplingBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSapling::new);
        register(net.minecraft.world.level.block.ScaffoldingBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftScaffolding::new);
        register(net.minecraft.world.level.block.SeaPickleBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSeaPickle::new);
        register(net.minecraft.world.level.block.ShulkerBoxBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftShulkerBox::new);
        register(net.minecraft.world.level.block.SkullBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSkull::new);
        register(net.minecraft.world.level.block.PlayerHeadBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSkullPlayer::new);
        register(net.minecraft.world.level.block.PlayerWallHeadBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSkullPlayerWall::new);
        register(net.minecraft.world.level.block.WallSkullBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSkullWall::new);
        register(net.minecraft.world.level.block.SmokerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSmoker::new);
        register(net.minecraft.world.level.block.SnowLayerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSnow::new);
        register(net.minecraft.world.level.block.FarmBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSoil::new);
        register(net.minecraft.world.level.block.StainedGlassPaneBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStainedGlassPane::new);
        register(net.minecraft.world.level.block.StairBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStairs::new);
        register(net.minecraft.world.level.block.StemBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStem::new);
        register(net.minecraft.world.level.block.AttachedStemBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStemAttached::new);
        register(net.minecraft.world.level.block.SlabBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStepAbstract::new);
        register(net.minecraft.world.level.block.StonecutterBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStonecutter::new);
        register(net.minecraft.world.level.block.StructureBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftStructure::new);
        register(net.minecraft.world.level.block.SweetBerryBushBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSweetBerryBush::new);
        register(net.minecraft.world.level.block.TntBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTNT::new);
        register(net.minecraft.world.level.block.DoublePlantBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTallPlant::new);
        register(net.minecraft.world.level.block.TallFlowerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTallPlantFlower::new);
        register(net.minecraft.world.level.block.TargetBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTarget::new);
        register(net.minecraft.world.level.block.WallTorchBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTorchWall::new);
        register(net.minecraft.world.level.block.TrapDoorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTrapdoor::new);
        register(net.minecraft.world.level.block.TripWireBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTripwire::new);
        register(net.minecraft.world.level.block.TripWireHookBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTripwireHook::new);
        register(net.minecraft.world.level.block.TurtleEggBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTurtleEgg::new);
        register(net.minecraft.world.level.block.TwistingVinesBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTwistingVines::new);
        register(net.minecraft.world.level.block.VineBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftVine::new);
        register(net.minecraft.world.level.block.WallSignBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWallSign::new);
        register(net.minecraft.world.level.block.WeepingVinesBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWeepingVines::new);
        register(net.minecraft.world.level.block.WitherSkullBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWitherSkull::new);
        register(net.minecraft.world.level.block.WitherWallSkullBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWitherSkullWall::new);
        register(net.minecraft.world.level.block.BrushableBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftBrushable::new);
        register(net.minecraft.world.level.block.CalibratedSculkSensorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCalibratedSculkSensor::new);
        register(net.minecraft.world.level.block.CandleBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCandle::new);
        register(net.minecraft.world.level.block.CandleCakeBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCandleCake::new);
        register(net.minecraft.world.level.block.CaveVinesBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCaveVines::new);
        register(net.minecraft.world.level.block.CaveVinesPlantBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCaveVinesPlant::new);
        register(net.minecraft.world.level.block.CeilingHangingSignBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCeilingHangingSign::new);
        register(net.minecraft.world.level.block.CherryLeavesBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftCherryLeaves::new);
        register(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftChiseledBookShelf::new);
        register(net.minecraft.world.level.block.DecoratedPotBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftDecoratedPot::new);
        register(net.minecraft.world.level.block.EquipableCarvedPumpkinBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftEquipableCarvedPumpkin::new);
        register(net.minecraft.world.level.block.GlowLichenBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftGlowLichen::new);
        register(net.minecraft.world.level.block.HangingRootsBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftHangingRoots::new);
        register(net.minecraft.world.level.block.InfestedRotatedPillarBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftInfestedRotatedPillar::new);
        register(net.minecraft.world.level.block.LayeredCauldronBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLayeredCauldron::new);
        register(net.minecraft.world.level.block.LightBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLight::new);
        register(net.minecraft.world.level.block.LightningRodBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftLightningRod::new);
        register(net.minecraft.world.level.block.MangroveLeavesBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftMangroveLeaves::new);
        register(net.minecraft.world.level.block.MangrovePropaguleBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftMangrovePropagule::new);
        register(net.minecraft.world.level.block.MangroveRootsBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftMangroveRoots::new);
        register(net.minecraft.world.level.block.PiglinWallSkullBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPiglinWallSkull::new);
        register(net.minecraft.world.level.block.PinkPetalsBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPinkPetals::new);
        register(net.minecraft.world.level.block.PitcherCropBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPitcherCrop::new);
        register(net.minecraft.world.level.block.PointedDripstoneBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPointedDripstone::new);
        register(net.minecraft.world.level.block.PowderSnowCauldronBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPowderSnowCauldron::new);
        register(net.minecraft.world.level.block.SculkCatalystBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSculkCatalyst::new);
        register(net.minecraft.world.level.block.SculkSensorBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSculkSensor::new);
        register(net.minecraft.world.level.block.SculkShriekerBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSculkShrieker::new);
        register(net.minecraft.world.level.block.SculkVeinBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSculkVein::new);
        register(net.minecraft.world.level.block.SmallDripleafBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSmallDripleaf::new);
        register(net.minecraft.world.level.block.SnifferEggBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftSnifferEgg::new);
        register(net.minecraft.world.level.block.TallSeagrassBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTallSeagrass::new);
        register(net.minecraft.world.level.block.TorchflowerCropBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftTorchflowerCrop::new);
        register(net.minecraft.world.level.block.WallHangingSignBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWallHangingSign::new);
        register(net.minecraft.world.level.block.WeatheringCopperSlabBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWeatheringCopperSlab::new);
        register(net.minecraft.world.level.block.WeatheringCopperStairBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftWeatheringCopperStair::new);
        register(net.minecraft.world.level.block.piston.PistonBaseBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPiston::new);
        register(net.minecraft.world.level.block.piston.PistonHeadBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPistonExtension::new);
        register(net.minecraft.world.level.block.piston.MovingPistonBlock.class, org.bukkit.craftbukkit.v1_20_R1.block.impl.CraftPistonMoving::new);
        //</editor-fold>
    }

    private static void register(Class<? extends Block> nms, Function<BlockState, CraftBlockData> bukkit) {
        Preconditions.checkState(MAP.put(nms, bukkit) == null, "Duplicate mapping %s->%s", nms, bukkit);
    }

    public static CraftBlockData newData(Material material, String data) {
        Preconditions.checkArgument(material == null || material.isBlock(), "Cannot get data for not block %s", material);

        BlockState blockData;
        Block block = CraftMagicNumbers.getBlock(material);
        Map<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> parsed = null;

        // Data provided, use it
        if (data != null) {
            try {
                // Material provided, force that material in
                if (block != null) {
                    data = BuiltInRegistries.BLOCK.getKey(block) + data;
                }

                StringReader reader = new StringReader(data);
                BlockStateParser.BlockResult arg = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), reader, false);
                Preconditions.checkArgument(!reader.canRead(), "Spurious trailing data: " + data);

                blockData = arg.blockState();
                parsed = arg.properties();
            } catch (CommandSyntaxException ex) {
                throw new IllegalArgumentException("Could not parse data: " + data, ex);
            }
        } else {
            blockData = block.defaultBlockState();
        }

        CraftBlockData craft = fromData(blockData);
        craft.parsedStates = parsed;
        return craft;
    }

    public static CraftBlockData fromData(BlockState data) {
        return MAP.getOrDefault(data.getBlock().getClass(), CraftBlockData::new).apply(data);
    }

    // CatServer start
    public static Class<?> getClosestBlockDataClass(Class<? extends Block> blockClass) {
        if (MAP.containsKey(blockClass))
            return MAP.get(blockClass).apply(null).getClass();

        // Try obtaining closest CraftBlockData subclass
        Class<?> superClass = blockClass.getSuperclass();
        Class<?> matchedClass = null;
        Function<BlockState, CraftBlockData> matchedFunction = null;

        while (superClass != null) {
            if (MAP.containsKey(superClass)) {
                matchedFunction = MAP.get(superClass);
                matchedClass = matchedFunction.apply(null).getClass();
                break;
            }
            superClass = superClass.getSuperclass();
        }
        if (matchedClass == null)
            return null;
        register(blockClass, matchedFunction);
        return matchedClass;
    }
    // CatServer end

    @Override
    public SoundGroup getSoundGroup() {
        return CraftSoundGroup.getSoundGroup(state.getSoundType());
    }

    @Override
    public int getLightEmission() {
        return state.getLightEmission();
    }

    @Override
    public boolean isOccluding() {
        return state.canOcclude();
    }

    @Override
    public boolean requiresCorrectToolForDrops() {
        return state.requiresCorrectToolForDrops();
    }

    @Override
    public boolean isPreferredTool(ItemStack tool) {
        Preconditions.checkArgument(tool != null, "tool must not be null");

        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(tool);
        return isPreferredTool(state, nms);
    }

    public static boolean isPreferredTool(BlockState iblockdata, net.minecraft.world.item.ItemStack nmsItem) {
        return !iblockdata.requiresCorrectToolForDrops() || nmsItem.isCorrectToolForDrops(iblockdata);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(state.getPistonPushReaction().ordinal());
    }

    @Override
    public boolean isSupported(org.bukkit.block.Block block) {
        Preconditions.checkArgument(block != null, "block must not be null");

        CraftBlock craftBlock = (CraftBlock) block;
        return state.canSurvive(craftBlock.getCraftWorld().getHandle(), craftBlock.getPosition());
    }

    @Override
    public boolean isSupported(Location location) {
        Preconditions.checkArgument(location != null, "location must not be null");

        CraftWorld world = (CraftWorld) location.getWorld();
        Preconditions.checkArgument(world != null, "location must not have a null world");

        BlockPos position = CraftLocation.toBlockPosition(location);
        return state.canSurvive(world.getHandle(), position);
    }

    @Override
    public boolean isFaceSturdy(BlockFace face, BlockSupport support) {
        Preconditions.checkArgument(face != null, "face must not be null");
        Preconditions.checkArgument(support != null, "support must not be null");

        return state.isFaceSturdy(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CraftBlock.blockFaceToNotch(face), CraftBlockSupport.toNMS(support));
    }

    @Override
    public Material getPlacementMaterial() {
        return CraftMagicNumbers.getMaterial(state.getBlock().asItem());
    }

    @Override
    public void rotate(StructureRotation rotation) {
        this.state = state.rotate(Rotation.valueOf(rotation.name()));
    }

    @Override
    public void mirror(org.bukkit.block.structure.Mirror mirror) {
        this.state = state.mirror(Mirror.valueOf(mirror.name()));
    }

    @NotNull
    @Override
    public org.bukkit.block.BlockState createBlockState() {
        return CraftBlockStates.getBlockState(this.state, null);
    }
}
