package com.yauhyeah.prisonmines.utils;

import com.google.common.base.Strings;

import cn.nukkit.utils.TextFormat;

public class StringUtils {

	public static String translateColors(String message) {
		return TextFormat.colorize(message);
	}

	public static boolean stringContainsItemFromList(String inputStr, String[] items) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].contains(inputStr)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmptyStringArray(String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null)
				return false;
		}
		return true;
	}

	public static String secondsToString(int pTime) {
		int mins = pTime / 60;
		return String.format("%02d:%02d:%02d", pTime / 3600, mins % 60, pTime % 60);
	}

	public static String getPrefix() {
		return translateColors("&8[&bParadox&dPE&8] &r&7");
	}


	public static boolean isAlphaNumeric(String s) {
		return (s != null && s.matches("^[a-zA-Z0-9]*$"));
	}

	public static String getProgressBar(int current, int max, int totalBars, char symbol, TextFormat completedColor,
			TextFormat notCompletedColor) {
		float percent = (float) current / max;
		int progressBars = (int) (totalBars * percent);

		return Strings.repeat("" + completedColor + symbol, progressBars)
				+ Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
	}

	public static String stripNonDigits(final CharSequence input /* inspired by seh's comment */) {
		final StringBuilder sb = new StringBuilder(input.length() /* also inspired by seh's comment */);
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if (c > 47 && c < 58) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
