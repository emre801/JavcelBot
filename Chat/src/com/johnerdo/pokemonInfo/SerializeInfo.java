package com.johnerdo.pokemonInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SerializeInfo {

	public static void serializeHash() {
		try {
			FileOutputStream fileOut = new FileOutputStream("PokeHash.txt");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Pokemon.pokeDataHash);
			out.close();
			fileOut.close();
			System.out.println(Pokemon.pokeDataHash.size());
			System.out.println("Serialized data is saved in BWAHAHAHA PokeHash.txt");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	public static void serializeTypeHash() {
		try {
			FileOutputStream fileOut = new FileOutputStream("Type.txt");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Pokemon.pokeTypeDataHash);
			out.close();
			fileOut.close();
			System.out.println(Pokemon.pokeDataHash.size());
			System.out.println("Serialized data is saved in BWAHAHAHA PokeHash.txt");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static void deserializeHash() {
		try {
			FileInputStream fileIn = new FileInputStream("PokeHash.txt");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Pokemon.pokeDataHash = (HashMap<Integer, String>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
			return;
		}
	}
	
	
	public static void deserializeTypeHash() {
		try {
			FileInputStream fileIn = new FileInputStream("Type.txt");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Pokemon.pokeTypeDataHash = (HashMap<Integer, String>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
			return;
		}
	}
}
