package com.localmood.common.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayUtil {

	public static List<String> toArr(String input) {
		return Arrays.asList(input.split(","));
	}

	public static String[][] to2DArr(String input) {
		return Arrays.stream(input.split("/"))
				.map(pair -> pair.split(","))
				.toArray(String[][]::new);
	}

}
