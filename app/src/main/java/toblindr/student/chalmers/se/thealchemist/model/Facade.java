package toblindr.student.chalmers.se.thealchemist.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        Map<String, String> toReturn = new HashMap<>();
        for (String item : allItems) {
            String[] info = item.split(":");
            toReturn.put(info[0], info[1]);
        }
        return toReturn;
    }

    private Alchemist createAlchemist(Collection<String> reactions) {

        Set<Reaction> allReactions = new HashSet<>();
        Set<Reaction> knownReactions = new HashSet<>();

        for (String s : reactions) {
            String[] parts = s.split("[=:]");
            if (parts.length == 2 || parts.length==3) {
                try {

                    List<Item> reactants = initReactants(parts[0]);
                    Set<Item> products = initProducts(parts[1]);
                    Reaction newReaction = new Reaction(reactants, products);
                    allReactions.add(newReaction);
                    if (parts.length == 3 && parts[2].toLowerCase().equals("true")) {
                        knownReactions.add(newReaction);
                        for (Item p : products) {
                            collector.addItem(p);
                        }
                    }
                } catch (ItemDoNotExistException e) {
                    //Do nothing
                }
            }

        }

        return new Alchemist(allReactions, knownReactions, collector);

    }

    private Set<Item> initProducts(String part) throws ItemDoNotExistException {
        Set<Item> products = new HashSet<>();
        String[] splitted =part.split("[+]");
        for (int i = 0; i < splitted.length; i++) {
            products.add(ItemCreator.createItem(splitted[i]));
        }
        return products;
    }

    private List<Item> initReactants(String part) throws ItemDoNotExistException {
        List<Item> reactants = new ArrayList<>();
        String[] splitted =part.split("[+]");
        for (int i = 0; i < splitted.length; i++) {
            reactants.add(ItemCreator.createItem(splitted[i]));
        }
        return reactants;
    }


    public Reaction tryReaction(List<Item> reactants) {
        return alchemist.generate(reactants);
    }

    public Collection<Item> getKnownItems() {
        return collector.getAllKnownItems();
    }

    public Collection<Item> getAllItems() {
        return ItemCreator.getAllItems();
    }

    public int getNumberOfItemsKnown() {
        return collector.getAllKnownItems().size();
    }

    public int getTotalNumberOfItems() {
        return ItemCreator.getAllItems().size();
    }


    public Collection<Reaction> getAllUnknownReactions() {
        return alchemist.getUnKnownReactions();
    }

    public Collection<Reaction> getAllKnownReactions() {
        return alchemist.getKnownReactions();
    }

    public void resetGame() {
        alchemist.reset();
        collector.reset();
    }
}
