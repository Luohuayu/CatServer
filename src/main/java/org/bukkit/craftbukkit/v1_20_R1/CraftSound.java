package org.bukkit.craftbukkit.v1_20_R1;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;

public class CraftSound {

    public static SoundEvent getSoundEffect(String s) {
        SoundEvent effect = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(s));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static SoundEvent getSoundEffect(Sound s) {
        SoundEvent effect = BuiltInRegistries.SOUND_EVENT.get(CraftNamespacedKey.toMinecraft(s.getKey()));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static Sound getBukkit(SoundEvent soundEffect) {
        return Registry.SOUNDS.get(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.SOUND_EVENT.getKey(soundEffect)));
    }
}
