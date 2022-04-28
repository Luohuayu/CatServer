package org.bukkit.craftbukkit.v1_16_R3.block.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateHolder;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.registry.Registry;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.CraftSoundGroup;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

public class CraftBlockData implements BlockData {

    private BlockState state;
    private Map<Property<?>, Comparable<?>> parsedStates;

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
     * Set a given {@link BlockStateEnum} with the matching enum from Bukkit.
     *
     * @param nms the NMS BlockStateEnum to set
     * @param bukkit the matching Bukkit Enum
     * @param <B> the Bukkit type
     * @param <N> the NMS type
     */
    protected <B extends Enum<B>, N extends Enum<N> & IStringSerializable> void set(EnumProperty<N> nms, Enum<B> bukkit) {
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

        for (Property parsed : craft.parsedStates.keySet()) {
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
    private static <N extends Enum<N> & IStringSerializable> N toNMS(Enum<?> bukkit, Class<N> nms) {
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
    protected <T extends Comparable<T>> T get(Property<T> ibs) {
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
    public <T extends Comparable<T>, V extends T> void set(Property<T> ibs, V v) {
        // Straight integer or boolean setter
        this.parsedStates = null;
        this.state = this.state.setValue(ibs, v);
    }

    @Override
    public String getAsString() {
        return toString(((StateHolder) state).getValues());
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

    // Mimicked from StateHolder#toString()
    public String toString(Map<Property<?>, Comparable<?>> states) {
        StringBuilder stateString = new StringBuilder(Registry.BLOCK.getKey(state.getBlock()).toString());

        if (!states.isEmpty()) {
            stateString.append('[');
            stateString.append(states.entrySet().stream().map(StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            stateString.append(']');
        }

        return stateString.toString();
    }

    public CompoundNBT toStates() {
        CompoundNBT compound = new CompoundNBT();

        for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
            Property iblockstate = (Property) entry.getKey();

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
     * Get a specified {@link IBlockState} from a given block's class with a
     * given name
     *
     * @param block the class to retrieve the state from
     * @param name the name of the state to retrieve
     * @param optional if the state can be null
     * @return the specified state or null
     * @throws IllegalStateException if the state is null and {@code optional}
     * is false.
     */
    private static Property<?> getState(Class<? extends Block> block, String name, boolean optional) {
        Property<?> state = null;

        for (Block instance : Registry.BLOCK) {
            if (instance.getClass() == block) {
                if (state == null) {
                    state = instance.getStateDefinition().getProperty(name);
                } else {
                    Property<?> newState = instance.getStateDefinition().getProperty(name);

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
        register(net.minecraft.block.AnvilBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftAnvil::new);
        register(net.minecraft.block.BambooBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBamboo::new);
        register(net.minecraft.block.BannerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBanner::new);
        register(net.minecraft.block.WallBannerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBannerWall::new);
        register(net.minecraft.block.BarrelBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBarrel::new);
        register(net.minecraft.block.BedBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBed::new);
        register(net.minecraft.block.BeehiveBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBeehive::new);
        register(net.minecraft.block.BeetrootBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBeetroot::new);
        register(net.minecraft.block.BellBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBell::new);
        register(net.minecraft.block.BlastFurnaceBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBlastFurnace::new);
        register(net.minecraft.block.BrewingStandBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBrewingStand::new);
        register(net.minecraft.block.BubbleColumnBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftBubbleColumn::new);
        register(net.minecraft.block.CactusBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCactus::new);
        register(net.minecraft.block.CakeBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCake::new);
        register(net.minecraft.block.CampfireBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCampfire::new);
        register(net.minecraft.block.CarrotBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCarrots::new);
        register(net.minecraft.block.CauldronBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCauldron::new);
        register(net.minecraft.block.ChainBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftChain::new);
        register(net.minecraft.block.ChestBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftChest::new);
        register(net.minecraft.block.TrappedChestBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftChestTrapped::new);
        register(net.minecraft.block.ChorusFlowerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftChorusFlower::new);
        register(net.minecraft.block.ChorusPlantBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftChorusFruit::new);
        register(net.minecraft.block.WallBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCobbleWall::new);
        register(net.minecraft.block.CocoaBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCocoa::new);
        register(net.minecraft.block.CommandBlockBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCommand::new);
        register(net.minecraft.block.ComposterBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftComposter::new);
        register(net.minecraft.block.ConduitBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftConduit::new);
        register(net.minecraft.block.DeadCoralPlantBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCoralDead::new);
        register(net.minecraft.block.CoralFinBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCoralFan::new);
        register(net.minecraft.block.CoralFanBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCoralFanAbstract::new);
        register(net.minecraft.block.CoralWallFanBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCoralFanWall::new);
        register(net.minecraft.block.DeadCoralWallFanBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCoralFanWallAbstract::new);
        register(net.minecraft.block.CoralPlantBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCoralPlant::new);
        register(net.minecraft.block.CropsBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftCrops::new);
        register(net.minecraft.block.DaylightDetectorBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftDaylightDetector::new);
        register(net.minecraft.block.SnowyDirtBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftDirtSnow::new);
        register(net.minecraft.block.DispenserBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftDispenser::new);
        register(net.minecraft.block.DoorBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftDoor::new);
        register(net.minecraft.block.DropperBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftDropper::new);
        register(net.minecraft.block.EnderChestBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftEnderChest::new);
        register(net.minecraft.block.EndPortalFrameBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftEnderPortalFrame::new);
        register(net.minecraft.block.EndRodBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftEndRod::new);
        register(net.minecraft.block.FenceBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftFence::new);
        register(net.minecraft.block.FenceGateBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftFenceGate::new);
        register(net.minecraft.block.FireBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftFire::new);
        register(net.minecraft.block.StandingSignBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftFloorSign::new);
        register(net.minecraft.block.FlowingFluidBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftFluids::new);
        register(net.minecraft.block.FurnaceBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftFurnaceFurace::new);
        register(net.minecraft.block.GlazedTerracottaBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftGlazedTerracotta::new);
        register(net.minecraft.block.GrassBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftGrass::new);
        register(net.minecraft.block.GrindstoneBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftGrindstone::new);
        register(net.minecraft.block.HayBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftHay::new);
        register(net.minecraft.block.HopperBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftHopper::new);
        register(net.minecraft.block.HugeMushroomBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftHugeMushroom::new);
        register(net.minecraft.block.FrostedIceBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftIceFrost::new);
        register(net.minecraft.block.PaneBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftIronBars::new);
        register(net.minecraft.block.JigsawBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftJigsaw::new);
        register(net.minecraft.block.JukeboxBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftJukeBox::new);
        register(net.minecraft.block.KelpBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftKelp::new);
        register(net.minecraft.block.LadderBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftLadder::new);
        register(net.minecraft.block.LanternBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftLantern::new);
        register(net.minecraft.block.LeavesBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftLeaves::new);
        register(net.minecraft.block.LecternBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftLectern::new);
        register(net.minecraft.block.LeverBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftLever::new);
        register(net.minecraft.block.LoomBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftLoom::new);
        register(net.minecraft.block.DetectorRailBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftMinecartDetector::new);
        register(net.minecraft.block.RailBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftMinecartTrack::new);
        register(net.minecraft.block.MyceliumBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftMycel::new);
        register(net.minecraft.block.NetherWartBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftNetherWart::new);
        register(net.minecraft.block.NoteBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftNote::new);
        register(net.minecraft.block.ObserverBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftObserver::new);
        register(net.minecraft.block.PistonBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPiston::new);
        register(net.minecraft.block.PistonHeadBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPistonExtension::new);
        register(net.minecraft.block.MovingPistonBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPistonMoving::new);
        register(net.minecraft.block.NetherPortalBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPortal::new);
        register(net.minecraft.block.PotatoBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPotatoes::new);
        register(net.minecraft.block.PoweredRailBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPoweredRail::new);
        register(net.minecraft.block.PressurePlateBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPressurePlateBinary::new);
        register(net.minecraft.block.WeightedPressurePlateBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPressurePlateWeighted::new);
        register(net.minecraft.block.CarvedPumpkinBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftPumpkinCarved::new);
        register(net.minecraft.block.ComparatorBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRedstoneComparator::new);
        register(net.minecraft.block.RedstoneLampBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRedstoneLamp::new);
        register(net.minecraft.block.RedstoneOreBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRedstoneOre::new);
        register(net.minecraft.block.RedstoneTorchBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRedstoneTorch::new);
        register(net.minecraft.block.RedstoneWallTorchBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRedstoneTorchWall::new);
        register(net.minecraft.block.RedstoneWireBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRedstoneWire::new);
        register(net.minecraft.block.SugarCaneBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftReed::new);
        register(net.minecraft.block.RepeaterBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRepeater::new);
        register(net.minecraft.block.RespawnAnchorBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRespawnAnchor::new);
        register(net.minecraft.block.RotatedPillarBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftRotatable::new);
        register(net.minecraft.block.SaplingBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSapling::new);
        register(net.minecraft.block.ScaffoldingBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftScaffolding::new);
        register(net.minecraft.block.SeaPickleBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSeaPickle::new);
        register(net.minecraft.block.ShulkerBoxBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftShulkerBox::new);
        register(net.minecraft.block.SkullBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSkull::new);
        register(net.minecraft.block.SkullPlayerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSkullPlayer::new);
        register(net.minecraft.block.SkullWallPlayerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSkullPlayerWall::new);
        register(net.minecraft.block.WallSkullBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSkullWall::new);
        register(net.minecraft.block.SmokerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSmoker::new);
        register(net.minecraft.block.SnowBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSnow::new);
        register(net.minecraft.block.FarmlandBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSoil::new);
        register(net.minecraft.block.StainedGlassPaneBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStainedGlassPane::new);
        register(net.minecraft.block.StairsBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStairs::new);
        register(net.minecraft.block.StemBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStem::new);
        register(net.minecraft.block.AttachedStemBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStemAttached::new);
        register(net.minecraft.block.SlabBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStepAbstract::new);
        register(net.minecraft.block.StoneButtonBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStoneButton::new);
        register(net.minecraft.block.StonecutterBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStonecutter::new);
        register(net.minecraft.block.StructureBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftStructure::new);
        register(net.minecraft.block.SweetBerryBushBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftSweetBerryBush::new);
        register(net.minecraft.block.DoublePlantBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTallPlant::new);
        register(net.minecraft.block.TallFlowerBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTallPlantFlower::new);
        register(net.minecraft.block.TallSeaGrassBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTallSeaGrass::new);
        register(net.minecraft.block.TargetBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTarget::new);
        register(net.minecraft.block.TNTBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTNT::new);
        register(net.minecraft.block.WallTorchBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTorchWall::new);
        register(net.minecraft.block.TrapDoorBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTrapdoor::new);
        register(net.minecraft.block.TripWireBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTripwire::new);
        register(net.minecraft.block.TripWireHookBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTripwireHook::new);
        register(net.minecraft.block.TurtleEggBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTurtleEgg::new);
        register(net.minecraft.block.TwistingVinesTopBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftTwistingVines::new);
        register(net.minecraft.block.VineBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftVine::new);
        register(net.minecraft.block.WallSignBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftWallSign::new);
        register(net.minecraft.block.WeepingVinesTopBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftWeepingVines::new);
        register(net.minecraft.block.WitherSkeletonSkullBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftWitherSkull::new);
        register(net.minecraft.block.WitherSkeletonWallSkullBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftWitherSkullWall::new);
        register(net.minecraft.block.WoodButtonBlock.class, org.bukkit.craftbukkit.v1_16_R3.block.impl.CraftWoodButton::new);
        //</editor-fold>
    }

    private static void register(Class<? extends Block> nms, Function<BlockState, CraftBlockData> bukkit) {
        Preconditions.checkState(MAP.put(nms, bukkit) == null, "Duplicate mapping %s->%s", nms, bukkit);
    }

    public static CraftBlockData newData(Material material, String data) {
        Preconditions.checkArgument(material == null || material.isBlock(), "Cannot get data for not block %s", material);

        BlockState blockData;
        Block block = CraftMagicNumbers.getBlock(material);
        Map<Property<?>, Comparable<?>> parsed = null;

        // Data provided, use it
        if (data != null) {
            try {
                // Material provided, force that material in
                if (block != null) {
                    data = Registry.BLOCK.getKey(block) + data;
                }

                StringReader reader = new StringReader(data);
                BlockStateParser arg = new BlockStateParser(reader, false).parse(false);
                Preconditions.checkArgument(!reader.canRead(), "Spurious trailing data: " + data);

                blockData = arg.getState();
                parsed = arg.getProperties();
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

    @Override
    public SoundGroup getSoundGroup() {
        return CraftSoundGroup.getSoundGroup(state.getSoundType());
    }
}
