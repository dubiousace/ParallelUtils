package parallelmc.parallelutils.modules.custommobs.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import parallelmc.parallelutils.Constants;
import parallelmc.parallelutils.Parallelutils;
import parallelmc.parallelutils.commands.Commands;
import parallelmc.parallelutils.commands.ParallelCommand;
import parallelmc.parallelutils.commands.permissions.ParallelOrPermission;
import parallelmc.parallelutils.commands.permissions.ParallelPermission;
import parallelmc.parallelutils.modules.custommobs.registry.SpawnerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A command to delete a spawner at a given location
 * Usage: /pu deletespawner <uuid>
 *        /pu deletespawner <x> <y> <z>
 */
public class ParallelDeleteSpawnerCommand extends ParallelCommand {

	private final String USAGE = "Usage: /pu deletespawner <uuid> \n /pu deletespawner <x> <y> <z>";

	public ParallelDeleteSpawnerCommand() {
		// Requires either the parallelutils.spawn permission, the parallelutils.spawn.spawners permission
		// or the parallelutils.spawn.spawners.delete permission
		super("deletespawner", new ParallelOrPermission(new ParallelPermission[]
				{new ParallelPermission("parallelutils.spawn"), new ParallelPermission("parallelutils.spawn.spawners"),
						new ParallelPermission("parallelutils.spawn.spawners.delete")}));
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args) {

		if (args.length == 2) {
			// UUID
			// Simply lookup the spawner in the registry by uuid and delete if it exists.
			Location loc = SpawnerRegistry.getInstance().getSpawnerLoc(args[1]);
			if (loc != null && deleteSpawnerComplete(loc)) {
				sender.sendMessage("Spawner " + args[1] + " deleted");
				return true;
			} else {
				sender.sendMessage("Unable to delete spawner " + args[1] + " \nPlease check UUID and try again");
				return false;
			}
		} else if (args.length == 4 || args.length == 5) {
			// Location
			// Check if a spawner exists at a given location and delete it if it exists
			String world = Constants.DEFAULT_WORLD;

			// If a player ran this, use the player's world as the default world
			if (sender instanceof Player player) {
				world = player.getWorld().getName();
			}

			Location location = null;

			if (args.length == 5) {
				world = args[4];
			}

			try {
				location = Commands.convertLocation(sender, args[1], args[2], args[3], Bukkit.getWorld(world));
				Parallelutils.log(Level.INFO, location.toString());
			} catch (NumberFormatException e) {
				sender.sendMessage("Invalid position.\n");
				sender.sendMessage(USAGE);
				return false;
			}

			if (deleteSpawnerComplete(location)) {
				sender.sendMessage("Spawner " + location.toString() + " deleted");
				return true;
			} else {
				sender.sendMessage("Unable to delete spawner " + location.toString() + "\nPlease check location and try again");
				return false;
			}
		} else {
			// Invalid
			sender.sendMessage(USAGE);
		}
		return false;
	}

	@Override
	public List<String> getTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
		ArrayList<String> list = new ArrayList<>();

		if (args.length == 2) {
			list.add("uuid");
			if (sender instanceof Player player) {

				Block targetedBlock = player.getTargetBlock(5);

				if (targetedBlock != null && targetedBlock.isSolid()) {
					// Autofill targeted coords
					list.add(String.format("%d", targetedBlock.getX()));
					list.add(String.format("%d %d", targetedBlock.getX(), targetedBlock.getY()));
					list.add(String.format("%d %d %d", targetedBlock.getX(), targetedBlock.getY(), targetedBlock.getZ()));
					list.add(String.format("%d %d %d world", targetedBlock.getX(), targetedBlock.getY(), targetedBlock.getZ()));
				} else {
					Location location = player.getLocation();
					list.add(String.format("%d", location.getBlockX()));
					list.add(String.format("%d %d", location.getBlockX(), location.getBlockY()));
					list.add(String.format("%d %d %d", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
					list.add(String.format("%d %d %d world", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
				}
			}
		} else if (args.length == 3) {
			if (sender instanceof Player player) {
				Block targetedBlock = player.getTargetBlock(5);

				if (targetedBlock != null && targetedBlock.isSolid()) {
					// Autofill targeted coords
					list.add(String.format("%d", targetedBlock.getY()));
					list.add(String.format("%d %d", targetedBlock.getY(), targetedBlock.getZ()));
					list.add(String.format("%d %d, world", targetedBlock.getY(), targetedBlock.getZ()));
				} else {
					Location location = player.getLocation();
					list.add(String.format("%d", location.getBlockY()));
					list.add(String.format("%d %d", location.getBlockY(), location.getBlockZ()));
					list.add(String.format("%d %d world", location.getBlockY(), location.getBlockZ()));
				}
			}
		} else if (args.length == 4) {
			if (sender instanceof Player player) {
				Block targetedBlock = player.getTargetBlock(5);

				if (targetedBlock != null && targetedBlock.isSolid()) {
					// Autofill targeted coords
					list.add(String.format("%d", targetedBlock.getZ()));
					list.add(String.format("%d world", targetedBlock.getZ()));
				} else {
					Location location = player.getLocation();
					list.add(String.format("%d", location.getBlockZ()));
					list.add(String.format("%d world", location.getBlockZ()));
				}
			}
		} else if (args.length == 5) {
			if (sender instanceof Player) {
				list.add("world");
			}
		}

		return list;
	}

	/**
	 * Deletes the spawner at a location as well as corresponding trackers and tasks.
	 * @param loc The location of the spawner to delete
	 * @return Returns true if the spawner was deleted, false otherwise
	 */
	private boolean deleteSpawnerComplete(Location loc) {
		if (SpawnerRegistry.getInstance().deleteSpawner(loc)) {
			SpawnerRegistry.getInstance().removeMobCount(loc);

			Bukkit.getScheduler().cancelTask(SpawnerRegistry.getInstance().getSpawnTaskID(loc));
			SpawnerRegistry.getInstance().removeSpawnTaskID(loc);

			if (SpawnerRegistry.getInstance().getLeashTaskID(loc) != null) {
				Bukkit.getScheduler().cancelTask(SpawnerRegistry.getInstance().getLeashTaskID(loc));
				SpawnerRegistry.getInstance().removeLeashTaskID(loc);
				SpawnerRegistry.getInstance().removeSpawnerLeash(loc);
			}

			return true;
		}

		return false;
	}
}
