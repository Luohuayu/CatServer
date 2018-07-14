// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntitySign;
import org.bukkit.block.Sign;

public class CraftSign extends CraftBlockState implements Sign
{
    private final TileEntitySign sign;
    private final String[] lines;
    
    public CraftSign(final Block block) {
        super(block);
        final CraftWorld world = (CraftWorld)block.getWorld();
        this.sign = (TileEntitySign)world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        this.lines = new String[this.sign.signText.length];
        System.arraycopy(revertComponents(this.sign.signText), 0, this.lines, 0, this.lines.length);
    }
    
    public CraftSign(final Material material, final TileEntitySign te) {
        super(material);
        this.sign = te;
        this.lines = new String[this.sign.signText.length];
        System.arraycopy(revertComponents(this.sign.signText), 0, this.lines, 0, this.lines.length);
    }
    
    @Override
    public String[] getLines() {
        return this.lines;
    }
    
    @Override
    public String getLine(final int index) throws IndexOutOfBoundsException {
        return this.lines[index];
    }
    
    @Override
    public void setLine(final int index, final String line) throws IndexOutOfBoundsException {
        this.lines[index] = line;
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            final ITextComponent[] newLines = sanitizeLines(this.lines);
            System.arraycopy(newLines, 0, this.sign.signText, 0, 4);
            this.sign.markDirty();
        }
        return result;
    }
    
    public static ITextComponent[] sanitizeLines(final String[] lines) {
        final ITextComponent[] components = new ITextComponent[4];
        for (int i = 0; i < 4; ++i) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            }
            else {
                components[i] = new TextComponentString("");
            }
        }
        return components;
    }
    
    public static String[] revertComponents(final ITextComponent[] components) {
        final String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = revertComponent(components[i]);
        }
        return lines;
    }
    
    private static String revertComponent(final ITextComponent component) {
        return CraftChatMessage.fromComponent(component);
    }
    
    @Override
    public TileEntitySign getTileEntity() {
        return this.sign;
    }
}
