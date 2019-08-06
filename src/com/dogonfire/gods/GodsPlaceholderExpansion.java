package com.dogonfire.gods;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import com.dogonfire.gods.Gods;
import com.dogonfire.gods.managers.BelieverManager;
import com.dogonfire.gods.managers.GodManager;

/**
 * This class will be registered through the register-method in the 
 * plugins onEnable-method.
 */
class GodsPlaceholderExpansion extends PlaceholderExpansion {

    private Gods plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public GodsPlaceholderExpansion(Gods plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     * 
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest 
     * method to obtain a value if a placeholder starts with our 
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "gods";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier 
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %gods_name_space%
        if("name_space".equals(identifier)){
        	String god = "";
        	
        	god = BelieverManager.instance().getGodForBeliever(player.getUniqueId());
        	
        	if (god == null) {
        		god = "";
        	}
        	else {
        		god = god + " ";
        	}
        	
            return god;
        }
        
     // %gods_name%
        if("name".equals(identifier)){
        	String god = "";
        	
        	god = BelieverManager.instance().getGodForBeliever(player.getUniqueId());
        	
        	if (god == null) {
        		god = "";
        	}
        	
            return god;
        }

        // %gods_prayerpower%
        if("prayerpower".equals(identifier)){
            return String.valueOf(BelieverManager.instance().getPrayerPower(player.getUniqueId()));
        }
        
        // %gods_color%
        if("color".equals(identifier)){
        	String god = "";
        	
        	god = BelieverManager.instance().getGodForBeliever(player.getUniqueId());
        	
        	if (god == null) {
        		god = "";
        	}
        	
        	ChatColor godColor = GodManager.instance().getColorForGod(god);
        	
            return "&" + String.valueOf(godColor.getChar());
        }
 
        // We return null if an invalid placeholder (f.e. %gods_cakenomnom%) 
        // was provided
        return null;
    }
}