package com.yauhyeah.prisonmines.mines;

import java.util.ArrayList;
import java.util.List;

import com.yauhyeah.prisonmines.mines.obj.LuckyReward;

public class LuckyRewardStorage {

	public static LuckyReward prizeOne = new LuckyReward(30, "100 Orbs", "orbs give {name} 100");
	public static LuckyReward prizeTwo = new LuckyReward(10, "1x Basic Key", "crate give {name} Basic 1");
	public static LuckyReward prizeThree = new LuckyReward(5, "Orbs Pouch Tier I", "orbs givepouch {name} 1 1");
	public static LuckyReward prizeFour = new LuckyReward(35, "50 Orbs", "orbs give {name} 50");
	public static LuckyReward prizeFive = new LuckyReward(35, "$500", "givemoney {name} 500");
	public static LuckyReward prizeSix = new LuckyReward(35, "1x Small bombs", "bomb give {name} 1 small");
	public static LuckyReward prizeSeven = new LuckyReward(3, "Orbs Pouch Tier II", "orbs givepouch {name} 1 2");
	public static List<LuckyReward> rews() {
		List<LuckyReward> rews = new ArrayList<LuckyReward>();
		rews.add(prizeOne);
		rews.add(prizeTwo);
		rews.add(prizeThree);
		rews.add(prizeFour);
		rews.add(prizeFive);
		rews.add(prizeSix);
		rews.add(prizeSeven);
		return rews;
	}

}
