package com.dogonfire.gods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HolyLawManager
{
	private Gods				plugin;
	private FileConfiguration	holyLawsConfig		= null;
	private File				holyLawsConfigFile	= null;
	private Random				random					= new Random();

	HolyLawManager(Gods plugin)
	{
		this.plugin = plugin;
	}

	public void load()
	{
		if (this.holyLawsConfigFile == null)
		{
			this.holyLawsConfigFile = new File(this.plugin.getDataFolder(), "holylaws.yml");
		}
		this.holyLawsConfig = YamlConfiguration.loadConfiguration(this.holyLawsConfigFile);

		this.plugin.log("Loaded " + this.holyLawsConfig.getKeys(false).size() + " holy laws.");
	}

	public void save()
	{
		if ((this.holyLawsConfig == null) || (this.holyLawsConfigFile == null))
		{
			return;
		}
		try
		{
			this.holyLawsConfig.save(this.holyLawsConfigFile);
		}
		catch (Exception ex)
		{
			this.plugin.log("Could not save config to " + this.holyLawsConfigFile.getName() + ": " + ex.getMessage());
		}
	}
	
	public String getPendingLawQuestion(String godName)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}

	public List<String> getAllLawsForGod(String godName)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}

	public void SetHolyMob(String godName, EntityType mobType)
	{
		this.holyLawsConfig.set(godName + ".Holy.MobKill." + mobType + ".StartTime", 0);		
		this.holyLawsConfig.set(godName + ".Holy.MobKill." + mobType + ".EndTime", 24000);		
	}

	public boolean IsHolyMobKill(String godName, EntityType mobType, int time)
	{
		if(this.holyLawsConfig.getString(godName + ".Holy.MobKill." + mobType) != null)		
		{
			return false;			
		}
		
		int startTime = this.holyLawsConfig.getInt(godName + ".Holy.MobKill." + mobType + ".StartTime");		
		int endTime = this.holyLawsConfig.getInt(godName + ".Holy.MobKill." + mobType + ".EndTime");		
	
		if(startTime == 0 && endTime == 0)
		{						
			return true;
		}
		
		String biomeType = this.holyLawsConfig.getInt(godName + ".Holy.MobKill." + mobType + ".Biome");		

		if(biomeType == null)
		{						
			return true;
		}

		return time > startTime && time < endTime; 		
	}

	public boolean IsUnholyMobKill(String godName, EntityType mobType)
	{
		return this.holyLawsConfig.getString(godName + ".Unholy.MobKill." + mobType.toString()) != null;		
	}

	public void SetHolyPlayerKill(String godName)
	{
		this.holyLawsConfig.set(godName + ".Holy.PlayerKill.StartTime", 0);		
		this.holyLawsConfig.set(godName + ".Holy.PlayerKill.EndTime", 24000);		
		this.holyLawsConfig.set(godName + ".Holy.PlayerKill.Biome", Biome.BIRCH_FOREST);		
	}

	public boolean IsHolyPlayerKill(String godName, UUID playerId)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}

	public boolean IsUnholyPlayerKill(String godName, UUID playerId)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}

	public boolean IsHolyFood(String godName, EntityType mobType)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}

	public boolean IsUnholyFood(String godName, EntityType mobType)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}
	
	public boolean IsHolyBiome(String godName, EntityType mobType, long time)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}

	public boolean IsUnholyBiome(String godName, EntityType mobType, long time)
	{
		return this.holyLawsConfig.getString(godName + ".Question");		
	}
	
	void generateHolyLawQuestion(String godName)
	{		
		// Alternate between holy and unholy question to ensure that they are equal numbers
		
	}

	void acceptPendingLawQuestion(String godName)
	{
		
	}

	void rejectPendingLawQuestion(String godName)
	{
		
	}
}
