package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.*;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;

import java.lang.reflect.Type;

public class FieldActionAdapter implements JsonSerializer<FieldAction> {

    @Override
    public JsonElement serialize(FieldAction src, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));

        return result;
    }
}
