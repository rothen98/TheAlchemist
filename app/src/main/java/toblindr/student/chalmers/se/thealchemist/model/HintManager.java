package toblindr.student.chalmers.se.thealchemist.model;

import java.util.HashSet;
import java.util.Set;

class HintManager {
    private Set<Item> unknownItems;

    public HintManager(Set<Item> unknownItems) {
        this.unknownItems = unknownItems;
    }

    public void makeItemsKnown(Set<Item> items){
        unknownItems.removeAll(items);
    }
    public void makeItemKnown(Item item){
        unknownItems.remove(item);
    }

    public Set<Item> getUnknownItems(){
        return new HashSet<>(unknownItems);
    }
}
