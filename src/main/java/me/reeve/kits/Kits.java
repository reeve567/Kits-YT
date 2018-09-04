package me.reeve.kits;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Kits extends JavaPlugin {
	
	private HashMap<UUID, Date> miner = new HashMap<>();
	private Config playerData = new Config(getDataFolder(), "player-data");
	
	@Override
	public void onEnable() {
		super.onEnable();
		setupDefault();
		loadData();
	}
	
	private void setupDefault() {
		if (playerData.getInt("ver") != 1) {
			playerData.set("ver", 1);
			playerData.set("kits.miner", new ArrayList<String>());
			playerData.set("kits.king", new ArrayList<String>());
			playerData.set("kits.knight", new ArrayList<String>());
			playerData.saveConfig();
		}
	}
	
	private void loadData() {
		List<String> minerData = playerData.getStringList("kits.miner");
		for (String s : minerData) {
			UUID uuid = UUID.fromString(s.substring(0, s.indexOf(";")));
			Date date = new Date(Long.parseLong(s.substring(s.indexOf(";") + 1)));
			miner.put(uuid, date);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		playerData.set("kits.miner", getMinerList());
		
		playerData.saveConfig();
	}
	
	private ArrayList<String> getMinerList() {
		ArrayList<String> temp = new ArrayList<>();
		for (UUID uuid : miner.keySet()) {
			temp.add(uuid.toString() + ";" + miner.get(uuid).getTime());
		}
		return temp;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getLabel().equalsIgnoreCase("kit")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					sender.sendMessage(new String[]{
							"§e§lKits Available:",
							"§aKnight",
							"§aKing",
							"§aMiner",
							""
					});
				} else if (args[0].equalsIgnoreCase("miner")) {
					if (sender.hasPermission("kits.miner")) {
						if (useKit((Player) sender, "miner")) {
							miner.put(((Player) sender).getUniqueId(), new Date());
							ItemStack[] list = new ItemStack[]{
									new ItemStack(Material.IRON_PICKAXE),
									new ItemStack(Material.COOKED_BEEF, 64)
								
							};
							Player name = (Player) sender;
							name.getInventory().addItem(list);
							sender.sendMessage("Kit received");
						} else {
							sender.sendMessage("You do not have this kit yet.");
						}
					} else {
						sender.sendMessage("You do not have permission");
					}
				} else if (args[0].equalsIgnoreCase("king")) {
					ItemStack[] list = new ItemStack[]{
							new ItemStack(Material.GOLD_HELMET),
							new ItemStack(Material.NETHER_STAR),
							new ItemStack(Material.GOLD_BLOCK, 16)
					};
					Player name = (Player) sender;
					name.getInventory().addItem(list);
					sender.sendMessage("Kit received");
				} else if (args[0].equalsIgnoreCase("knight")) {
					ItemStack[] list = new ItemStack[]{
							new ItemStack(Material.IRON_HELMET),
							new ItemStack(Material.IRON_CHESTPLATE),
							new ItemStack(Material.IRON_LEGGINGS),
							new ItemStack(Material.IRON_BOOTS),
							new ItemStack(Material.IRON_SWORD)
					};
					Player name = (Player) sender;
					name.getInventory().addItem(list);
					sender.sendMessage("Kit received");
				}
			} else {
				sender.sendMessage("Console cannot run this command.");
			}
			
		}
		return true;
	}
	
	private boolean useKit(Player player, String name) {
		switch (name.toLowerCase()) {
			case "knight":
				
				break;
			case "king":
				
				break;
			case "miner":
				if (miner.containsKey(player.getUniqueId()))
					return new Date().getTime() - miner.get(player.getUniqueId()).getTime() >= 86400000;
				return true;
		}
		return false;
	}
}
