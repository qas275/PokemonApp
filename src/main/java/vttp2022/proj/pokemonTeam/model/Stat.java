package vttp2022.proj.pokemonTeam.model;

import jakarta.json.JsonObject;

public class Stat {
    private String statName;

    public String getStatName() {
        return statName;
    }
    public void setStatName(String statName) {
        this.statName = statName;
    }

    public static Stat createStat(JsonObject jObject){
        Stat pokeStat = new Stat();
        pokeStat.setStatName(jObject.get("name").toString());
        return pokeStat;
    }

}
