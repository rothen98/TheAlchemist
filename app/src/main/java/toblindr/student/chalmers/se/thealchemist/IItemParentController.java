package toblindr.student.chalmers.se.thealchemist;

import toblindr.student.chalmers.se.thealchemist.model.Item;

interface IItemParentController {

    void reaction(ItemHolder item, float itemX, float itemY, ItemHolder itemTwo, float itemTwoX, float itemTwoY);
}
