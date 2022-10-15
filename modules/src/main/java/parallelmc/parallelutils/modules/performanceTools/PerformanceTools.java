package parallelmc.parallelutils.modules.performanceTools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import parallelmc.parallelutils.Constants;
import parallelmc.parallelutils.ParallelClassLoader;
import parallelmc.parallelutils.ParallelModule;
import parallelmc.parallelutils.ParallelUtils;
import parallelmc.parallelutils.modules.performanceTools.commands.FindLoadedChunksCommand;

import java.net.URLClassLoader;
import java.util.List;
import java.util.logging.Level;

/**
 * This module adds tools to help diagnose performance issues
 */
public class PerformanceTools extends ParallelModule {

	private BukkitTask loaderDetector;

	public PerformanceTools(ParallelClassLoader classLoader, List<String> dependents) {
		super(classLoader, dependents);
	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable() {
		PluginManager manager = Bukkit.getPluginManager();
		Plugin plugin = manager.getPlugin(Constants.PLUGIN_NAME);
		int CLDhours = 1;

		if (plugin == null) {
			ParallelUtils.log(Level.SEVERE, "Unable to enable PerformanceTools. Plugin " + Constants.PLUGIN_NAME
					+ " does not exist!");
			return;
		}

		ParallelUtils puPlugin = (ParallelUtils) plugin;

		if (!puPlugin.registerModule(this)) {
			ParallelUtils.log(Level.SEVERE, "Unable to register module PerformanceTools! " +
					"Module may already be registered. Quitting...");
			return;
		}

		puPlugin.addCommand("loadedChunks", new FindLoadedChunksCommand());

		//start searching for chunk loaders every [time]
		//loaderDetector = new LoaderDetectorTask().runTaskTimer(plugin,0,20*60*60*CLDhours);
	}

	@Override
	public void onDisable() {
		//loaderDetector.cancel();
	}

	@Override
	public void onUnload() {

	}

	@Override
	public @NotNull String getName() {
		return "PerformanceTools";
	}
}