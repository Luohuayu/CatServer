/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.common.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A high-speed implementation of a capability delegator.
 * This is used to wrap the results of the AttachCapabilitiesEvent.
 * It is HIGHLY recommended that you DO NOT use this approach unless
 * you MUST delegate to multiple providers instead just implement y
 * our handlers using normal if statements.
 *
 * Internally the handlers are baked into arrays for fast iteration.
 * The ResourceLocations will be used for the NBT Key when serializing.
 */
public final class CapabilityDispatcher implements INBTSerializable<NBTTagCompound>, ICapabilityProvider
{
    private final FastCapability[] fastCapabilities;
    private int writerCount = 0;

    public CapabilityDispatcher(Map<ResourceLocation, ICapabilityProvider> list)
    {
        this(list, null);
    }

    @SuppressWarnings("unchecked")
    public CapabilityDispatcher(Map<ResourceLocation, ICapabilityProvider> list, @Nullable ICapabilityProvider parent)
    {
        int indexOffset = 0;
        fastCapabilities = new FastCapability[parent != null ? list.size() + 1 : list.size()];

        if (parent != null) // Parents go first!
        {
            FastCapability fastCapability = new FastCapability(parent);
            if (parent instanceof INBTSerializable)
            {
                fastCapability.writer = (INBTSerializable<NBTBase>)parent;
                fastCapability.name = "Parent";
                writerCount++;
            }

            fastCapabilities[indexOffset++] = fastCapability;
        }

        for (Map.Entry<ResourceLocation, ICapabilityProvider> entry : list.entrySet())
        {
            ICapabilityProvider prov = entry.getValue();

            FastCapability fastCapability = new FastCapability(prov);
            if (prov instanceof INBTSerializable)
            {
                fastCapability.writer = (INBTSerializable<NBTBase>)prov;
                fastCapability.name = entry.getKey().toString();
                writerCount++;
            }
            fastCapabilities[indexOffset++] = fastCapability;
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        for (FastCapability fastCapability : fastCapabilities)
        {
            if (fastCapability.cap.hasCapability(capability, facing))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        for (FastCapability fastCapability : fastCapabilities)
        {
            T ret = fastCapability.cap.getCapability(capability, facing);
            if (ret != null)
            {
                return ret;
            }
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        for (FastCapability fastCapability : fastCapabilities)
        {
            if (fastCapability.writer == null) continue;
            nbt.setTag(fastCapability.name, fastCapability.writer.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        for (FastCapability fastCapability : fastCapabilities)
        {
            if (fastCapability.writer == null) continue;
            if (nbt.hasKey(fastCapability.name))
            {
                fastCapability.writer.deserializeNBT(nbt.getTag(fastCapability.name));
            }
        }
    }

    public boolean areCompatible(CapabilityDispatcher other) //Called from ItemStack to compare equality.
    {                                                        // Only compares serializeable caps.
        if (other == null) return this.writerCount == 0;  // Done this way so we can do some pre-checks before doing the costly NBT serialization and compare
        if (this.writerCount == 0) return other.writerCount == 0;
        return this.serializeNBT().equals(other.serializeNBT());
    }

    static class FastCapability {
        public ICapabilityProvider cap;
        public INBTSerializable<NBTBase> writer;
        public String name;

        public FastCapability(ICapabilityProvider cap) {
            this.cap = cap;
        }
    }
}
