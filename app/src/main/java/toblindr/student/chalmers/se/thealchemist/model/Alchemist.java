package toblindr.student.chalmers.se.thealchemist.model;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Alchemist {
    //private Map<Item,Set<Reaction>> allReactionsMap;
    private final Map<Item, Set<Reaction>> knownReactionsMap;
    private final Map<Item, Set<Reaction>> unKnownReactionsMap;
    private final HintMaster hintMaster;

    //private final Set<Reaction> allReactions;
    //private final Set<Reaction> knownReactions;

    private final ItemMaster itemMaster;


    public Alchemist(Set<Reaction> knownReactions, Set<Reaction> unKnownReactions, ItemMaster itemMaster, HintMaster hintMaster) {
        this.knownReactionsMap = new HashMap<>();
        this.unKnownReactionsMap = new HashMap<>();
        this.itemMaster = itemMaster;
        initReactions(knownReactions, unKnownReactions);
        this.hintMaster = hintMaster;

    }

    /**
     * Initiates the reaction by splitting up the reactions into known and unknown
     *
     * @param
     * @param knownReactions
     */
    private void initReactions(Set<Reaction> knownReactions, Set<Reaction> unknownReactions) {
        for (Reaction r : knownReactions) {
            for (Item i : r.getReactants()) {
                handleKnownReaction(i, r);
            }

        }
        for (Reaction r : unknownReactions) {
            for (Item i : r.getReactants()) {
                handleUnknownReaction(i, r);
            }

        }

    }

    /**
     * Adds unknown reaction to map
     *
     * @param i
     * @param r
     */
    private void handleUnknownReaction(Item i, Reaction r) {
        Set<Reaction> aSet = unKnownReactionsMap.get(i);
        if (aSet != null) {
            aSet.add(r);
        } else {
            Set<Reaction> newSet = new HashSet<>();
            newSet.add(r);
            unKnownReactionsMap.put(i, newSet);
        }

    }

    /**
     * Adds known reaction to map
     *
     * @param i
     * @param r
     */
    private void handleKnownReaction(Item i, Reaction r) {
        Set<Reaction> aSet = knownReactionsMap.get(i);
        if (aSet != null) {
            aSet.add(r);
        } else {
            Set<Reaction> newSet = new HashSet<>();
            newSet.add(r);
            knownReactionsMap.put(i, newSet);
        }
    }

    /**
     * Performs a reaction between the given items. If successful, the products are returned.
     * If failed, null is returned.
     *
     * @return an item or null
     */
    public Reaction generate(List<Item> reactants) {
        if (reactants != null && !reactants.isEmpty()) {
            Item itemToCheck = reactants.get(0);
            Reaction reaction = getReactionFromUnknown(itemToCheck, reactants);
            if (reaction == null) {
                reaction = getReaction(itemToCheck, knownReactionsMap.get(itemToCheck), reactants);
            }
            return reaction;
        }

        return null;


    }

    private Reaction getReactionFromUnknown(Item itemToCheck, List<Item> reactants) {
        Set<Reaction> setToCheck = unKnownReactionsMap.get(itemToCheck);
        if (setToCheck != null) {
            Reaction possibleReaction = lookForRightReaction(setToCheck, reactants);
            if (possibleReaction != null) {
                //If an item is discovered, and this has made a reaction discoverable, we must update
                //this in hints

                //For all products
                for (Item product : possibleReaction.getProducts()) {
                    //If not known before
                    if (!itemMaster.isItemKnown(product)) {
                        //Tell the hintmaster and itemmaster we discovered a new item
                        hintMaster.itemNowKnown(product);
                        addProductsToItemMaster(possibleReaction.getProducts());
                        //We must check all all unknown reactions the product is a part of
                        Set<Reaction> reactionSet = unKnownReactionsMap.get(product);
                        if (reactionSet != null) {
                            for (Reaction r : reactionSet) {
                                //We must check that all reactants of the reaction are known
                                //If all of them are known we can add this reaction to hintmaster
                                //Even though hintmaster possibly already has this reaction
                                //todo
                                if (areItemsKnown(r.getReactants()) && !areItemsKnown(r.getProducts())) {
                                    hintMaster.addReaction(r);
                                }


                            }
                        }
                    }
                }

                makeReactionToKnown(possibleReaction);
                //make sure to set this reaction as known so that it won't be a hint anymore
                hintMaster.setReactionAsKnown(possibleReaction);

                return possibleReaction;
            }
        }
        return null;

    }

    private boolean areItemsKnown(Collection<Item> items) {
        for (Item item : items) {
            if (!itemMaster.isItemKnown(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes reaction from unknown map and adds reaction to known.
     * Creates new set in known if necessary.
     *
     * @param reaction
     */
    private void makeReactionToKnown(Reaction reaction) {
        for (Item reactant : reaction.getReactants()) {
            unKnownReactionsMap.get(reactant).remove(reaction);
            Set<Reaction> reactionsContainingItem = knownReactionsMap.get(reactant);
            if (reactionsContainingItem == null) {
                reactionsContainingItem = new HashSet<>();
                reactionsContainingItem.add(reaction);
                knownReactionsMap.put(reactant, reactionsContainingItem);
            } else {
                reactionsContainingItem.add(reaction);
            }

        }

    }


    private Reaction getReaction(Item itemToCheck, Set<Reaction> reactions, List<Item> reactants) {
        if (reactions != null) {
            Reaction possibleReaction = lookForRightReaction(reactions, reactants);
            if (possibleReaction != null) {
                addProductsToItemMaster(possibleReaction.getProducts());
                handlePossibleNewReaction(itemToCheck, possibleReaction);
                hintMaster.itemsNowKnown(possibleReaction.getProducts());
                return possibleReaction;
            }
        }
        return null;
    }

    private void addProductsToItemMaster(Set<Item> products) {
        for (Item item : products) {
            if (!itemMaster.isItemKnown(item)) {
                itemMaster.setUnknownItemAsKnown(item);
                Log.i("info", item.toString() + " added");
            }
        }
    }

    private Reaction lookForRightReaction(Set<Reaction> reactions, Collection<Item> reactants) {
        if (reactions != null) {
            for (Reaction r : reactions) {
                if (r.hasReactants(reactants)) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * If the reaction was unknown before, this method takes care of it
     *
     * @param i
     * @param r
     */
    private void handlePossibleNewReaction(Item i, Reaction r) {
        Set<Reaction> reactionSet = knownReactionsMap.get(i);
        if (reactionSet == null) {
            Set<Reaction> newSet = new HashSet<>();
            newSet.add(r);
            knownReactionsMap.put(i, newSet);
            unKnownReactionsMap.get(i).remove(r);
        } else if (!reactionSet.contains(r)) {
            reactionSet.add(r);
            unKnownReactionsMap.get(i).remove(r);
        }
    }

    public Set<Reaction> getKnownReactionsItemIsPartOf(Item item) {
        Set<Reaction> knownReactionsItemIsPartOf = knownReactionsMap.get(item);
        if (knownReactionsItemIsPartOf != null) {
            return new HashSet<>(knownReactionsItemIsPartOf);
        } else {
            return new HashSet<>();
        }

    }

    public Set<Reaction> getUnKnownReactionsItemIsPartOf(Item item) {
        Set<Reaction> unKnownReactionsItemIsPartOf = unKnownReactionsMap.get(item);
        if (unKnownReactionsItemIsPartOf != null) {
            return new HashSet<>(unKnownReactionsItemIsPartOf);
        } else {
            return new HashSet<>();
        }
    }

    public Set<Reaction> getAllReactionsItemIsPartOf(Item item) {
        Set<Reaction> unKnownReactionsItemIsPartOf = unKnownReactionsMap.get(item);
        Set<Reaction> knownReactionsItemIsPartOf = knownReactionsMap.get(item);

        Set<Reaction> toReturn = new HashSet<>();
        if (unKnownReactionsItemIsPartOf != null) {
            toReturn.addAll(unKnownReactionsItemIsPartOf);
        }
        if (knownReactionsItemIsPartOf != null) {
            toReturn.addAll(knownReactionsItemIsPartOf);
        }
        return toReturn;
    }


    public Collection<Reaction> getUnKnownReactions() {
        Collection<Reaction> toReturn = new HashSet<>();
        for (Set<Reaction> reactions : unKnownReactionsMap.values()) {
            toReturn.addAll(reactions);
        }
        return toReturn;
        /*HashSet<Reaction> toReturn = new HashSet<>(allReactions);
        toReturn.removeAll(knownReactions);
        return toReturn;*/
    }

    public Collection<Reaction> getKnownReactions() {
        Collection<Reaction> toReturn = new HashSet<>();
        for (Set<Reaction> reactions : knownReactionsMap.values()) {
            toReturn.addAll(reactions);
        }
        return toReturn;

    }


    public void reset() {
        for(Map.Entry<Item,Set<Reaction>> entry:knownReactionsMap.entrySet()){
            Set<Reaction> reactionSet = entry.getValue();
            if(reactionSet!=null){
                for(Reaction reaction:reactionSet){
                    for(Item reactant: reaction.getReactants()){
                        Set<Reaction> unKnownReactionSet = unKnownReactionsMap.get(reactant);
                        if(unKnownReactionSet!=null){
                            unKnownReactionSet.add(reaction);
                        }else{
                            Set<Reaction> newSet = new HashSet<>();
                            newSet.add(reaction);
                            unKnownReactionsMap.put(reactant,newSet);
                        }
                    }

                }
            }
        }


        knownReactionsMap.clear();
    }
}
