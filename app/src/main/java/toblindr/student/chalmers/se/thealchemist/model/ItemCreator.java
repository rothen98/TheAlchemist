package toblindr.student.chalmers.se.thealchemist.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemCreator {

    private static Map<String,String> allItems;

    public static Item createItem(String name) throws ItemDoNotExistException {
        String path = allItems.get(name);
        if(path!=null){
            System.out.println(path);
            return create(name,path);
        }else{
            throw new ItemDoNotExistException();
        }

    }

    private static Item create(String name, String path) {
        return new Item(name,path);
    }

    public static void setItems(Map<String,String> items){
        allItems = items;
    }

    public static Collection<Item> getAllItems(){
        List<Item> items = new ArrayList<>();
        for(String key:allItems.keySet()){
            items.add(create(key,allItems.get(key)));
        }
        return items;
    }
}
