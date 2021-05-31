package parallelmc.parallelutils.commands.custommobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import parallelmc.parallelutils.Constants;
import parallelmc.parallelutils.Parallelutils;
import parallelmc.parallelutils.commands.Commands;
import parallelmc.parallelutils.commands.ParallelCommand;
import parallelmc.parallelutils.commands.permissions.ParallelPermission;
import parallelmc.parallelutils.commands.permissions.ParallelOrPermission;
import parallelmc.parallelutils.custommobs.nmsmobs.EntityFireWisp;
import parallelmc.parallelutils.custommobs.nmsmobs.EntityWisp;
import parallelmc.parallelutils.custommobs.nmsmobs.SpawnReason;

import java.util.logging.Level;

/**
 * A command to summon a ParallelUtils custom mob
 * Usage: /pu summon <mob> <x> <y> <z>
 */
public class ParallelSummonCommand extends ParallelCommand {

	public static final String[] SUMMON_MOBS = new String[]{"wisp", "fire_wisp"};

	private final String USAGE = "Usage: /pu summon <mob> <x> <y> <z>";

	public ParallelSummonCommand() {
		super("summon", new ParallelOrPermission(new ParallelPermission[]{new ParallelPermission("parallelutils.spawn"), new ParallelPermission("parallelutils.spawn.summon")}));
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args) {
		if (args.length <= 1) {
			String options = "Options:\n";
			for (String s : SUMMON_MOBS) {
				options += s + "\n";
			}
			sender.sendMessage(options);
			return true;
		}

		PluginManager manager = Bukkit.getPluginManager();
		JavaPlugin plugin = (JavaPlugin) manager.getPlugin(Constants.PLUGIN_NAME);

		if (plugin == null) {
			Parallelutils.log(Level.SEVERE, "Unable to execute command 'summon'. Plugin "
					+ Constants.PLUGIN_NAME + " does not exist!");
			sender.sendMessage("Unable to execute command 'summon'. Plugin "
					+ Constants.PLUGIN_NAME + " does not exist! Report this to the devs!");
			return false;
		}

		// If this is a player and no world was
		World world = Bukkit.getWorld(Constants.DEFAULT_WORLD);

		if (args.length > 5) {
			world = Bukkit.getWorld(args[5]);
		} else {
			if (sender instanceof Player player) {
				world = player.getWorld();
			}
		}

		Location location = null;
		try {
			location = Commands.convertLocation(sender, args[2], args[3], args[4], world);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			sender.sendMessage("Incorrect coordinate formatting!");
			sender.sendMessage(USAGE);
			return false;
		}

		switch (args[1]) {
			case "wisp":
				EntityWisp wisp = EntityWisp.spawn(plugin, (CraftServer) sender.getServer(),
						(CraftWorld) location.getWorld(), location, SpawnReason.COMMAND, location);
				break;
			case "fire_wisp":
				EntityFireWisp fireWisp = EntityFireWisp.spawn(plugin, (CraftServer) sender.getServer(),
						(CraftWorld) location.getWorld(), location, SpawnReason.COMMAND, location);
				break;
			default:
				sender.sendMessage("Mob does not exist!");
				sender.sendMessage(USAGE);
				return false;
		}
		return true;
	}
}
