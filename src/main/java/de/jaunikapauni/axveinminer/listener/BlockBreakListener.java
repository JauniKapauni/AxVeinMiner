package de.jaunikapauni.axveinminer.listener;

import de.jaunikapauni.axveinminer.AxVeinMiner;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockBreakListener implements Listener {

    AxVeinMiner reference;
    public BlockBreakListener(AxVeinMiner reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block b = e.getBlock();
        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        if(!e.getPlayer().isSneaking()) return;
        if(!tool.getType().toString().endsWith("_PICKAXE")) return;
        if(!reference.MATERIALS.contains(b.getType())) return;
        List<Block> vein = new ArrayList<>();
        Queue<Block> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(b);
        visited.add(b.getX() + "," + b.getY() + "," + b.getZ());
        int[][] neighbors = {{1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0}, {0,0,1}, {0,0,-1}};
        while (!queue.isEmpty() && vein.size() < reference.MAX_BLOCKS){
            Block current = queue.poll();
            vein.add(current);
            for(int[] off : neighbors){
                Block nb = current.getRelative(off[0], off[1], off[2]);
                String key = nb.getX() + "," + nb.getY() + "," + nb.getZ();
                if(!visited.contains(key) &&
                        nb.getType() == b.getType()){
                    visited.add(key);
                    queue.add(nb);
                }
            }
        }
        e.setDropItems(false);
         b.breakNaturally(tool);
        List<Block> blocks = new ArrayList<>(vein);
        for(int i = 1; i < blocks.size(); i++){
            Block block = blocks.get(i);
            Bukkit.getScheduler().runTaskLater(reference, () -> {
                BlockBreakEvent event = new BlockBreakEvent(block, e.getPlayer());
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()){
                    block.breakNaturally(tool);
                }
            }, (long) i * 20);
        }
    }
}
