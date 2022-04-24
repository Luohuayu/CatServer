package org.bukkit.craftbukkit.v1_18_R2;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;

public class CraftSound {

    public static SoundEvent getSoundEffect(String s) {
        SoundEvent effect = net.minecraft.core.Registry.SOUND_EVENT.get(new ResourceLocation(s));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static SoundEvent getSoundEffect(Sound s) {
        SoundEvent effect = net.minecraft.core.Registry.SOUND_EVENT.get(CraftNamespacedKey.toMinecraft(s.getKey()));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static Sound getBukkit(SoundEvent soundEffect) {
        return Registry.SOUNDS.get(CraftNamespacedKey.fromMinecraft(net.minecraft.core.Registry.SOUND_EVENT.getKey(soundEffect)));
    }
}
