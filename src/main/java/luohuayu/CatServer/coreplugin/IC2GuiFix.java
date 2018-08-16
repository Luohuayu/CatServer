package luohuayu.CatServer.coreplugin;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;

public class IC2GuiFix {
    public static void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof CraftPlayer)) return;
        CraftPlayer p = (CraftPlayer) e.getWhoClicked();
        if (!p.getHandle().openContainer.getClass().getName().startsWith("ic2.core.item.tool.Container")) return;
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().equals(p.getItemInHand()) || e.getClick().isKeyboardClick()) {
            e.setCancelled(true);
        }
    }
}
