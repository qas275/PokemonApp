package vttp2022.proj.pokemonTeam.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Trainer {
    private String trainerName;
    private Pokemon[] pokeTeam;
    private String searchPokeString;
    public List<String> pokeList = new LinkedList<>();
    private String searchPoke;
    
    public Trainer(){

    }
        
    public Trainer(String username){
        this.trainerName = username;
    }
    
    public String getSearchPoke() {
        return searchPoke;
    }

    public void setSearchPoke(String searchPoke) {
        this.searchPoke = searchPoke;
    }

    public List<String> getPokeList() {
        return pokeList;
    }

    public void setPokeList(List<String> pokeList) {
        this.pokeList = pokeList;
    }

    public String getSearchPokeString() {
        return searchPokeString;
    }

    public void setSearchPokeString(String searchPokeString) {
        this.searchPokeString = searchPokeString;
    }
    
    public String getTrainerName() {
        return trainerName;
    }
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
    public Pokemon[] getPokeTeam() {
        return pokeTeam;
    }
    public void setPokeTeam(Pokemon[] pokeTeam) {
        this.pokeTeam = pokeTeam;
    }
}
