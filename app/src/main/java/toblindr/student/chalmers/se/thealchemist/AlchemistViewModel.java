package toblindr.student.chalmers.se.thealchemist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import toblindr.student.chalmers.se.thealchemist.model.Facade;
import toblindr.student.chalmers.se.thealchemist.model.Item;
import toblindr.student.chalmers.se.thealchemist.model.Reaction;

public class AlchemistViewModel extends AndroidViewModel {

    private Facade facade;
    private DataHandler dataHandler;
    private MutableLiveData<Integer> currentAmountOfKnownItems;
    private MutableLiveData<Item> latestDiscoveredItem;
    public AlchemistViewModel(@NonNull Application application, String items, String reactions) {
        super(application);
        this.dataHandler = new DataHandler();
        facade = new Facade(initReactions(application.getResources(),reactions),
                initItems(application.getResources(),items));

        currentAmountOfKnownItems=new MutableLiveData<>();
        latestDiscoveredItem=new MutableLiveData<>();
        updateAmountOfKnownItems();

    }


    private void updateAmountOfKnownItems(){
        currentAmountOfKnownItems.setValue(facade.getNumberOfItemsKnown());
    }
    public Map<Item,ItemStatus> tryReaction(List<Item> reactants){
        Map<Item,ItemStatus> items = new HashMap<>();
        Collection<Reaction> reactionsBefore = facade.getAllKnownReactions();
        Collection<Item> knownItemsBefore = facade.getKnownItems();
        Reaction createdReaction = facade.tryReaction(reactants);
        if(createdReaction!= null){
            if(reactionsBefore.contains(createdReaction)){
                for(Item item:createdReaction.getProducts()){
                    items.put(item,ItemStatus.OLD);
                }
            }else{
                for(Item item:createdReaction.getProducts()){
                    if(knownItemsBefore.contains(item)){
                        items.put(item,ItemStatus.NEW_REACTION);
                    }else{
                        items.put(item,ItemStatus.NEW);
                        updateAmountOfKnownItems();
                        updateLatestItemDiscovered(item);
                    }
                }
            }
        }
        return items;


    }

    private void updateLatestItemDiscovered(Item item) {
        latestDiscoveredItem.setValue(item);
    }

    public int getNumberOfDiscoveredItems() {
        return facade.getNumberOfItemsKnown();
    }
    public int getTotalNumberOfItems(){
        return facade.getTotalNumberOfItems();
    }

    private Collection<String> initItems(Resources resources,String phonefile) {

        List<String> itemsRawFile = dataHandler.readRawTextFile(R.raw.items,resources);
        if(phonefile.equals("EMPTY")){
            return combineItems(new ArrayList<String>(),itemsRawFile);
        }
        String itemsPhone[] = phonefile.split("\n");
        List<String> phone = new ArrayList<>(Arrays.asList(itemsPhone));
        return combineItems(phone,itemsRawFile);
    }
    //todo this method should probably be more efficient
    private Collection<String> combineItems(Collection<String> phoneFile, Collection<String> rawFile) {
        Collection<String> toReturn = new ArrayList<>();
        Collection<String> toRemove = new ArrayList<>();
        for (String string: rawFile){
           String[] parts =  string.split("[=:]");
           if(parts.length==3){
               toReturn.add(string);
               toRemove.add(string);
               String itemInfo = parts[0]+":"+parts[1]+":";
               String known = (itemInfo+"known");
               String unknown = (itemInfo+"unknown");
               if(phoneFile.contains(known)){
                   phoneFile.remove(known);
               }else if(phoneFile.contains(unknown)){
                   phoneFile.remove(unknown);
               }
           }
        }
        rawFile.removeAll(toRemove);
        for(String string:phoneFile){
            String[] parts =  string.split("[=:]");
            String itemInfo = parts[0]+":"+parts[1];
            if(rawFile.contains(itemInfo)){
                toReturn.add(string);
                rawFile.remove(itemInfo);
            }

        }

        for(String string:rawFile){
            toReturn.add(string+":unknown");
        }

        return toReturn;
    }

    private Collection<String> initReactions(Resources resources, String phoneFile) {
        List<String> reactionsRawFile = dataHandler.readRawTextFile(R.raw.reactions,resources);
        if(phoneFile.equals("EMPTY")){
            return combineReactions(new ArrayList<String>(),reactionsRawFile);
        }
        List<String> reactionsPhone = new ArrayList<>(Arrays.asList(phoneFile.split("\n")));
        reactionsPhone.remove(reactionsPhone.size()-1);

        return combineReactions(reactionsPhone,reactionsRawFile);
    }

    private Collection<String> combineReactions(Collection<String> phoneFiles, Collection<String> rawFiles){
        Collection<String> toReturn = new ArrayList<>();
        for(String r:phoneFiles){
            String justReaction = r.split(":")[0];
            if(rawFiles.contains(justReaction)){
                Log.i("Info","Added: " + r);
                Log.i("Info","Removed: " + justReaction);
                toReturn.add(r);
                rawFiles.remove(justReaction);
            }
        }
        toReturn.addAll(rawFiles);

        return toReturn;
    }

    public String getSavedReactions(){
        StringBuilder sb = new StringBuilder();
        for (Reaction r : facade.getAllUnknownReactions()) {
            for(Item item:r.getReactants()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append("=");
            for(Item item:r.getProducts()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        for (Reaction r : facade.getAllKnownReactions()) {
            for(Item item:r.getReactants()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append("=");
            for(Item item:r.getProducts()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append(":true");
            sb.append("\n");
        }
        sb.setLength(sb.length()-1);
        return  sb.toString();


    }

    public String getSavedItems() {
        StringBuilder sb = new StringBuilder();
        for (Item item : facade.getAllKnownItems()) {
            sb.append(item.getName());
            sb.append(":");
            sb.append(item.getImagePath());
            sb.append(":");
            sb.append("known");
            sb.append("\n");
        }
        for (Item item : facade.getAllUnKnownItems()) {
            sb.append(item.getName());
            sb.append(":");
            sb.append(item.getImagePath());
            sb.append(":");
            sb.append("unknown");
            sb.append("\n");
        }

        sb.setLength(sb.length()-1);


        return sb.toString();
    }
    public Collection<Reaction> getAllUnknownReactions() {
        return facade.getAllUnknownReactions();
    }

    public Collection<Reaction> getAllKnownReactions() {
        return facade.getAllKnownReactions();
    }

    public Collection<Item> getKnownItems() {
        return facade.getKnownItems();
    }

    public void resetGame() {
        facade.resetGame();
        updateAmountOfKnownItems();
    }

    public Item getHint() {
        return facade.getHint();
    }

    // Create a LiveData with a String


    public MutableLiveData<Integer> getCurrentAmountOfKnownItems() {
        if (currentAmountOfKnownItems == null) {
            currentAmountOfKnownItems = new MutableLiveData<>();
            updateAmountOfKnownItems();
        }
        return currentAmountOfKnownItems;

    }

    public MutableLiveData<Item> getLatestDiscoveredItem(){
        if(latestDiscoveredItem==null){
            latestDiscoveredItem=new MutableLiveData<>();
        }
        return latestDiscoveredItem;
    }
}
