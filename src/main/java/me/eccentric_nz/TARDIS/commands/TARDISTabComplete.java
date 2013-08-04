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
package me.eccentric_nz.TARDIS.commands;

import com.google.common.collect.ImmutableList;
import java.util.*;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardis command
 */
public class TARDISTabComplete implements TabCompleter {

    private TARDIS plugin;
    private final List<String> ROOT_SUBS = ImmutableList.of("abort", "add", "chameleon", "comehere", "direction", "exterminate", "find", "help", "hide", "home", "inside", "jettison", "list", "namekey", "occupy", "rebuild", "remove", "removesave", "rescue", "room", "save", "secondary", "setdest", "update", "version");
    private final List<String> CHAM_SUBS = ImmutableList.of("on", "off", "short", "reset");
    private final List<String> DIR_SUBS = ImmutableList.of("north", "west", "south", "east");
    private final List<String> LIST_SUBS = ImmutableList.of("companions", "saves", "areas", "rechargers");
    private final List<String> SEC_SUBS = ImmutableList.of("button", "world-repeater", "x-repeater", "z-repeater", "y-repeater", "artron", "handbrake", "door", "back");
    private final List<String> UPD_SUBS = ImmutableList.of("door", "button", "world-repeater", "x-repeater", "z-repeater", "y-repeater", "chameleon", "save-sign", "artron", "handbrake", "condenser", "scanner", "backdoor", "keyboard", "creeper", "eps", "back", "terminal", "ars", "temporal", "light", "farm", "stable", "rail");

    public TARDISTabComplete(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];

        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("add") || sub.equals("remove")) {
                return null;
            } else if (sub.equals("chameleon")) {
                return partial(lastArg, CHAM_SUBS);
            } else if (sub.equals("direction")) {
                return partial(lastArg, DIR_SUBS);
            } else if (sub.equals("list")) {
                return partial(lastArg, LIST_SUBS);
            } else if (sub.equals("rescue")) {
                return null;
            } else if (sub.equals("room") || sub.equals("jettison")) {
                return partial(lastArg, plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false));
            } else if (sub.equals("secondary")) {
                return partial(lastArg, SEC_SUBS);
            } else if (sub.equals("update")) {
                return partial(lastArg, UPD_SUBS);
            }
        } else if (args.length == 3) {
            String sub = args[1];
            if (sub.equals("rechargers")) {
                return partial(lastArg, plugin.getConfig().getConfigurationSection("rechargers").getKeys(false));
            }
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }
}
