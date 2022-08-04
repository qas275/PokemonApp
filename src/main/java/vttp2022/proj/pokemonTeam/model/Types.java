package vttp2022.proj.pokemonTeam.model;

import jakarta.json.JsonObject;

public class Types {
    private String slot;
    private Type type;
    
    public String getSlot() {
        return slot;
    }
    public void setSlot(String slot) {
        this.slot = slot;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public static Types createTypes(JsonObject currentTypes) {
        Types pokeTypes = new Types();
        pokeTypes.slot = currentTypes.get("slot").toString();
        pokeTypes.type = Type.createType(currentTypes.getJsonObject("type"));
        return pokeTypes;
    }

}
