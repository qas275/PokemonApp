package vttp2022.proj.pokemonTeam.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.proj.pokemonTeam.service.PokeService;

@Component("Pokemon")
public class Pokemon implements Serializable{
    public String idNum;
    public boolean checked = false; 
    public String name;
    private HashMap<String, String> statsMap = new HashMap<>();
    private String[] types;
    private URI imgURL;

    private static final Logger logger = LoggerFactory.getLogger(Pokemon.class);
    
    @Autowired
    PokeService PokeSvc;
    
    public HashMap<String, String> getStatsMap() { return statsMap; }
    public void setStatsMap(HashMap<String, String> statsMap) { this.statsMap = statsMap; }
    
    public boolean isChecked() {return checked;}
    public void setChecked(boolean checked) {this.checked = checked;}

    public URI getImgURL() { return imgURL; }
    public void setImgURL(URI imgURL) { this.imgURL = imgURL; }
    
    public String getID() { return idNum; }
    public void setID(String value) { this.idNum = value; }
    
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    public String[] getTypes() { return types; }
    public void setTypes(String[] value) { this.types = value; }

    public Pokemon(){

    }

    public Pokemon(String pokename){
        this.name = pokename;
    }

    //creating new pokemon with response from api
    public static Pokemon createJson(String jsonString){
        Pokemon poke = new Pokemon();
        logger.info("WITHIN MODEL CREATEJSON");
        try(InputStream is = new ByteArrayInputStream(jsonString.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject jobject = reader.readObject();
            logger.info("JSON OBJ CREATED, ID >>> "+ jobject.get("id").toString());
            poke.idNum = jobject.get("id").toString();//getJsonstring then get string method has issue here
            poke.name = jobject.getString("name");
            logger.info("WITHIN MODEL POKEMON ID >>> "+poke.idNum+poke.name);
            poke.imgURL = URI.create(jobject.getJsonObject("sprites").getJsonObject("other").getJsonObject("official-artwork").getString("front_default"));
            logger.info("CURRENT POKE ID >>> "+poke.idNum);
            
            //Stats Unmarshalling
            JsonArray jArrayStats = jobject.get("stats").asJsonArray(); //need to do manual setting for each sub model
            for(int i=0; i<jArrayStats.size();i++){
                JsonObject currentStatJsonObject = jArrayStats.getJsonObject(i);
                String statName = currentStatJsonObject.getJsonObject("stat").getString("name");
                logger.info("WITHIN POKEMON STATNAME>>>" + statName);
                String baseStat = currentStatJsonObject.get("base_stat").toString();
                logger.info("WITHIN POKEMON BASESTAT>>" + baseStat);
                poke.statsMap.put(statName, baseStat);
            }
            logger.info("STATNAME HP >>>> "+poke.statsMap.get("hp"));
            logger.info("STATNAME ATTACK >>>> "+poke.statsMap.get("attack"));
            logger.info(jArrayStats.toString());
            
            //Types Unmarshalling
            JsonArray jArrayTypes = jobject.get("types").asJsonArray();
            poke.setTypes(new String[jArrayTypes.size()]);
            for(int i=0; i<jArrayTypes.size();i++){
                JsonObject currentStatJsonObject = jArrayTypes.getJsonObject(i);
                String typeName = currentStatJsonObject.getJsonObject("type").getString("name");
                poke.types[i] = typeName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poke;
    }


    public static String[] createPokemonArr(String jsonString){
        String[] pokemonStringArr = new String[6];
        Random rand = new Random();
        try(InputStream is = new ByteArrayInputStream(jsonString.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject jobject = reader.readObject();
            logger.info("JSON OBJ CREATED, ID >>> "+ jobject.get("id").toString());
            JsonArray pokeJsArr = jobject.get("pokemon").asJsonArray();
            int pokeArrSize = pokeJsArr.size(); 
            for(int i=0; i<6;i++){
                int randNum = rand.nextInt(pokeArrSize);
                pokemonStringArr[i] = pokeJsArr.getJsonObject(randNum).getJsonObject("pokemon").getString("name");
                logger.info("I >>> " + i + " POKENAME >>> " + pokemonStringArr[i]);
            }
            return pokemonStringArr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemonStringArr;
    }
}
