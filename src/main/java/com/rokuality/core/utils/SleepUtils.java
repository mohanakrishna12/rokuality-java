package com.rokuality.core.utils;

public class SleepUtils {

	public static void sleep(Integer sleepInMS) {
		try {
			Thread.sleep(sleepInMS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
