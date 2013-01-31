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

import java.util.ArrayList;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

/**
 * The Weeping Angels are a species of quantum-locked humanoids from the early
 * universe. They are known for being murderous psychopaths, eradicating their
 * victims "mercifully" by dropping them into the past and letting them live out
 * their full lives, just in a different time period. This, in turn, allows them
 * to live off the remaining time energy of the victim's life.
 *
 * @author eccentric_nz
 */
public class TARDISWitherDragonListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    List<EntityType> ents = new ArrayList<EntityType>();
    Version bukkitversion;
    Version prewitherversion = new Version("1.4.2");

    public TARDISWitherDragonListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = new Version(v[0]);
        ents.add(EntityType.ENDER_DRAGON);
        if (bukkitversion.compareTo(prewitherversion) >= 0) {
            ents.add(EntityType.WITHER);
        }
    }

    /**
     * Listens for Wither and Dragon entity interaction with the TARDIS blocks.
     * If the block is a TARDIS block, then the block change event is canceled.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void bossBlockBreak(EntityChangeBlockEvent event) {
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        EntityType eType;
        try {
            eType = event.getEntityType();
        } catch (Exception e) {
            eType = null;
        }
        if (eType != null && ents.contains(eType)) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("location", l);
            ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
            if (rs.resultSet()) {
                event.setCancelled(true);
            }
        }
    }
}