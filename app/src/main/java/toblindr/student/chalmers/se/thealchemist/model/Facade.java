package toblindr.student.chalmers.se.thealchemist.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Facade {
    private Alchemist alchemist;
    private HintMaster hintMaster;
    private ItemMaster itemMaster;

    public Facade(Collection<String> reactions, Collection<String> allItems) {

        ItemCreator.setItems(initAllItems(allItems)); //Important to do first
        create(initAllItems2(allItems),reactions);

        //collector = new ItemsCollector();
        //alchemist = createAlchemist(reactions);
    }

    private Map<String, String> initAllItems(Collection<String> allItems) {
        Map<String, String> toReturn = new HashMap<>();
        for (String item : allItems) {
            String[] info = item.split("[=:]");
            toReturn.put(info[0], info[1]);
        }
        return toReturn;
    }

    //Should return name, [known,imagepath]
    private Map<String,String[]> initAllItems2(Collection<String> allItems){
        Map<String, String[]> toReturn = new HashMap<>();
        for (String item : allItems) {
            String[] info = item.split(":",2);
            toReturn.put(info[0], info[1].split(":"));
        }
        return toReturn;
    }

    private void create(Map<String,String[]> allItems, Collection<String> reactions){
        itemMaster = new ItemMaster();
        hintMaster = new HintMaster();
        Set<Reaction> knownReactions = new HashSet<>();
        Set<Reaction> unKnownReactions = new HashSet<>();

        for (Map.Entry<String, String[]> entry : allItems.entrySet()) {
            String itemName = entry.getKey();
            String[] array = entry.getValue();

            Item thisItem = new Item(itemName,array[0]);
            if(array[1].equalsIgnoreCase("known")){
                itemMaster.addKnownItem(thisItem);
            }else if(array[1].equalsIgnoreCase("important")){
                itemMaster.addImportantItem(thisItem);
            }
            else{
                itemMaster.addUnknownItem(thisItem);
                hintMaster.addItem(thisItem);
            }
        }
        for (String s : reactions) {
            String[] parts = s.split("[=:]");
            if (parts.length == 2 || parts.length==3) {
                try {
                    List<Item> reactants = initReactants(parts[0]);
                    boolean reactantsKnown = checkIfItemsKnown(reactants);
                    Set<Item> products = initProducts(parts[1]);
                    Reaction newReaction = new Reaction(reactants, products);
                    for(Item i:products){
                        if(!itemMaster.isItemKnown(i)){
                            if(reactantsKnown){
                                hintMaster.addReaction(newReaction);
                            }
                            unKnownReactions.add(newReaction);
                        }else{
                            if (parts.length == 3 && parts[2].toLowerCase().equals("true")) {
                                knownReactions.add(newReaction);
                            }else{
                                unKnownReactions.add(newReaction);
                            }
                        }
                    }
                } catch (ItemDoNotExistException e) {
                    //Couldn't create item
                }
            }
        }
        alchemist=new Alchemist(knownReactions,unKnownReactions,itemMaster,hintMaster);
    }

    private boolean checkIfItemsKnown(Collection<Item> items) {
        for(Item item:items){
            if (!itemMaster.isItemKnown(item)){
                return false;
            }
        }
        return true;
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
        return itemMaster.getAllKnownItems();
    }

    public Collection<Item> getAllItems() {
        return ItemCreator.getAllItems();
    }

    public int getNumberOfItemsKnown() {
        return itemMaster.getNumberOfKnownItems();
    }

    public int getTotalNumberOfItems() {
        return itemMaster.getTotalNumberOfItems();
    }


    public Collection<Reaction> getAllUnknownReactions() {
        return alchemist.getUnKnownReactions();
    }

    public Collection<Reaction> getAllKnownReactions() {
        return alchemist.getKnownReactions();
    }

    /**
     *
     * @return an unknown item or null if there are no unknown items
     */
    public Item getHint(){
        return hintMaster.getHint();
    }

    public void resetGame() {
        alchemist.reset();
        itemMaster.reset();
        hintMaster.reset();

        for(Item i:itemMaster.getAllUnknownItems()){
            hintMaster.addItem(i);
        }
        for(Reaction r:alchemist.getUnKnownReactions()){
            for(Item product: r.getProducts()){
                if(!itemMaster.isItemKnown(product) && checkIfItemsKnown(r.getReactants())){
                    hintMaster.addReaction(r);
                }
            }
        }
    }


    public Collection<Item> getAllKnownItems() {
        return itemMaster.getAllKnownItems();
    }

    public Collection<Item> getAllUnKnownItems(){
        return itemMaster.getAllUnknownItems();
    }

}
