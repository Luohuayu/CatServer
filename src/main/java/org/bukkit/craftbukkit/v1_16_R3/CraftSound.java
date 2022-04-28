package org.bukkit.craftbukkit.v1_16_R3;

import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang.Validate;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;

public class CraftSound {

    public static SoundEvent getSoundEffect(String s) {
        SoundEvent effect = Registry.SOUND_EVENT.get(new ResourceLocation(s));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static SoundEvent getSoundEffect(Sound s) {
        SoundEvent effect = Registry.SOUND_EVENT.get(CraftNamespacedKey.toMinecraft(s.getKey()));
        Preconditions.checkArgument(effect != null, "Sound effect %s does not exist", s);

        return effect;
    }

    public static  Sound getBukkit(SoundEvent soundEvent) {
        return org.bukkit.Registry.SOUNDS.get(CraftNamespacedKey.fromMinecraft(Registry.SOUND_EVENT.getKey(soundEvent)));
    }
}
