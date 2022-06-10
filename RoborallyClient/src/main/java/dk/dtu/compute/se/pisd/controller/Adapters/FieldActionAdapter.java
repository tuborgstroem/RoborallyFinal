package dk.dtu.compute.se.pisd.controller.Adapters;

import com.google.gson.*;
import dk.dtu.compute.se.pisd.model.fieldActions.ConveyorBelt;
import dk.dtu.compute.se.pisd.model.fieldActions.FieldAction;
import java.lang.reflect.Type;

/**
 * @author Tobias Borgstrøm s184810
 * spicfied serializer for ICommand implementations
 */
public class FieldActionAdapter implements JsonSerializer<FieldAction>, JsonDeserializer<FieldAction> {

    /**
     * Converts FieldAction into json
     * @return json of FieldAction implementation
     * @throws JsonParseException
     */
    @Override
    public JsonElement serialize(FieldAction src, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));

        return result;
    }

    /**
     * converts Json to Icommand implementation based on type variable
     * @return an implementation of Icommand
     * @throws JsonParseException
     */
    @Override
    public FieldAction deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        try {
            return context.deserialize(jsonElement, Class.forName("dk.dtu.compute.se.pisd.model.fieldActions." + type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
