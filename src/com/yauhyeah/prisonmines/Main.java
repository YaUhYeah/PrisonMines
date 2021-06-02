package com.yauhyeah.prisonmines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import com.yauhyeah.prisonmines.mines.cmd.MineCommand;
import com.yauhyeah.prisonmines.mines.listeners.MinesListener;
import com.yauhyeah.prisonmines.mines.obj.Mine;
import com.yauhyeah.prisonmines.mines.obj.MineBlock;
import com.yauhyeah.prisonmines.mines.obj.MineComposition;
import com.yauhyeah.prisonmines.mines.obj.MineRegion;

import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;

public class Main extends PluginBase {
	private Config minesCfg;
	private File minesFile;
	public List<Mine> getMines;
	private static Main main;

	PlaceholderAPI api = PlaceholderAPI.getInstance();

	@Override
	public void onEnable() {
		registerFiles();
		getServer().getCommandMap().register("mines", new MineCommand());
		getServer().getPluginManager().registerEvents(new MinesListener(), this);
		getMines = new ArrayList<>();
		reloadAllMinesFromConfig();
	}

	@Override
	public void onLoad() {
		main = this;
	}

	public void registerFiles() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		saveResource("mines.yml");
		minesFile = new File(getDataFolder(), "mines.yml");
		if (!minesFile.exists()) {
			try {
				minesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		minesCfg = new Config(minesFile);
	}

	public void reloadAllMinesFromConfig() {
		getMines.clear();
		for (String key : minesCfg.getSection("Mines").getKeys(false)) {
			Location tpLoc = new Location(minesCfg.getDouble("Mines." + key + ".tpLocX"),
					(minesCfg.getDouble("Mines." + key + ".tpLocY")), (minesCfg.getDouble("Mines." + key + ".tpLocZ")),
					minesCfg.getDouble("Mines." + key + ".tpYaw"), minesCfg.getDouble("Mines." + key + ".tpPitch"),
					Main.getMain().getServer().getLevelByName(minesCfg.getString("Mines." + key + ".tpLocLevelName")));
			String name = minesCfg.getString("Mines." + key + ".name");
			Location minLoc = new Location(minesCfg.getDouble("Mines." + key + ".minX"),
					minesCfg.getDouble("Mines." + key + ".minY"), minesCfg.getDouble("Mines." + key + ".minZ"),
					Main.getMain().getServer().getLevelByName(minesCfg.getString("Mines." + key + ".tpLocLevelName")));
			Location maxLoc = new Location(minesCfg.getDouble("Mines." + key + ".maxX"),
					minesCfg.getDouble("Mines." + key + ".maxY"), minesCfg.getDouble("Mines." + key + ".maxZ"),
					Main.getMain().getServer().getLevelByName(minesCfg.getString("Mines." + key + ".tpLocLevelName")));
			List<MineBlock> mbs = new ArrayList<>();
			for (String comp : minesCfg.getSection("Mines." + key + ".composition").getKeys(false)) {
				MineBlock mb = new MineBlock(Block.get(Integer.parseInt(comp)),
						minesCfg.getInt("Mines." + key + ".composition." + comp));
				mbs.add(mb);
			}
			MineRegion mr = new MineRegion(maxLoc, minLoc,
					Main.getMain().getServer().getLevelByName(minesCfg.getString("Mines." + key + ".tpLocLevelName")));
			MineComposition cmp = new MineComposition(mbs);
			Mine mine = new Mine(mr, name, cmp, tpLoc);
			getMines.add(mine);
		}
	}

	public void startMineResetTask() {
		if (!getMines.isEmpty()) {
			for (Mine m : getMines) {
				new NukkitRunnable() {
					int i = 120;

					@Override
					public void run() {
						if (i <= 0) {
							m.resetMine();
							i = 120;
						}
						api.builder(m.getMineName() + "_resetMineDelay", Integer.class).loader(entry -> i).build();
						i--;
					}
				}.runTaskTimer(this, 0, 20);
			}
		}
	}

	public Config getMinesCfg() {
		return minesCfg;
	}

	public File getMinesFile() {
		return minesFile;
	}

	public static Main getMain() {
		return main;
	}

	public List<Mine> getMinesList() {
		return getMines;
	}

	public void setGetMines(List<Mine> getMines) {
		this.getMines = getMines;
	}
}
