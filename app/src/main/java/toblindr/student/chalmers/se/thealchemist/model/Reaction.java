package toblindr.student.chalmers.se.thealchemist.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Reaction {
    private final List<Item> reactants;
    private final Set<Item> products;

    Reaction(List<Item> reactants, Set<Item> products) {
        this.reactants = reactants;
        this.products = products;

    }

    public Collection<Item> getReactants(){
        return new ArrayList<>(reactants);
    }


    public Set<Item> getProducts() {
        return new HashSet<>(products);
    }

    public boolean hasReactant(Item item) {
        return reactants.contains(item);
    }

    public boolean hasProduct(Item item){
        return products.contains(item);
    }
    public boolean hasProducts(Collection<Item> products){
        return this.products.equals(products);
    }

    public boolean hasReactants(Collection<Item> reactants) {
        List<Item> copyOfReactants=new ArrayList<>(this.reactants);
        List<Item> copyOfGivenReactants = new ArrayList<>(reactants);
        for(Item r: reactants){
            if(copyOfReactants.contains(r)){
                copyOfReactants.remove(r);
            }else{
                return false;
            }
        }

        for(Item r:this.reactants){
            if(copyOfGivenReactants.contains(r)){
                copyOfGivenReactants.remove(r);
            }else{
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction = (Reaction) o;
        return hasReactants(reaction.getReactants()) &&
                hasProducts(reaction.getProducts());
    }

    @Override
    public int hashCode() {
        return reactants.hashCode()+products.hashCode();
    }
}
