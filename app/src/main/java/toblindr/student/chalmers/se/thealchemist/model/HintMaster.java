package toblindr.student.chalmers.se.thealchemist.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class HintMaster {
    private Map<Item,List<Reaction>> hints;
    private Set<Item> currentlyActiveHints;
    private Random random = new Random();

    public HintMaster() {
        hints=new HashMap<>();
        currentlyActiveHints = new HashSet<>();
    }

    public void addItem(Item thisItem) {
        hints.put(thisItem,new ArrayList<Reaction>());
    }

    public void addReaction(Reaction newReaction) {
        for(Item item:newReaction.getProducts()){
            List<Reaction> reactionList = hints.get(item);
            if(reactionList!=null && !reactionList.contains(newReaction)){
                reactionList.add(newReaction);
            }else{
                List<Reaction> newList = new ArrayList<>();
                newList.add(newReaction);
                hints.put(item,newList);
            }
        }
    }

    /**
     * Retrieves random hint and removes it from the hints
     * @return
     */
    public Item getHint(){
        Item toReturn = null;
        for(Item i:hints.keySet()){
            List<Reaction> list = hints.get(i);
            if(list!=null && !list.isEmpty()){
                toReturn =i;
                break;
            }


        }
        if(toReturn!=null){
            hints.remove(toReturn);
            currentlyActiveHints.add(toReturn);
        }
        return toReturn;
    }

    public void itemsNowKnown(Set<Item> products) {
        for(Item item:products){
            hints.remove(item);
            currentlyActiveHints.remove(item);
        }

    }

    public void setReactionAsKnown(Reaction possibleReaction) {
        for(Item product:possibleReaction.getProducts()){
            hints.remove(product);
        }
    }


    public void itemNowKnown(Item product) {
        hints.remove(product);
        currentlyActiveHints.remove(product);
    }

    /**
     * Resets the hints by clearing all hints.
     *
     */
    public void reset() {
        currentlyActiveHints.clear();
        hints.clear();
    }

}
