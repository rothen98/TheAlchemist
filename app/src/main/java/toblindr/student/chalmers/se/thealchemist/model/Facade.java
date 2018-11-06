package toblindr.student.chalmers.se.thealchemist.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Facade {
    private Alchemist alchemist;
    private ItemsCollector collector;

    public Facade(Collection<String> reactions, Map<String,String> allItems) {
        ItemCreator.setItems(allItems); //Important to do first
        collector = new ItemsCollector();
        alchemist = new Alchemist(initTable(reactions),collector);
    }

    private Table<String, String, String> initTable(Collection<String> reactions) {
        Table<String,String,String> toReturn = HashBasedTable.create();
        for(String reaction:reactions){
            String[] parts = reaction.split("/+|=");
            if(parts.length==3) {
                toReturn.put(parts[0], parts[1], parts[2]);
            }

        }
        return toReturn;
    }

    public Item tryReaction(Item itemOne, Item itemTwo){
        return alchemist.generate(itemOne,itemTwo);
    }

    public Collection<Item> getKnownItems(){
        return collector.getAllKnownItems();
    }

    public Collection<Item> getAllItems() {
        return ItemCreator.getAllItems();
    }
}
