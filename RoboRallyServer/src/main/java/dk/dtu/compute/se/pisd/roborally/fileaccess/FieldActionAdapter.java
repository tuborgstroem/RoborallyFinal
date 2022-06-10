package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.*;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;

import java.lang.reflect.Type;

public class FieldActionAdapter implements JsonSerializer<FieldAction>, JsonDeserializer<FieldAction> {

    @Override
    public JsonElement serialize(FieldAction src, Type typeOfT, JsonSerializationContext context) throws JsonParseException {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        if(src instanceof ConveyorBelt){
            result.addProperty("heading", String.valueOf(((ConveyorBelt) src).getHeading()));
        }

        return result;
    }

    @Override
    public FieldAction deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        try {
            return context.deserialize(jsonElement, Class.forName("dk.dtu.compute.se.pisd.roborally.model.fieldActions." + type));
        }
         catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
