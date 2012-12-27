/*
 * Copyright (C) 2012 eccentric_nz
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

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listens for player interaction with the TARDIS console and other specific
 * items. If the block is clicked and players name is contained in the
 * appropriate HashMap, then the blocks position is recorded in the database.
 *
 * @author eccentric_nz
 */
public class TARDISUpdateListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISUpdateListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdateInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();

        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (plugin.trackPlayers.containsKey(playerNameStr)) {
                String blockName = plugin.trackPlayers.get(playerNameStr);
                Location block_loc = block.getLocation();
                String bw = block_loc.getWorld().getName();
                int bx = block_loc.getBlockX();
                int by = block_loc.getBlockY();
                int bz = block_loc.getBlockZ();
                byte blockData = block.getData();
                if (blockData >= 8 && blockType == Material.IRON_DOOR_BLOCK) {
                    by = (by - 1);
                }
                String blockLocStr = bw + ":" + bx + ":" + by + ":" + bz;
                plugin.trackPlayers.remove(playerNameStr);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    player.sendMessage(plugin.pluginName + " " + TARDISConstants.NO_TARDIS);
                    return;
                }
                int id = rs.getTardis_id();
                String home = rs.getHome();
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                String table = "tardis";
                if (blockName.equalsIgnoreCase("door") && blockType == Material.IRON_DOOR_BLOCK) {
                    // get door data this should let us determine the direction
                    String d = "EAST";
                    switch (blockData) {
                        case 0:
                            d = "EAST";
                            break;
                        case 1:
                            d = "SOUTH";
                            break;
                        case 2:
                            d = "WEST";
                            break;
                        case 3:
                            d = "NORTH";
                            break;
                    }
                    table = "doors";
                    set.put("door_location", blockLocStr);
                    set.put("door_direction", d);
                    tid.put("door_type", 1);
                }
                if (blockName.equalsIgnoreCase("button") && blockType == Material.STONE_BUTTON) {
                    set.put("button", blockLocStr);
                }
                if (blockName.equalsIgnoreCase("world-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                    set.put("repeater0", blockLocStr);
                }
                if (blockName.equalsIgnoreCase("x-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                    set.put("repeater1", blockLocStr);
                }
                if (blockName.equalsIgnoreCase("z-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                    set.put("repeater2", blockLocStr);
                }
                if (blockName.equalsIgnoreCase("y-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                    set.put("repeater3", blockLocStr);
                }
                if (blockName.equalsIgnoreCase("chameleon") && (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST)) {
                    set.put("chameleon", blockLocStr);
                    set.put("chamele_on", 0);
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "Chameleon");
                    s.setLine(1, "Circuit");
                    s.setLine(3, ChatColor.RED + "OFF");
                    s.update();
                }
                if (blockName.equalsIgnoreCase("save-sign") && (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST)) {
                    set.put("save_sign", blockLocStr);
                    // add text to sign
                    String[] coords = home.split(":");
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "Saves");
                    s.setLine(1, "Home");
                    s.setLine(2, coords[0]);
                    s.setLine(3, coords[1] + "," + coords[2] + "," + coords[3]);
                    s.update();
                }
                qf.doUpdate(table, set, tid);
                player.sendMessage(plugin.pluginName + " The position of the TARDIS " + blockName + " was updated successfully.");
            }
        }
    }
}