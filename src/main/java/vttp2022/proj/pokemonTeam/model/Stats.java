package vttp2022.proj.pokemonTeam.model;

import jakarta.json.JsonObject;

public class Stats {
    private String baseStat;
    private Stat statName;

    public String getBaseStat() {
        return baseStat;
    }
    public void setBaseStat(String baseStat) {
        this.baseStat = baseStat;
    }
    public Stat getStatName() {
        return statName;
    }
    public void setStatName(Stat statName) {
        this.statName = statName;
    }

    public static Stats createStats(JsonObject jObject){
        Stats pokeStats = new Stats();
        pokeStats.setBaseStat(jObject.get("base_stat").toString());
        JsonObject statJObject = jObject.get("stat").asJsonObject();
        pokeStats.setStatName(Stat.createStat(statJObject));
        
        return pokeStats;
    }
}
