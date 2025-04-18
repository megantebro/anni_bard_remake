package lobby.anni_bard_remake;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Anni_bard_remake extends JavaPlugin implements Listener {


    private Map<Block,BardEffect>activeJukeBox = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this,this);
        applyEffectNearbyPlayers();
        playJukeBoxSound();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void openCustomGUI(Player player){
        Inventory gui = Bukkit.createInventory(null,9,"bardbox");
        ItemStack item1 = new ItemStack(Material.MUSIC_DISC_MALL);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("再生");
        item1.setItemMeta(meta1);
        gui.setItem(0, item1);

        ItemStack item2 = new ItemStack(Material.MUSIC_DISC_FAR);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName("速度上昇");
        item2.setItemMeta(meta2);
        gui.setItem(1, item2);

        ItemStack item3 = new ItemStack(Material.MUSIC_DISC_STRAD);
        ItemMeta meta3 = item3.getItemMeta();
        meta3.setDisplayName("採掘速度上昇");
        item3.setItemMeta(meta3);
        gui.setItem(2, item3);

        ItemStack item4 = new ItemStack(Material.MUSIC_DISC_MELLOHI);
        ItemMeta meta4 = item4.getItemMeta();
        meta4.setDisplayName("鈍足");
        item4.setItemMeta(meta4);
        gui.setItem(3, item4);

        ItemStack item5 = new ItemStack(Material.MUSIC_DISC_STAL);
        ItemMeta meta5 = item5.getItemMeta();
        meta5.setDisplayName("発光");
        item5.setItemMeta(meta5);
        gui.setItem(4, item5);
        player.openInventory(gui);
    }
    //ジュークボックスの所有者を格納する変数
    private BiMap<Player,Block> playerByJukeboxBlock = HashBiMap.create();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block != null && block.getType() == Material.JUKEBOX) {
            playerByJukeboxBlock.put(event.getPlayer(),block);
            openCustomGUI(event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!event.getView().getOriginalTitle().equals("bardbox"))return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem == null || clickedItem.getType() == Material.AIR)return;
        if(!(event.getWhoClicked() instanceof Player))return;
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();
        Block block = playerByJukeboxBlock.get(player);

        if(block != null && block.getType() == Material.JUKEBOX){
            if(clickedItem.getType() == Material.MUSIC_DISC_MALL){
                activeJukeBox.put(block,BardEffect.REGEN_BUFF);
            }
            if(clickedItem.getType() == Material.MUSIC_DISC_FAR){
                activeJukeBox.put(block,BardEffect.SPEED_BUFF);
            }
            if(clickedItem.getType() == Material.MUSIC_DISC_STRAD){
                activeJukeBox.put(block,BardEffect.HASTE);
            }
            if(clickedItem.getType() == Material.MUSIC_DISC_MELLOHI){
                activeJukeBox.put(block,BardEffect.SLOW);
            }
            if(clickedItem.getType() == Material.MUSIC_DISC_STAL){
                activeJukeBox.put(block,BardEffect.GLOWING);
            }
        }
    }



    public void applyEffectNearbyPlayers(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<Block,BardEffect>entry : activeJukeBox.entrySet()){
                    Block jukeBox = entry.getKey();
                    BardEffect bardEffect = entry.getValue();
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.getLocation().distance(jukeBox.getLocation()) < bardEffect.getRange()){
                            bardEffect.getEffect().apply(player);
                        }
                    }
                }
            }
        }.runTaskTimer(this,2,0);
    }
    public void playJukeBoxSound(){
        new BukkitRunnable(){
            @Override
            public void run(){
                for(Map.Entry<Block,BardEffect>entry : activeJukeBox.entrySet()){
                    Block jukeBox = entry.getKey();
                    BardEffect bardEffect = entry.getValue();
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.stopSound(bardEffect.getBgm());
                        player.playSound(jukeBox.getLocation(),bardEffect.getBgm(),1f,1f);
                    }
                }
            }
        }.runTaskTimer(this,60 * 20,0);
    }
}
