package vttp2022.proj.pokemonTeam.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Pokemon {
    private String id;
    private String name;
    private HashMap<String, String> statsMap = new HashMap<>();
    private HashMap<String, String> typesMap = new HashMap<>();
    private Stats[] stats;
    private Types[] types;
    
    private static final Logger logger = LoggerFactory.getLogger(Pokemon.class);
    
    public HashMap<String, String> getStatsMap() {
        return statsMap;
    }
    public void setStatsMap(HashMap<String, String> statsMap) {
        this.statsMap = statsMap;
    }
    public HashMap<String, String> getTypesMap() {
        return typesMap;
    }
    public void setTypesMap(HashMap<String, String> typesMap) {
        this.typesMap = typesMap;
    }
    public String getID() { return id; }
    public void setID(String value) { this.id = value; }
    
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public Stats[] getStats() { return stats; }
    public void setStats(Stats[] value) { this.stats = value; }

    public Types[] getTypes() { return types; }
    public void setTypes(Types[] value) { this.types = value; }

    //creating new pokemon with response from api
    public static Pokemon createJson(String jsonString){
        Pokemon poke = new Pokemon();
        logger.info("WITHIN MODEL CREATEJSON");
        try(InputStream is = new ByteArrayInputStream(jsonString.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject jobject = reader.readObject();
            logger.info("JSON OBJ CREATED, ID >>> "+ jobject.get("id").toString());

            poke.id = jobject.get("id").toString();//getJsonstring then get string method has issue here
            poke.name = jobject.getString("name");
            logger.info("WITHIN MODEL POKEMON ID >>> "+poke.id+poke.name);

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
            // List<Stats> holdingStatsList = new LinkedList<>();
            // for(int i=0; i<jArrayStats.size();i++){
            //     JsonObject currentStats = jArrayStats.get(i).asJsonObject();
            //     Stats holdingStats = Stats.createStats(currentStats);
            //     holdingStatsList.add(holdingStats);
            // }
            // Stats[] arr = new Stats[holdingStatsList.size()];
            // for(int j=0;j<holdingStatsList.size();j++){
            //     arr[j] = holdingStatsList.get(j);
            // }
            // poke.stats=arr;
            logger.info("STATNAME HP >>>> "+poke.statsMap.get("hp"));
            logger.info("STATNAME ATTACK >>>> "+poke.statsMap.get("attack"));
            logger.info(jArrayStats.toString());
            
            //Types Unmarshalling
            JsonArray jArrayTypes = jobject.get("types").asJsonArray();
            for(int i=0; i<jArrayTypes.size();i++){
                JsonObject currentStatJsonObject = jArrayTypes.getJsonObject(i);
                String typeName = currentStatJsonObject.getJsonObject("type").get("name").toString();
                String typeSlot = currentStatJsonObject.get("slot").toString();
                poke.typesMap.put(typeName, typeSlot);
            }
            // List<Types> holdingTypesList = new LinkedList<>();
            // for (int j=0;j<jArrayTypes.size();j++){
            //     JsonObject currentTypes = jArrayTypes.getJsonObject(j);
            //     Types holdingTypes = Types.createTypes(currentTypes);
            //     holdingTypesList.add(holdingTypes);
            // }
            // Types[] arrTypes = new Types[holdingTypesList.size()];
            // for(int k=0; k<holdingTypesList.size();k++){
            //     arrTypes[k] = holdingTypesList.get(k);
            // }
            // poke.types=arrTypes;
            //poke.types = (Types[]) jobject.getJsonArray("types").toArray(); //Doesnt work
            // Stats[] statholder = poke.stats;
            // String asd = statholder[0].getStatName();
            //String holder = poke.stats[0].getStatName();
            //logger.info("STATNAME>>> "+ holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poke;
    }
}
