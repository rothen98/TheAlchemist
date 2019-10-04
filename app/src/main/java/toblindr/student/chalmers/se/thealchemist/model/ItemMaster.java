package toblindr.student.chalmers.se.thealchemist.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class ItemMaster {
    private Set<Item> knownItems;
    private Set<Item> unknownItems;
    private Set<Item> importantItems;

    public ItemMaster() {
        knownItems=new HashSet<>();
        unknownItems=new HashSet<>();
        importantItems=new HashSet<>();
    }

    /**
     * An important item is an item that always is known
     * @param item
     */
    public void addImportantItem(Item item){
        importantItems.add(item);
        knownItems.add(item);
    }
    public void addKnownItem(Item item) {
        if(item!=null){
            knownItems.add(item);
        }

    }

    public void addUnknownItem(Item item) {
        if(item!=null){
            unknownItems.add(item);
        }

    }

    public void setUnknownItemAsKnown(Item item){
        if(item!=null){
            if(unknownItems.remove(item)){
                knownItems.add(item);
            }

        }

    }

    public boolean isItemKnown(Item i) {
        if(i==null){
            return false;
        }else{
            return knownItems.contains(i);
        }
    }

    public Collection<Item> getAllKnownItems() {
        return new HashSet<>(knownItems);
    }

    public void reset() {
        knownItems.removeAll(importantItems);
        unknownItems.addAll(knownItems);
        knownItems.clear();
        knownItems.addAll(importantItems);

    }

    public int getTotalNumberOfItems() {
        return knownItems.size()+unknownItems.size();
    }

    public int getNumberOfKnownItems() {
        return knownItems.size();
    }

    public Collection<Item> getAllUnknownItems() {
        return new HashSet<>(unknownItems);
    }
}
