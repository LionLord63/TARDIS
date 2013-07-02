/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISAdminMenuInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The architectural reconfiguration system is a component of the Doctor's
 * TARDIS in the shape of a tree that, according to the Eleventh Doctor,
 * "reconstructs the particles according to your needs." It is basically "a
 * machine that makes machines," perhaps somewhat like a 3D printer. It is,
 * according to Gregor Van Baalen's scanner, "more valuable than the total sum
 * of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISAdminMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISAdminMenuListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        int inhand = p.getItemInHand().getTypeId();
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && inhand == plugin.getConfig().getInt("admin_item") && p.hasPermission("tardis.admin")) {
            ItemStack[] items = new TARDISAdminMenuInventory(plugin).getMenu();
            Inventory menu = plugin.getServer().createInventory(p, 54, "§4Admin Menu");
            menu.setContents(items);
            p.openInventory(menu);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onARSTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Admin Menu")) {
            int slot = event.getRawSlot();
            String option = getDisplay(inv, slot);
            boolean bool = plugin.getConfig().getBoolean(option);
            plugin.getConfig().set(option, !bool);
            String lore = (bool) ? "false" : "true";
            setLore(inv, slot, lore);
            plugin.saveConfig();
            event.setCancelled(true);
        }
    }

    private String getDisplay(Inventory inv, int slot) {
        ItemStack is = inv.getItem(slot);
        ItemMeta im = is.getItemMeta();
        return im.getDisplayName();
    }

    private void setLore(Inventory inv, int slot, String str) {
        List<String> lore = (str != null) ? Arrays.asList(new String[]{str}) : null;
        ItemStack is = inv.getItem(slot);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }
}