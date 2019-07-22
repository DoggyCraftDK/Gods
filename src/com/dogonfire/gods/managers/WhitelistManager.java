package com.dogonfire.gods.managers;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.dogonfire.gods.Gods;
import com.dogonfire.gods.config.GodsConfiguration;

public class WhitelistManager
{
	private static WhitelistManager instance;

	public static WhitelistManager get()
	{
		if (instance == null)
			instance = new WhitelistManager();
		return instance;
	}

	private FileConfiguration	whiteList		= null;

	private File				whiteListFile	= null;
	private FileConfiguration	blackList		= null;
	private File				blackListFile	= null;

	private WhitelistManager()
	{
	}

	public float getMinGodPower(String godName)
	{
		int power = this.whiteList.getInt(godName + ".MinPower");

		return power;
	}

	public boolean isBlacklistedGod(String godName)
	{
		String name = this.blackList.getString(godName);

		return name != null;
	}

	public boolean isWhitelistedGod(String godName)
	{
		String name = this.whiteList.getString(godName);

		return name != null;
	}

	public void load()
	{
		if (GodsConfiguration.get().isUseWhitelist())
		{
			if (this.whiteListFile == null)
			{
				this.whiteListFile = new File(Gods.get().getDataFolder(), "whitelist.yml");
			}

			this.whiteList = YamlConfiguration.loadConfiguration(this.whiteListFile);

			Gods.get().log("Loaded " + this.whiteList.getKeys(false).size() + " whitelisted Gods.");

			if (this.whiteList.getKeys(false).size() == 0)
			{
				this.whiteList.set("TheExampleGodName.MinPower", Integer.valueOf(0));

				save();
			}

			GodsConfiguration.get().setUseBlacklist(false);

			Gods.get().log("Using whitelist");
		}

		if (GodsConfiguration.get().isUseBlacklist())
		{
			if (this.blackListFile == null)
			{
				this.blackListFile = new File(Gods.get().getDataFolder(), "blacklist.yml");
			}
			this.blackList = YamlConfiguration.loadConfiguration(this.blackListFile);

			Gods.get().log("Loaded " + this.blackList.getKeys(false).size() + " blacklisted Gods.");
			if (this.blackList.getKeys(false).size() == 0)
			{
				this.blackList.set("TheExampleGodName", "");

				save();
			}
			GodsConfiguration.get().setUseWhitelist(false);

			Gods.get().log("Using blacklist");
		}
	}

	public void save()
	{
		try
		{
			this.whiteList.save(this.whiteListFile);
		}
		catch (Exception ex)
		{
			Gods.get().log("Could not save whitelist to " + this.whiteListFile + ": " + ex.getMessage());
		}
		try
		{
			this.blackList.save(this.blackListFile);
		}
		catch (Exception ex)
		{
			Gods.get().log("Could not save blacklist to " + this.blackListFile + ": " + ex.getMessage());
		}
	}
}