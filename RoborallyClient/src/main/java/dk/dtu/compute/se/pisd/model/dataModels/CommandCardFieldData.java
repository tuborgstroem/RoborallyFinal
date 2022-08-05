package dk.dtu.compute.se.pisd.model.dataModels;

import dk.dtu.compute.se.pisd.model.CommandCard;
import dk.dtu.compute.se.pisd.model.CommandCardField;

public class CommandCardFieldData {

    private CommandCard card;

    private boolean visible;

    public CommandCardFieldData(CommandCardField field){
        this.card = field.getCard();
        this.visible = field.isVisible();
    }
}
