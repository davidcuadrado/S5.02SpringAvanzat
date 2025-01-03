package cat.itacademy.s05.t02.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Pet entity that represent every existing pet. ")
@Document(collection = "pet")
public class Pet {

	@Id
	private String id;
	private String userId;
	private String name;
	private PetType petType;
	private PetMood currentMood;
	private int happiness;
	private int energy;
	private int hunger;
	private int hygiene;
	private int health;
	private PetColor petColor;
	private ArrayList<String> specialTreats;
	private PetEnvironment environment;

	public Pet(String name, String userId, PetType petType, PetColor petColor, PetEnvironment environment) {
		this.name = name;
		this.userId = userId;
		this.petType = petType;
		this.petColor = petColor;
		this.environment = environment;
	}

	public String getPetId() {
		return this.id;
	}

	public void setPetId(String setId) {
		this.id = setId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setName(String petName) {
		this.name = petName;
	}

	public String getName() {
		return name;
	}

	public PetType getPetType() {
		return petType;
	}

	public void setPetType(PetType petType) {
		this.petType = petType;
	}

	public PetMood getCurrentMood() {
		return currentMood;
	}

	public void setCurrentMood(PetMood currentMood) {
		this.currentMood = currentMood;
	}

	public int getHappiness() {
		return happiness;
	}

	public void setHappiness(int happiness) {
		this.happiness = happiness;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	
	public ArrayList<String> getSpecialTreats() {
		return specialTreats;
	}

	public void setSpecialTreats(ArrayList<String> specialTreats) {
		this.specialTreats = specialTreats;
	}

	public void addSpecialTreats(String specialTreat) {
		this.specialTreats.add(specialTreat);
	}

	public PetEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(PetEnvironment environment) {
		this.environment = environment;
	}

	public int getHygiene() {
		return hygiene;
	}

	public void setHygiene(int hygiene) {
		this.hygiene = hygiene;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public PetColor getPetColor() {
		return petColor;
	}

	public void setPetColor(PetColor petColor) {
		this.petColor = petColor;
	}

}
