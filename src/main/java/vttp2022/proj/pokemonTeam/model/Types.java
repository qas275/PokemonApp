package vttp2022.proj.pokemonTeam.model;

import jakarta.json.JsonObject;

public class Types {
    private String slot;
    private String type;
    
    public String getSlot() {
        return slot;
    }
    public void setSlot(String slot) {
        this.slot = slot;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public static Types createTypes(JsonObject currentTypes) {
        Types pokeTypes = new Types();
        pokeTypes.slot = currentTypes.get("slot").toString();
        pokeTypes.type = currentTypes.getJsonObject("type").get("name").toString();
        return pokeTypes;
    }

}
