package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.*;
import dk.dtu.compute.se.pisd.roborally.model.programming.ICommand;

import java.lang.reflect.Type;

/**
 * @author Tobias Borgstrøm s184810
 * spicfied serializer for ICommand implementations
 */
public class CommandInterfaceAdapter implements JsonSerializer<ICommand>, JsonDeserializer<ICommand> {

    /**
     * Converts Icommand into json
     * @return json of Icommand implementation
     * @throws JsonParseException
     */
    @Override
    public JsonElement serialize(ICommand src, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
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
    public ICommand deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        try {
            return context.deserialize(jsonElement, Class.forName("dk.dtu.compute.se.pisd.roborally.model.programming." + type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
