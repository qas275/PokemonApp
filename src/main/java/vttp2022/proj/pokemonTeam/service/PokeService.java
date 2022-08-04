package vttp2022.proj.pokemonTeam.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
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

    public Trainer getRedisTrainer(String username){
        userInputUsername = username;
        boolean existingTrainer=redisTemplate.hasKey(username);
        System.out.println(username + " is EXISTING TRAINER>>> "+ existingTrainer);
        if (!existingTrainer){
            currentTrainer = new Trainer(username);
            redisTemplate.opsForValue().setIfAbsent(username, currentTrainer);
        }else{
            currentTrainer = (Trainer) redisTemplate.opsForValue().get(username);//TODO check mapping correct or not
            if(null!=currentTrainer.getPokeTeam()){
                for(Pokemon poke:currentTrainer.getPokeTeam()){
                    currentTrainer.pokeList.add(poke.getName());
                }
            }
            logger.info("CURRENT LOGGED IN TRAINER>>> "+currentTrainer.getTrainerName());
            logger.info("SIZE OF TRAINER TEAM >>> "+ currentTrainer.getPokeList().size());
        }
        return currentTrainer;
    }

    public Pokemon getApiPokemon(String searchName){
        String reqPokeUrl = overallURL + "/" +searchName;
        RestTemplate templatePoke = new RestTemplate();
        ResponseEntity<String> resp = null;
        resp = templatePoke.exchange(reqPokeUrl, HttpMethod.GET, null, String.class, 1);
        Pokemon reqPoke = Pokemon.createJson(resp.getBody());
        return reqPoke;
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
