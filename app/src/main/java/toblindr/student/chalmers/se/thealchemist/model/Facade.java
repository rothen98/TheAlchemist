package toblindr.student.chalmers.se.thealchemist.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Facade {
    private Alchemist alchemist;
    private ItemsCollector collector;

    public Facade(Collection<String> reactions, Collection<String> allItems) {

        ItemCreator.setItems(initAllItems(allItems)); //Important to do first
        collector = new ItemsCollector();
        alchemist = createAlchemist(reactions);
    }

    private Map<String, String> initAllItems(Collection<String> allItems) {
        Map<String,String> toReturn = new HashMap<>();
        for(String item:allItems){
            String[] info = item.split(":");
            toReturn.put(info[0],info[1]);
        }
        return toReturn;
    }

    private Alchemist createAlchemist(Collection<String> reactions){

        Set<Reaction> allReactions = new HashSet<>();
        Set<Reaction> knownReactions = new HashSet<>();

        for(String s: reactions){
            String[] parts = s.split("[+=:]");
            try {
                Reaction newReaction = new Reaction(ItemCreator.createItem(parts[0]),
                        ItemCreator.createItem(parts[1]),
                        ItemCreator.createItem(parts[2]));
                allReactions.add(newReaction);
                if(parts.length==4 && parts[3].toLowerCase().equals("true")){
                    knownReactions.add(newReaction);
                    collector.addItem(newReaction.getProduct());
                }
            } catch (ItemDoNotExistException e) {
                //Do nothing
            }

        }

        return new Alchemist(allReactions,knownReactions,collector);

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

    public int getNumberOfItemsKnown(){
        return collector.getAllKnownItems().size();
    }

    public int getTotalNumberOfItems(){
       return ItemCreator.getAllItems().size();
    }


    public Collection<Reaction> getAllUnknownReactions() {
        return alchemist.getUnKnownReactions();
    }

    public Collection<Reaction> getAllKnownReactions(){
        return alchemist.getKnownReactions();
    }
}
