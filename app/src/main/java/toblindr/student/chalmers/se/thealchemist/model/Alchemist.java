package toblindr.student.chalmers.se.thealchemist.model;

import com.google.common.collect.Table;

class Alchemist {
    private Table<String,String,String> alchemistTable;
    private ItemsCollector itemsCollector;

    /**
     *
     * @param alchemistTable the table with all the possible reactions
     */
    public Alchemist(Table<String, String, String> alchemistTable, ItemsCollector collector) {
        this.alchemistTable = alchemistTable;
        this.itemsCollector = collector;

    }

    /**
     * Performs a reaction between the two given items. If succesful, an item is returned.
     * If failed, null is returned.
     * @param itemOne the first item
     * @param itemTwo the second item
     * @return an item or null
     */
    public Item generate(Item itemOne, Item itemTwo) {
        try {
            if (alchemistTable.contains(itemOne.getName(), itemTwo.getName())) {
                Item newItem = ItemCreator.createItem(alchemistTable.get(itemOne.getName(),
                        itemTwo.getName()));
                itemsCollector.addItem(newItem);
                return newItem;
            } else if (alchemistTable.contains(itemTwo.getName(), itemOne.getName())) {
                Item newItem =ItemCreator.createItem(alchemistTable.get(itemTwo.getName(),
                        itemOne.getName()));
                itemsCollector.addItem(newItem);
                return newItem;
            }
        } catch (ItemDoNotExistException e) {
            return null;
        }
        return null;
    }
}
