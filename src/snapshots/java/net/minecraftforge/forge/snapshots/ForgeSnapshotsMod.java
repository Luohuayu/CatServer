/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.forge.snapshots;

import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Options;
import org.slf4j.Logger;

public class ForgeSnapshotsMod
{
    public static final String BRANDING_NAME = "Forge";
    public static final String BRANDING_ID = "forge";
    private static final Logger LOGGER = LogUtils.getLogger();
    static boolean seenSnapshotWarning = false;

    public static void processOptions(Options.FieldAccess fieldAccess)
    {
        // seenSnapshotWarning = fieldAccess.process("seenSnapshotWarning", seenSnapshotWarning); // CatServer - remove
    }

    public static void logStartupWarning()
    {
        // LOGGER.warn("Froge is not officially supported. Bugs and instability are expected."); // CatServer - remove
    }

    public static void addCrashReportHeader(StringBuilder builder, CrashReport crashReport)
    {
        // builder.append("---- Please note that Minecraft Forge DOES NOT support Froge builds. Bugs and instability are expected. ----\n"); // CatServer - remove
    }
}
