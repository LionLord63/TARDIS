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

import me.eccentric_nz.TARDIS.TARDIS;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Believing that he would die on the Game Station, the Ninth Doctor used
 * Emergency Program One to play a message for the benefit of Rose Tyler before
 * the TARDIS returned Rose to her home of Powell Estate in 2006.
 *
 * @author eccentric_nz
 */
public class TARDISNPCListener implements Listener {

    private final TARDIS plugin;

    public TARDISNPCListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking on an NPC. If the NPC is an Emergency Program
     * One NPC then it is removed.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCInteract(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        Integer id = Integer.valueOf(npc.getId());
        if (plugin.npcIDs.contains(id)) {
            npc.destroy();
            event.getClicker().sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + "Bye!");
            plugin.npcIDs.remove(id);
        }
    }
}
