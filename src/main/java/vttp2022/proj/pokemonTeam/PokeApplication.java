package vttp2022.proj.pokemonTeam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokeApplication {

	private Logger logger = LoggerFactory.getLogger(PokeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(PokeApplication.class, args);
	}

}
