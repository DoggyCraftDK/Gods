package com.dogonfire.gods.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RitualManager
{
	private static RitualManager instance;

	public static RitualManager instance()
	{
		if (instance == null)
			instance = new RitualManager();
		return instance;
	}

	private RitualManager()
	{
	}

	public boolean handleAltarPray(Location altarLocation, Player player, String godName)
	{
		return false;
	}	
}