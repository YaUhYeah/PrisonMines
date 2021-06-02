package com.yauhyeah.prisonmines.mines;

import com.yauhyeah.prisonmines.Main;
import com.yauhyeah.prisonmines.mines.obj.Mine;
import com.yauhyeah.prisonmines.mines.obj.MineBlock;
import com.yauhyeah.prisonmines.utils.StringUtils;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;

public class FormStorage {

	public static FormWindowSimple MinerMenu() {
		FormWindowSimple fs = new FormWindowSimple(StringUtils.translateColors("&b&lParadox &d&lPrisons"),
				StringUtils.translateColors("&b&l&nChoose a Mine to teleport to!"));
		for (Mine mine : Main.getMain().getMines) {
			if (!mine.getMineName().equalsIgnoreCase("pvp")) {
				fs.addButton(new ElementButton(StringUtils.translateColors("&a") + mine.getMineName()));
			}
		}
		return fs;
	}

	public static FormWindowSimple MineEditMenu() {
		FormWindowSimple fs = new FormWindowSimple(StringUtils.translateColors("&b&lMine Management"),
				StringUtils.translateColors("&b&l&nChoose a Mine edit!"));
		for (Mine mine : Main.getMain().getMines) {
			fs.addButton(new ElementButton(StringUtils.translateColors("&b") + mine.getMineName()));
		}
		return fs;
	}

	public static FormWindowSimple deleteConfirm(Mine mine) {
		FormWindowSimple fs = new FormWindowSimple(StringUtils.translateColors("&b&lConfirm mine deletion"),
				StringUtils.translateColors("&c&lAre you sure you want to delete the mine: " + mine.getMineName()));
		fs.addButton(new ElementButton(StringUtils.translateColors("&cNO!")));
		fs.addButton(new ElementButton(StringUtils.translateColors("&cYes!")));
		return fs;
	}

	public static FormWindowSimple MineEditMainMenu(Mine mine) {
		FormWindowSimple fs = new FormWindowSimple(
				StringUtils.translateColors("&9&lMine " + mine.getMineName() + " menu"),
				StringUtils.translateColors("&b&l&nChoose an option."));
		fs.addButton(new ElementButton(StringUtils.translateColors("&bReset Mine")));
		fs.addButton(new ElementButton(StringUtils.translateColors("&bSet teleport location")));
		fs.addButton(new ElementButton(StringUtils.translateColors("&bEdit composition")));
		fs.addButton(new ElementButton(StringUtils.translateColors("&bDelete this Mine")));
		return fs;
	}

	public static FormWindowCustom mineCompositionEditor(Mine mine) {
		FormWindowCustom fc = new FormWindowCustom(
				StringUtils.translateColors("&9&lMine " + mine.getMineName() + " composition"));
		fc.addElement(new ElementLabel(StringUtils.translateColors("&bMineName: " + mine.getMineName())));// 0
		fc.addElement(new ElementLabel(StringUtils.translateColors("&bFormat &dblockID:chance")));// 1
		int i = 1;
		for (MineBlock mb : mine.getMineComposition().getMb()) {
			fc.addElement(new ElementInput(StringUtils.translateColors("&bBlock #" + i), "blockID:chance",
					mb.getBlock().getId() + ":" + mb.getChance()));// 3
			i++;
		}
		fc.addElement(new ElementLabel(StringUtils.translateColors("&bAdd More Blocks to mine?")));// 4
		fc.addElement(
				new ElementInput(StringUtils.translateColors("&bPut 1 to add a block."), "put the number 1.", 0 + ""));// 5
		fc.addElement(new ElementToggle(StringUtils.translateColors("&bSave changes?")));// 6
		return fc;
	}

}
