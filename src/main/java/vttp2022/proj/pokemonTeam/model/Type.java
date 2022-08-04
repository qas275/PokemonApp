package vttp2022.proj.pokemonTeam.model;

import jakarta.json.JsonObject;

public class Type {
    private String name;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public static Type createType(JsonObject jsonObject) {
        Type pokeType = new Type();
        pokeType.name = jsonObject.get("name").toString();
        return pokeType;
    }
}
