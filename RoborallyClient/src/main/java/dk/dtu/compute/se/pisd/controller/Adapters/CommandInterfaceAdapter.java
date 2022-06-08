package dk.dtu.compute.se.pisd.controller.Adapters;

import com.google.gson.*;
import dk.dtu.compute.se.pisd.model.programming.ICommand;

import java.lang.reflect.Type;

public class CommandInterfaceAdapter implements JsonSerializer<ICommand>, JsonDeserializer<ICommand> {

    @Override
    public JsonElement serialize(ICommand src, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));

        return result;
    }

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
