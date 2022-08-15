package vttp2022.proj.pokemonTeam.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Pokemon {
    private String id;
    private String name;
    private Stats[] stats;
    private Types[] types;

    private static final Logger logger = LoggerFactory.getLogger(Pokemon.class);
    
    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public Stats[] getStats() { return stats; }
    public void setStats(Stats[] value) { this.stats = value; }

    public Types[] getTypes() { return types; }
    public void setTypes(Types[] value) { this.types = value; }

    public static Pokemon createJson(String jsonString){
        Pokemon poke = new Pokemon();
        logger.info("WITHIN MODEL CREATEJSON");
        try(InputStream is = new ByteArrayInputStream(jsonString.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject jobject = reader.readObject();
            logger.info("WITHIN TRY");
            logger.info(jobject.get("id").toString());
            //creating new pokemon with response from api
            poke.id = jobject.get("id").toString();//getJsonstring then get string method has issue here, not sure why cannot work
            poke.name = jobject.get("name").toString();
            logger.info("WITHIN MODEL"+poke.id);
            JsonArray jArrayStats = jobject.get("stats").asJsonArray(); //need to do manual setting for each sub model
            List<Stats> holdingStatsList = new LinkedList<>();    
            for(int i=0; i<jArrayStats.size();i++){
                JsonObject currentStats = jArrayStats.get(i).asJsonObject();
                Stats holdingStats = Stats.createStats(currentStats);
                holdingStatsList.add(holdingStats);
            }
            Stats[] arr = new Stats[holdingStatsList.size()];
            for(int j=0;j<holdingStatsList.size();j++){
                arr[j] = holdingStatsList.get(j);
            }
            poke.stats=arr;
            logger.info("STATNAME >>>> "+poke.stats[0].getStatName().getStatName());
            logger.info("STATNAME >>>> "+poke.stats[1].getStatName().getStatName());
            logger.info(jArrayStats.toString());
            //poke.stats = (Stats[]) jobject.getJsonArray("stats").toArray(); //TODO this casting does not work, need manual assignment
            //TODO write code in stats and types to take in array and manual assign and call here
            JsonArray jArrayTypes = jobject.get("types").asJsonArray();
            List<Types> holdingTypesList = new LinkedList<>();
            for (int j=0;j<jArrayTypes.size();j++){
                JsonObject currentTypes = jArrayTypes.getJsonObject(j);
                Types holdingTypes = Types.createTypes(currentTypes);
                holdingTypesList.add(holdingTypes);
            }
            Types[] arrTypes = new Types[holdingTypesList.size()];
            for(int k=0; k<holdingTypesList.size();k++){
                arrTypes[k] = holdingTypesList.get(k);
            }
            poke.types=arrTypes;
            //poke.types = (Types[]) jobject.getJsonArray("types").toArray(); //Doesnt work
            // Stats[] statholder = poke.stats;
            // String asd = statholder[0].getStatName();
            String holder = poke.stats[0].getStatName().getStatName();
            logger.info("STATNAME>>> "+ holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poke;
    }
}
