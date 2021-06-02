package com.yauhyeah.prisonmines.utils;

import java.util.ArrayList;
import java.util.List;

import com.yauhyeah.prisonmines.Main;
import com.yauhyeah.prisonmines.mines.obj.Mine;

import cn.nukkit.level.Location;
import cn.nukkit.level.Position;

public class MineUtils {
	public static Mine mineByLoc(Location loc) {
		for (Mine m : Main.getMain().getMines) {
			if (m.getRegion().isInRegion(loc)) {
				return m;
			}
		}
		return null;
	}

	public static boolean isLocInMine(Location loc) {
		for (Mine m : Main.getMain().getMines) {
			if (m.getRegion().isInRegion(loc)) {
				return true;
			}
		}
		return false;
	}

	public static Mine getMineByName(String name) throws NullPointerException {
		for (Mine m : Main.getMain().getMines) {
			if (name.equals(m.getMineName())) {
				return m;
			}
		}
		return null;
	}

	public static List<Position> getCorners(Location loc) {
		List<Position> locs = new ArrayList<>();
		if (mineByLoc(loc) != null) {
			Mine m = mineByLoc(loc);
			Location locMax = new Location(m.getRegion().getLocMax().getX(), loc.getY(),
					m.getRegion().getLocMax().getZ());
			Location locMin = new Location(m.getRegion().getLocMin().getX(), loc.getY(),
					m.getRegion().getLocMin().getZ());
			Position cornerOne = locMax.setComponents(locMax.getX(), loc.getY(), locMax.getZ());
			Position cornerTwo = locMin.setComponents(locMin.getX(), loc.getY(), locMin.getZ());
			locs.add(cornerOne);
			locs.add(cornerTwo);
		}
		return locs;
	}

}
