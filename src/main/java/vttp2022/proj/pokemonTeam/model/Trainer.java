package vttp2022.proj.pokemonTeam.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component("Trainer")
public class Trainer implements Serializable{
    private String trainerName;
    private String searchPokeString;
    private String searchPokeCatString;
    public Pokemon reqPoke;
    public String[] pokeArrString= new String[1];
    public Pokemon[] pokeArrPoke = new Pokemon[1];
    public Pokemon[] pokeSearchArrPoke = new Pokemon[1];
    
    public Trainer(){}
    
    public Trainer(String username){
        this.trainerName = username;
        this.pokeArrString[0] = "Your Team is empty";
    }
    
    public Pokemon[] getPokeArrPoke() {
        return pokeArrPoke;
    }
    
    public void setPokeArrPoke(Pokemon[] pokeArrPoke) {
        this.pokeArrPoke = pokeArrPoke;
    }
    
    public String[] getPokeArrString() {
        return pokeArrString;
    }
    
    public void setPokeArrString(String[] pokeArrString) {
        this.pokeArrString = pokeArrString;
    }
    
    public String getSearchPokeString() {
        return searchPokeString;
    }
    
    public void setSearchPokeString(String searchPokeString) {
        this.searchPokeString = searchPokeString;
    }
    
    public String getSearchPokeCatString() {
        return searchPokeCatString;
    }
    
    public void setSearchPokeCatString(String searchPokeCatString) {
        this.searchPokeCatString = searchPokeCatString;
    }
    
    public String getTrainerName() {
        return trainerName;
    }
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
    public Pokemon getReqPoke() {
        return reqPoke;
    }
    
    public void setReqPoke(Pokemon reqPoke) {
        this.reqPoke = reqPoke;
    }
    
    public Pokemon[] getPokeSearchArrPoke() {
        return pokeSearchArrPoke;
    }
    
    public void setPokeSearchArrPoke(Pokemon[] pokeSearchArrPoke) {
        this.pokeSearchArrPoke = pokeSearchArrPoke;
    }
}
