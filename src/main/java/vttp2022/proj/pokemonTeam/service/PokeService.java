package vttp2022.proj.pokemonTeam.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp2022.proj.pokemonTeam.model.Pokemon;
import vttp2022.proj.pokemonTeam.model.Trainer;

@Service
public class PokeService {
    private static final Logger logger = LoggerFactory.getLogger(PokeService.class);

    @Autowired
    @Qualifier("PokeRedis")
    RedisTemplate<String, Trainer> redisTemplate;
    
    public Pokemon reqPoke;

    String overallURL = "https://pokeapi.co/api/v2/pokemon";
    String userInputUsername;
    Trainer currentTrainer;

    //login to existing user or create new user in Redis
    public Trainer getRedisTrainer(String username){
        userInputUsername = username;
        boolean existingTrainer=redisTemplate.hasKey(username);
        logger.info(username + " is EXISTING TRAINER>>> "+ existingTrainer);
        if (!existingTrainer){
            currentTrainer = new Trainer(username);
            redisTemplate.opsForValue().setIfAbsent(username, currentTrainer);
        }else{
            currentTrainer = (Trainer) redisTemplate.opsForValue().get(username);//TODO username mapped correctly, check poketeam mapping when add done
            logger.info(currentTrainer.getTrainerName());
            if(null!=currentTrainer.getPokeArrString()){ //
                logger.info("TEST >>> "+currentTrainer.getPokeArrString().toString());
                logger.info("CURRENT LOGGED IN TRAINER>>> "+currentTrainer.getTrainerName());
                logger.info("SIZE OF TRAINER TEAM >>> "+ currentTrainer.getPokeArrString().length);
            }
        }
        return currentTrainer;
    }

    public Optional<Pokemon> getApiPokemon(String searchName){
        String reqPokeUrl = overallURL + "/" +searchName.toLowerCase();
        RestTemplate templatePoke = new RestTemplate();
        ResponseEntity<String> resp = null;
        try {
            resp = templatePoke.exchange(reqPokeUrl, HttpMethod.GET, null, String.class, 1);
            Pokemon reqPoke = Pokemon.createJson(resp.getBody());
            logger.info("WITHIN SERVICE>>> "+reqPoke.getStatsMap().get("hp"));
            return Optional.of(reqPoke);
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Trainer addPoketoTeam(Pokemon pokeToAdd, Trainer currentTrainer){
        Trainer trainerToUpdate = currentTrainer;
        String[] newTeam = new String[1];
        if(null!=currentTrainer.getPokeArrString()){
            if(currentTrainer.getPokeArrString().length<6){
                newTeam = new String[currentTrainer.getPokeArrString().length +1];
                for (int i=0; i< currentTrainer.getPokeArrString().length; i++){
                    newTeam[i]=currentTrainer.getPokeArrString()[i];
                }
            }
        }
        newTeam[newTeam.length-1]=pokeToAdd.getName();    
        trainerToUpdate.setPokeArrString(newTeam);
        redisTemplate.opsForValue().set(trainerToUpdate.getTrainerName(), trainerToUpdate);
        return trainerToUpdate;
    }

    // public Optional<Berry> getBerry(String berryInput){
    //     String urlBerry = overallURL + "/" + berryInput;
    //     RestTemplate template = new RestTemplate();
    //     RequestEntity req = RequestEntity.get(urlBerry).build();
    //     ResponseEntity<String> resp = template.exchange(req, String.class);

    //     Berry berryObj = new Berry();
    //     try(InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
    //         JsonReader r = Json.createReader(is);
    //         JsonObject obj = r.readObject();
    //         berryObj.setID(obj.getInt("id"));
    //         berryObj.setSize(obj.getInt("size"));
    //         berryObj.setName(obj.getString("name"));
            
    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }
        
    //     Optional<Berry> = 
    //     return berryInfo;
    // }
    
}
