package toblindr.student.chalmers.se.thealchemist.model;

import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Alchemist {
    private final Set<Reaction> allReactions;
    private final Set<Reaction> knownReactions;
    private final ItemsCollector itemsCollector;


    public Alchemist(Set<Reaction> allReactions, Set<Reaction> knownReactions, ItemsCollector collector) {

        this.itemsCollector = collector;
        this.allReactions = new HashSet<>(allReactions);
        this.knownReactions = new HashSet<>(knownReactions);
        initReactions();

    }

    private void initReactions() {

    }

    /**
     * Performs a reaction between the given items. If succesful, the products are returned.
     * If failed, null is returned.
     *
     * @return an item or null
     */
    public Reaction generate(Collection<Item> reactants) {
            for (Reaction reaction: allReactions){
                if (reaction.hasReactants(reactants)){
                    knownReactions.add(reaction);
                    for(Item item:reaction.getProducts()){
                        itemsCollector.addItem(item);
                    }

                    return reaction;
                }
            }
            return null;
    }
    public Set<Reaction> getKnownReactionsItemIsPartOf(Item item){
        Set<Reaction> setToReturn = new HashSet<>();
        for (Reaction reaction: knownReactions){
            if(reaction.hasReactant(item)){
                setToReturn.add(reaction);
            }
        }
        return setToReturn;
    }

    public Set<Reaction> getUnKnownReactionsItemIsPartOf(Item item){
        Set<Reaction> copyOfAll = new HashSet<>(allReactions);
        copyOfAll.removeAll(knownReactions);
        Set<Reaction> setToReturn = new HashSet<>();
        for (Reaction reaction: copyOfAll){
            if(reaction.hasReactant(item)){
                setToReturn.add(reaction);
            }
        }
        return setToReturn;
    }

    public Set<Reaction> getAllReactionsItemIsPartOf(Item item){
        Set<Reaction> setToReturn = new HashSet<>();
        for (Reaction reaction: allReactions){
            if(reaction.hasReactant(item)){
                setToReturn.add(reaction);
            }
        }
        return setToReturn;
    }

    public Set<Reaction> getAllReactionsItemIsProductOf(Item item){
        Set<Reaction> setToReturn = new HashSet<>();
        for (Reaction reaction: allReactions){
            if(reaction.hasProduct(item)){
                setToReturn.add(reaction);
            }
        }
        return setToReturn;
    }

    public Set<Reaction> getKnownReactionsItemIsProductOf(Item item){
        Set<Reaction> setToReturn = new HashSet<>();
        for (Reaction reaction: knownReactions){
            if(reaction.hasProduct(item)){
                setToReturn.add(reaction);
            }
        }
        return setToReturn;
    }

    public Set<Reaction> getUnKnownReactionsItemIsProductOf(Item item){
        Set<Reaction> copyOfAll = new HashSet<>(allReactions);
        copyOfAll.removeAll(knownReactions);
        Set<Reaction> setToReturn = new HashSet<>();
        for (Reaction reaction: copyOfAll){
            if(reaction.hasProduct(item)){
                setToReturn.add(reaction);
            }
        }
        return setToReturn;
    }


    public Collection<Reaction> getUnKnownReactions() {
        HashSet<Reaction> toReturn = new HashSet<>(allReactions);
        toReturn.removeAll(knownReactions);
        return toReturn;
    }
    public Collection<Reaction> getKnownReactions(){
        return new HashSet<>(knownReactions);
    }

    public void reset() {
        knownReactions.clear();
    }
}
