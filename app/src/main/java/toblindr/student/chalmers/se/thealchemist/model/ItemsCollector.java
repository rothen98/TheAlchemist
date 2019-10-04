package toblindr.student.chalmers.se.thealchemist.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class ItemsCollector {
    private Collection<Item> knownItems;




    public ItemsCollector() {
        this.knownItems = new HashSet<>();
        initKnownItems();


    }

    private void addItem(String itemName) {
        try {
            knownItems.add(ItemCreator.createItem(itemName));
        } catch (ItemDoNotExistException e) {

            //Do nothing...
        }
    }

    public void addItem(Item item){
        knownItems.add(item);
    }

    public Collection<Item> getAllKnownItems(){
        return new ArrayList<>(knownItems);
    }

    public void reset() {
        knownItems.clear();
        initKnownItems();
    }

    private void initKnownItems() {
        addItem("Fire");
        addItem("Water");
        addItem("Air");
        addItem("Earth");
    }

    public boolean itemsAreKnown(Collection<Item> reactants) {
        for(Item i:reactants){
            if(!knownItems.contains(i)){
                return false;
            }
        }
        return true;
    }
}
