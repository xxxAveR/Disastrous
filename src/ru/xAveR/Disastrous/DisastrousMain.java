package ru.xAveR.Disastrous;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DisastrousMain extends JavaPlugin implements Listener{
	String myDisplayName = ChatColor.GOLD + "" + ChatColor.BOLD + "Живая Вода";
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		
		ItemStack myItem = new ItemStack(Material.POTION, 1, (byte)0); 
		ItemMeta im = myItem.getItemMeta();
		im.setDisplayName(myDisplayName);
		myItem.setItemMeta(im);
		
		ShapedRecipe livingwater = new ShapedRecipe(myItem);
		livingwater.shape("GIG","QBQ","GIG");
		livingwater.setIngredient('B', Material.GLASS_BOTTLE);
		livingwater.setIngredient('I', Material.INK_SACK);
		livingwater.setIngredient('G', Material.GLOWSTONE_DUST);
		livingwater.setIngredient('Q', Material.QUARTZ);
		getServer().addRecipe(livingwater);
		
		saveDefaultConfig();
	}
	@Override
	public void onDisable(){
		
	}


	  
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player death = event.getEntity();
		Player killer = event.getEntity().getKiller();
		if(death instanceof Player){
		if(!(death.hasPermission("disastrous.immunity"))){
		if(death.hasPermission("disastrous.death")){
			death.setMaxHealth(death.getMaxHealth() - this.getConfig().getDouble("Number deductible health"));
			if(killer.hasPermission("disastrous.kill")){
				if(killer.getMaxHealth() < 40){
					killer.setMaxHealth(killer.getMaxHealth() + this.getConfig().getDouble("Number increase health"));
					}else{
						killer.sendMessage("Ты убил достаточно много людей...");
						killer.sendMessage(ChatColor.RED + "" + ChatColor.BOLD +"... и больше не являешься гибельным!");
						PermissionUser user = PermissionsEx.getUser(killer);
						user.addPermission("disastrous.immunity");
						killer.setMaxHealth(20);
					}
				}
			}
		if(this.getConfig().getBoolean("Ban players") == true){
			if(death.getMaxHealth() <= 1){
			death.kickPlayer(death.getName() +", Вы утратили запас жизней.");
			death.setBanned(true);
					}
				}
			}
		}
	}
	@EventHandler
	public void useLiveWater(PlayerItemConsumeEvent event){
		Player player = event.getPlayer();
		ItemStack itemM = event.getItem();
		if(itemM.getItemMeta().getDisplayName() == myDisplayName){
			if(!(player.hasPermission("disastrous.immunity"))){
				PermissionUser user = PermissionsEx.getUser(player);
				user.addPermission("disastrous.immunity");
				player.setMaxHealth(20);
				player.sendMessage(ChatColor.RED + "Вы больше не гибельный!");
			}else {
				player.sendMessage(ChatColor.RED + "Вы уже выпили порцию живой воды!");
				event.setCancelled(true);
			}
		} 
	}
}
