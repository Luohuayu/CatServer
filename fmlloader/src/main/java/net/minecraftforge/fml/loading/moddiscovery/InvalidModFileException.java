/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading.moddiscovery;

import java.util.Locale;
import net.minecraftforge.forgespi.language.IModFileInfo;

public class InvalidModFileException extends RuntimeException
{
    private final IModFileInfo modFileInfo;

    public InvalidModFileException(String message, IModFileInfo modFileInfo)
    {
        super(String.format(Locale.ENGLISH,"%s (%s)", message, ((ModFileInfo)modFileInfo).getFile().getFileName()));
        this.modFileInfo = modFileInfo;
    }
}
