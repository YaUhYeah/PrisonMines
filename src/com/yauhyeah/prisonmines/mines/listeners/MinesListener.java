package com.yauhyeah.prisonmines.mines.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yauhyeah.prisonmines.Main;
import com.yauhyeah.prisonmines.mines.FormStorage;
import com.yauhyeah.prisonmines.mines.cmd.MineCommand;
import com.yauhyeah.prisonmines.mines.obj.Mine;
import com.yauhyeah.prisonmines.mines.obj.MineBlock;
import com.yauhyeah.prisonmines.mines.obj.MineComposition;
import com.yauhyeah.prisonmines.mines.obj.MineRegion;
import com.yauhyeah.prisonmines.utils.ItemStorage;
import com.yauhyeah.prisonmines.utils.StringUtils;
import com.yauhyeah.ranks.RankUtils;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

public class MinesListener implements Listener {
	public static Config mines = MineCommand.mines;
	public static File minesFile = MineCommand.minesFile;
	List<Location> locsForMine = new ArrayList<>();
	HashMap<Player, Mine> mineToDelete = new HashMap<>();

	@EventHandler
	public void onResponseCustom(PlayerFormRespondedEvent e) {
		Player p = e.getPlayer();
		if (e.getWindow() instanceof FormWindowCustom) {
			FormWindowCustom gui = (FormWindowCustom) e.getWindow();
			for (Mine mine : Main.getMain().getMines) {
				if (gui.getTitle()
						.equals(StringUtils.translateColors("&9&lMine " + mine.getMineName() + " composition"))) {
					if (e.getResponse() != null) {
						int compositionSize = mine.getMineComposition().getMb().size();
						int beginningInput = 1;
						int afterComposition = beginningInput + compositionSize + 3;
						boolean confirmation = gui.getResponse().getToggleResponse(afterComposition++);
						int numberOfBlocksToAdd = Integer
								.parseInt(gui.getResponse().getInputResponse(beginningInput + compositionSize + 2));
						if (confirmation) {
							ConfigSection sec = mines.getSection("Mines." + mine.getMineName());
							sec.remove("composition");
							mines.set("Mines." + mine.getMineName(), sec);
							mines.save(minesFile);
							int total = beginningInput + 1;
							for (int i = 0; i < compositionSize; i++) {
								String resp = gui.getResponse().getInputResponse(total);
								if (resp.contains(":")) {
									String array[] = resp.split(":", 2);
									mines.set("Mines." + mine.getMineName() + ".composition."
											+ Integer.parseInt(array[0]), Integer.parseInt(array[1]));
									mines.save(minesFile);
								}
								total++;
							}
							for (int i = 0; i < numberOfBlocksToAdd; i++) {
								mines.set("Mines." + mine.getMineName() + ".composition." + 0, 100);
								mines.save(minesFile);
							}
							p.sendMessage(StringUtils.getPrefix() + "Updated mine composition.");
							Main.getMain().reloadAllMinesFromConfig();
						}
					}
				}
			}
		}

	}

	@EventHandler
	public void onResponse(PlayerFormRespondedEvent e) {
		Player p = e.getPlayer();
		if (e.getWindow() != null) {
			if (e.getWindow() instanceof FormWindowSimple) {
				FormWindowSimple gui = (FormWindowSimple) e.getWindow();
				if (gui != null) {
					if (gui.getResponse().getClickedButton().getText() != null) {
						String responseName = gui.getResponse().getClickedButton().getText();
						if (gui.getTitle().equals(StringUtils.translateColors("&b&lParadox &d&lPrisons"))) {
							if (responseName != null) {
								for (Mine m : Main.getMain().getMines) {
									if (responseName.equals(StringUtils.translateColors("&a" + m.getMineName()))) {
										if (RankUtils.getRankByName(m.getMineName()) != null) {
											if (RankUtils.getRankByName(m.getMineName()).getOrder() <= RankUtils
													.getRankByPlayer(p).getOrder()) {
												p.teleport(m.getTpLocation());
												p.sendActionBar(StringUtils.translateColors(
														"&bTeleported to mine " + m.getMineName() + "."));
												return;
											} else {
												p.sendMessage(
														StringUtils.getPrefix() + "No perms for this mine! /rankup!");
												return;
											}
										} else {
											p.sendMessage(StringUtils.translateColors(
													"&cERROR! Rank does not exist for mine: " + m.getMineName()));
											return;
										}
									}
								}
							}
						} else if (gui.getTitle().equals(StringUtils.translateColors("&b&lMine Management"))) {
							if (responseName != null) {
								for (Mine m : Main.getMain().getMines) {
									if (responseName.equals(StringUtils.translateColors("&b" + m.getMineName()))) {
										p.showFormWindow(FormStorage.MineEditMainMenu(m));
										return;
									}
								}
							}
						} else if (gui.getTitle().equals(StringUtils.translateColors("&b&lConfirm mine deletion"))) {
							if (responseName != null) {
								if (responseName.equals(StringUtils.translateColors("&cYes!"))) {
									ConfigSection sec = mines.getSection("SignShops");
									sec.remove("Mines." + mineToDelete.get(p).getMineName());
									mines.set("Mines." + mineToDelete.get(p).getMineName(), sec);
									mines.save(minesFile);
									for (Mine mine : Main.getMain().getMines) {
										if (mine.getMineName().equals(mineToDelete.get(p).getMineName())) {
											Main.getMain().getMines.remove(mine);
										}
									}
									p.sendMessage(StringUtils.getPrefix() + "You have deleted a mine!");
								}
							}
							Main.getMain().reloadAllMinesFromConfig();
							mineToDelete.remove(p);
						}
						for (Mine m : Main.getMain().getMines) {
							if (gui.getTitle()
									.equals(StringUtils.translateColors("&9&lMine " + m.getMineName() + " menu"))) {
								if (responseName != null) {
									if (responseName.equals(StringUtils.translateColors("&bReset Mine"))) {
										m.resetMine();
										p.sendMessage(StringUtils.getPrefix() + "Reset Mine " + m.getMineName());
										return;
									} else if (responseName
											.equals(StringUtils.translateColors("&bSet teleport location"))) {
										mines.set("Mines." + m.getMineName() + ".tpLocX", p.getLocation().getX());
										mines.set("Mines." + m.getMineName() + ".tpLocY", p.getLocation().getY());
										mines.set("Mines." + m.getMineName() + ".tpLocZ", p.getLocation().getZ());
										mines.set("Mines." + m.getMineName() + ".tpYaw", p.getLocation().getYaw());
										mines.set("Mines." + m.getMineName() + ".tpPitch", p.getLocation().getPitch());
										mines.set("Mines." + m.getMineName() + ".tpLocLevelName",
												p.getLevel().getName());
										p.sendMessage(StringUtils.getPrefix() + "Set new tp location for Mine: "
												+ m.getMineName());
										mines.save(minesFile);
										Main.getMain().reloadAllMinesFromConfig();
										return;
									} else if (responseName.equals(StringUtils.translateColors("&bEdit composition"))) {
										p.showFormWindow(FormStorage.mineCompositionEditor(m));
										return;
									} else if (responseName.equals(StringUtils.translateColors("&bDelete this Mine"))) {
										p.showFormWindow(FormStorage.deleteConfirm(m));
										mineToDelete.put(p, m);
										return;
									}
								}
							}
						}
					}
				}
			}

		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (MineCommand.playersInSetupModeMine.containsKey(p)) {
			if (p.getInventory().getItemInHand().getCustomName().equals(ItemStorage.mineSetupWand().getCustomName())) {
				e.setCancelled();
				if (locsForMine.size() > 1) {
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".name",
							MineCommand.playersInSetupModeMine.get(p));
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".minX",
							locsForMine.get(0).getX());
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".minY",
							locsForMine.get(0).getY());
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".minZ",
							locsForMine.get(0).getZ());
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".maxX",
							locsForMine.get(1).getX());
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".maxY",
							locsForMine.get(1).getY());
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".maxZ",
							locsForMine.get(1).getZ());
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".composition.1", 100);/////
					mines.set("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".world",
							locsForMine.get(0).getLevel().getName());
					mines.save(minesFile);
					p.sendMessage(StringUtils.getPrefix()
							+ ("Mine: " + MineCommand.playersInSetupModeMine.get(p) + " Created!"));
					Location tpLoc = new Location(
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocX"),
							(mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocY")),
							(mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocZ")),
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpYaw"),
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpPitch"),
							Main.getMain().getServer().getLevelByName(mines.getString(
									"Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocLevelName")));
					String name = mines.getString("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".name");
					Location minLoc = new Location(
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".minX"),
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".minY"),
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".minZ"),
							Main.getMain().getServer().getLevelByName(mines.getString(
									"Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocLevelName")));
					Location maxLoc = new Location(
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".maxX"),
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".maxY"),
							mines.getDouble("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".maxZ"),
							Main.getMain().getServer().getLevelByName(mines.getString(
									"Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocLevelName")));
					List<MineBlock> mbs = new ArrayList<>();
					for (String comp : mines
							.getSection("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".composition")
							.getKeys(false)) {
						MineBlock mb = new MineBlock(Block.get(Integer.parseInt(comp)), mines
								.getInt("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".composition." + comp));
						mbs.add(mb);
					}
					MineRegion mr = new MineRegion(maxLoc, minLoc, Main.getMain().getServer().getLevelByName(
							mines.getString("Mines." + MineCommand.playersInSetupModeMine.get(p) + ".tpLocLevelName")));
					MineComposition cmp = new MineComposition(mbs);
					Main.getMain().getMinesList().add(new Mine(mr, name, cmp, tpLoc));
					MineCommand.playersInSetupModeMine.remove(p);
					locsForMine.clear();
					return;
				} else {
					locsForMine.add(e.getBlock().getLocation());
					p.sendActionBar(StringUtils.translateColors("&bAdded location to mine region."));
				}
			}
		}
	}

}
