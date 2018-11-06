package toblindr.student.chalmers.se.thealchemist.model;

public class Item {
    private String name;
    private String imagePath;

    Item (String name,String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }


    String getName(){
        return name;
    }
    public String getImagePath(){
        return imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {

        return name.hashCode();
    }
}
