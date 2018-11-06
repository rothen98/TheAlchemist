package toblindr.student.chalmers.se.thealchemist.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AlchemistTest {

    @BeforeClass
    public static void setUpClass(){
        initMap();
    }
    Table<String,String,String> table = initTable();
    ItemsCollector collector = new ItemsCollector();

    private static void initMap(){
        Map<String,String> newMap = new HashMap<>();
        newMap.put("Fire","path");
        newMap.put("Water","path");
        newMap.put("Air","path");
        newMap.put("Earth","path");
        newMap.put("Steam","path");
        newMap.put("Lava","path");
        newMap.put("Mud","path");

        ItemCreator.setItems(newMap);

    }
    private List<String> initItems() {
        List<String> items = new ArrayList<>();
        items.add("Fire");
        items.add("Water");
        items.add("Air");
        items.add("Earth");
        items.add("Steam");
        items.add("Lava");
        items.add("Mud");
        return items;
    }

    private HashBasedTable<String, String, String> initTable() {
        HashBasedTable<String,String,String> table = HashBasedTable.create();
        table.put("Fire","Water","Steam");
        table.put("Fire","Earth","Lava");
        table.put("Water","Earth","Mud");
        return table;
    }

    private Alchemist alchemist = new Alchemist(table, collector);

    @Test
    public void generate(){
        List<Item> knownItems = new ArrayList<>(collector.getAllKnownItems());
        assertNull(alchemist.generate(knownItems.get(0),knownItems.get(0)));

        int counter = 0;
        for(int k = 0; k<knownItems.size(); k++) {
            for (int i = 0; i < knownItems.size(); i++) {
                Item item = alchemist.generate(knownItems.get(i), knownItems.get(k));
                if(item != null){
                    System.out.println(item.getName());
                    counter++;
                }
            }
        }



    }
}