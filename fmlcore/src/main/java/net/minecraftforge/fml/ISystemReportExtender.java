/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml;

import java.util.function.Supplier;

public interface ISystemReportExtender extends Supplier<String>
{
    String getLabel();
}
