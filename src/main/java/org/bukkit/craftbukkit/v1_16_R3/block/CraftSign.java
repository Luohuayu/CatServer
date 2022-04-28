package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

public class CraftSign extends CraftBlockEntityState<SignTileEntity> implements Sign {

    // Lazily initialized only if requested:
    private String[] originalLines = null;
    private String[] lines = null;

    public CraftSign(final Block block) {
        super(block, SignTileEntity.class);
    }

    public CraftSign(final Material material, final SignTileEntity te) {
        super(material, te);
    }

    @Override
    public String[] getLines() {
        if (lines == null) {
            // Lazy initialization:
            SignTileEntity sign = this.getSnapshot();
            lines = new String[sign.messages.length];
            System.arraycopy(revertComponents(sign.messages), 0, lines, 0, lines.length);
            originalLines = new String[lines.length];
            System.arraycopy(lines, 0, originalLines, 0, originalLines.length);
        }
        return lines;
    }

    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        return getLines()[index];
    }

    @Override
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        getLines()[index] = line;
    }

    @Override
    public boolean isEditable() {
        return getSnapshot().isEditable;
    }

    @Override
    public void setEditable(boolean editable) {
        getSnapshot().isEditable = editable;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getSnapshot().getColor().getId());
    }

    @Override
    public void setColor(DyeColor color) {
        getSnapshot().setColor(net.minecraft.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public void applyTo(SignTileEntity sign) {
        super.applyTo(sign);

        if (lines != null) {
            for (int i = 0; i < lines.length; i++) {
                String line = (lines[i] == null) ? "" : lines[i];
                if (line.equals(originalLines[i])) {
                    continue; // The line contents are still the same, skip.
                }
                sign.messages[i] = CraftChatMessage.fromString(line)[0];
            }
        }
    }

    public static ITextComponent[] sanitizeLines(String[] lines) {
        ITextComponent[] components = new ITextComponent[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = new StringTextComponent("");
            }
        }

        return components;
    }

    public static String[] revertComponents(ITextComponent[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(ITextComponent component) {
        return CraftChatMessage.fromComponent(component);
    }
}
