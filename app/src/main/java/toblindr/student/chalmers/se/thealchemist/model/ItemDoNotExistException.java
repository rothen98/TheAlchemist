package toblindr.student.chalmers.se.thealchemist.model;

class ItemDoNotExistException extends Exception {
    public ItemDoNotExistException() {
        super("No item with the given name");
    }
}
