package com.dogonfire.gods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;

import com.dogonfire.gods.GodManager.GodType;

public class SacrificeManager
{
	private Gods plugin;
	private Random random = new Random();
	private FileConfiguration	sacrificesConfig		= null;
	private File				sacrificesConfigFile	= null;
	private HashMap<Integer, String> droppedItems = new HashMap();

	SacrificeManager(Gods p)
	{
		this.plugin = p;
	}
	
	public void load()
	{
		if (this.sacrificesConfigFile == null)
		{
			this.sacrificesConfigFile = new File(this.plugin.getDataFolder(), "sacrifices.yml");
		}
		this.sacrificesConfig = YamlConfiguration.loadConfiguration(this.sacrificesConfigFile);

		this.plugin.log("Loaded " + this.sacrificesConfig.getKeys(false).size() + " sacrifies.");
	}

	private Material getSacrificeNeedForGod(String godName)
	{
		Random materialRandom = new Random(plugin.getGodManager().getSeedForGod(godName));
		List<Integer> materials = new ArrayList();

		for (int n = 0; n < 5; n++)
		{
			materials.add(materialRandom.nextInt(24));
		}

		int typeIndex = 0;
		Material type = Material.AIR;

		do
		{
			typeIndex = ((Integer) materials.get(this.random.nextInt(materials.size()))).intValue();

			switch (typeIndex)
			{
				case 0:
					type = Material.RED_ROSE;
					break;
				case 1:
					type = Material.LEAVES;
					break;
				case 2:
					type = plugin.getGodManager().getNotEatFoodTypeForGod(godName);
					break;
				case 3:
					type = Material.RABBIT_HIDE;
					break;
				case 4:
					type = Material.RABBIT_FOOT;
					break;
				case 5:
					type = Material.CACTUS;
					break;
				case 6:
					type = Material.BREAD;
					break;
				case 7:
					type = Material.CARROT_ITEM;
					break;
				case 8:
					type = Material.IRON_PICKAXE;
					break;
				case 9:
					type = Material.IRON_INGOT;
					break;
				case 10:
					type = Material.GOLD_INGOT;
					break;
				case 11:
					type = Material.APPLE;
					break;
				case 12:
					type = Material.BOOK;
					break;
				case 13:
					type = Material.CAKE;
					break;
				case 14:
					type = Material.MELON;
					break;
				case 15:
					type = Material.COOKIE;
					break;
				case 16:
					type = Material.PUMPKIN;
					break;
				case 17:
					type = Material.SUGAR_CANE;
					break;
				case 18:
					type = Material.EGG;
					break;
				case 19:
					type = Material.WHEAT;
					break;
				case 20:
					type = Material.SPIDER_EYE;
					break;
				case 21:
					type = Material.POTATO_ITEM;
					break;
				case 22:
					type = Material.BONE;
					break;
				case 23:
					type = Material.FEATHER;
			}
		}
		while (type == getEatFoodTypeForGod(godName) || type == Material.AIR);

		return type;
	}
	
	public Material getSacrificeItemTypeForGod(String godName)
	{
		String itemName = "";
		Integer value = Integer.valueOf(0);
		String sacrificeItemName = null;

		ConfigurationSection configSection = this.godsConfig.getConfigurationSection(godName + ".SacrificeValues");
		if ((configSection == null) || (configSection.getKeys(false).size() == 0))
		{
			return null;
		}
		for (int i = 0; i < configSection.getKeys(false).size(); i++)
		{
			itemName = (String) configSection.getKeys(false).toArray()[this.random.nextInt(configSection.getKeys(false).size())];

			value = Integer.valueOf(this.godsConfig.getInt(godName + ".SacrificeValues." + itemName));
			if (value.intValue() > 10)
			{
				sacrificeItemName = itemName;
			}
		}
		if (sacrificeItemName != null)
		{
			return Material.getMaterial(sacrificeItemName);
		}
		return null;
	}
	
	public void handleSacrifice(String godName, Player believer, Material type)
	{
		if (believer == null)
		{
			return;
		}

		if (!this.plugin.isEnabledInWorld(believer.getWorld()))
		{
			return;
		}

		if (godName == null)
		{
			return;
		}

		int godPower = (int) this.plugin.getGodManager().getGodPower(godName);

		this.plugin.log(believer.getDisplayName() + " sacrificed " + type.name() + " to " + godName);

		Material eatFoodType = getEatFoodTypeForGod(godName);

		if (type == eatFoodType)
		{
			addMoodForGod(godName, getAngryModifierForGod(godName));
			cursePlayer(godName, believer.getUniqueId(), getGodPower(godName));

			try
			{
				this.plugin.getLanguageManager().setType(this.plugin.getLanguageManager().getItemTypeName(eatFoodType));
			}
			catch (Exception ex)
			{
				this.plugin.logDebug(ex.getStackTrace().toString());
			}

			this.plugin.getLanguageManager().setPlayerName(believer.getDisplayName());

			if (this.plugin.commandmentsBroadcastFoodEaten)
			{
				godSayToBelievers(godName, LanguageManager.LANGUAGESTRING.GodToBelieverHolyFoodSacrifice, 2 + this.random.nextInt(10));
			}
			else
			{
				godSayToBeliever(godName, believer.getUniqueId(), LanguageManager.LANGUAGESTRING.GodToBelieverHolyFoodSacrifice);
			}

			strikePlayerWithLightning(believer.getUniqueId(), 1 + this.random.nextInt(3));

			return;
		}

		float value = getSacrificeValueForGod(godName, type);

		this.plugin.getLanguageManager().setPlayerName(believer.getDisplayName());

		try
		{
			this.plugin.getLanguageManager().setType(this.plugin.getLanguageManager().getItemTypeName(type));
		}
		catch (Exception ex)
		{
			this.plugin.logDebug(ex.getStackTrace().toString());
		}

		if (value > 10.0F)
		{
			addMoodForGod(godName, getPleasedModifierForGod(godName));
			this.plugin.getBelieverManager().addPrayer(believer.getUniqueId(), godName);

			blessPlayer(godName, believer.getUniqueId(), godPower);
			godSayToBeliever(godName, believer.getUniqueId(), LanguageManager.LANGUAGESTRING.GodToBelieverGoodSacrifice);

			this.plugin.getBelieverManager().increasePrayerPower(believer.getUniqueId(), 1);
		}
		else if (value >= -5.0F)
		{
			godSayToBeliever(godName, believer.getUniqueId(), LanguageManager.LANGUAGESTRING.GodToBelieverMehSacrifice);
		}
		else
		{
			addMoodForGod(godName, getAngryModifierForGod(godName));
			strikePlayerWithLightning(believer.getUniqueId(), 1 + this.random.nextInt(3));
			godSayToBeliever(godName, believer.getUniqueId(), LanguageManager.LANGUAGESTRING.GodToBelieverBadSacrifice);
		}

		value -= 1.0F;

		this.godsConfig.set(godName + ".SacrificeValues." + type.name(), Float.valueOf(value));

		saveTimed();
	}

	private float getSacrificeValueForGod(String godName, Material type)
	{
		return (float) this.godsConfig.getDouble(godName + ".SacrificeValues." + type.name());
	}

	private Material getSacrificeUnwantedForGod(String godName)
	{
		List<Material> unwantedItems = new ArrayList();
		ConfigurationSection configSection = this.godsConfig.getConfigurationSection(godName + ".SacrificeValues.");
		if (configSection != null)
		{
			for (String itemType : configSection.getKeys(false))
			{
				Material item = null;
				try
				{
					item = Material.valueOf(itemType);
				}
				catch (Exception ex)
				{
					continue;
				}
				if (this.godsConfig.getDouble(godName + ".SacrificeValues." + itemType) <= 0.0D)
				{
					unwantedItems.add(item);
				}
			}
		}
		else
		{
			return null;
		}
		if (unwantedItems.size() == 0)
		{
			return null;
		}
		return (Material) unwantedItems.get(this.random.nextInt(unwantedItems.size()));
	}
	
	public void manageSacrifices(String godName)
	{
		if (!this.plugin.sacrificesEnabled)
		{
			return;
		}
		
		int godPower = 1 + (int) this.plugin.getGodManager().getGodPower(godName);
		if (this.random.nextInt(20 + (int) (70.0F / godPower)) > 0)
		{
			return;
		}
		Material type = getSacrificeNeedForGod(godName);

		float value = getSacrificeValueForGod(godName, type);

		value += 1 + this.random.nextInt(3);
		if (value > 64.0F)
		{
			value = 64.0F;
		}
		else if (value < -64.0F)
		{
			value = -64.0F;
		}
		this.plugin.logDebug("Increasing wanted " + type.name() + " sacrifice need for " + godName + " to " + value);

		this.godsConfig.set(godName + ".SacrificeValues." + type.name(), Float.valueOf(value));

		saveTimed();

		type = getSacrificeUnwantedForGod(godName);
		
		if (type != null)
		{
			value = 0.25F * getSacrificeValueForGod(godName, type);
			if (value > -0.5D)
			{
				value = 0.0F;
			}
			this.plugin.logDebug("Reducing unwanted " + type.name() + " sacrifice need for " + godName + " to " + value);
			if (value == 0.0F)
			{
				this.godsConfig.set(godName + ".SacrificeValues." + type.name(), null);
			}
			else
			{
				this.godsConfig.set(godName + ".SacrificeValues." + type.name(), Float.valueOf(value));
			}
			save();
		}
		
		

		if (this.random.nextInt(10) > 0)
		{
			return;
		}

		this.clearDroppedItems();
	}

	public void addDroppedItem(int entityID, String playerName)
	{
		this.droppedItems.put(Integer.valueOf(entityID), playerName);
	}

	public String getDroppedItemPlayer(int entityID)
	{
		return (String) this.droppedItems.get(Integer.valueOf(entityID));
	}

	public void clearDroppedItems()
	{
		this.plugin.logDebug("Cleared " + this.droppedItems.size() + " dropped items");
		this.droppedItems.clear();
	}
}