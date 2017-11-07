package com.Sortex.controller;

public class WatchdogTimer {
	private static boolean watchdogTimer = false;
	private static boolean isWatchdogEnabled = true;
	
	public static void reset() {
		watchdogTimer = true;
	}
	public static void start() {
		watchdogTimer = false;
	}
	
	public static boolean isRested() {
		return watchdogTimer;
	}
	
	public static void enable() {
		System.out.println("Watchdog timer enabled");
		isWatchdogEnabled = true;
	}
	public static void disable() {
		System.out.println("Watchdog timer disabled");
		isWatchdogEnabled = false;
	}
	
	public static boolean isEnabled() {
		return isWatchdogEnabled;
	}
	
}
