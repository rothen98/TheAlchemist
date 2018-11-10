package toblindr.student.chalmers.se.thealchemist.model;

import java.util.Objects;

public class Reaction {
    private final Item reactantOne;
    private final Item reactantTwo;
    private final Item product;

    Reaction(Item reactantOne, Item reactantTwo, Item product) {
        this.reactantOne = reactantOne;
        this.reactantTwo = reactantTwo;
        this.product = product;
    }

    public Item getReactantOne() {
        return reactantOne;
    }

    public Item getReactantTwo() {
        return reactantTwo;
    }

    public Item getProduct() {
        return product;
    }

    public boolean hasReactant(Item item) {
        return reactantOne.equals(item)||reactantTwo.equals(item);
    }

    public boolean hasProduct(Item item){
        return product.equals(item);
    }

    public boolean hasReactants(Item itemOne, Item itemTwo) {
        return (reactantOne.equals(itemOne) && reactantTwo.equals(itemTwo)) ||
                (reactantOne.equals(itemTwo) && reactantTwo.equals(itemOne));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction = (Reaction) o;
        return hasReactants(reaction.reactantOne,reaction.reactantTwo) &&
                hasProduct(reaction.product);
    }

    @Override
    public int hashCode() {

        return reactantOne.hashCode()+reactantTwo.hashCode()+product.hashCode();
    }
}
