package com.johnerdo.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ReadFilesForSetup {
	
	public static HashMap<String, String> buildHashMap(String fileName) throws FileNotFoundException{
		HashMap<String, String> map = new HashMap<String, String>();
		Scanner input = new Scanner(new File(fileName));
		while(input.hasNextLine()){
			String[] words = input.nextLine().split(":---:");
			map.put(words[0], words[1]);
		}
		input.close();
		return map;
	}
	
	public static Queue<String> buildLine(String fileName) throws FileNotFoundException{
		Queue<String> queue = new LinkedList<String> ();
		Scanner input = new Scanner(new File(fileName));
		while(input.hasNextLine()){
			queue.add(input.nextLine());
		}
		input.close();
		return queue;
	}
	
	public static void writeHashMap(String fileName, HashMap<String, String> map) throws IOException{
		FileOutputStream writer = new FileOutputStream(fileName);
		for(String key: map.keySet()){
			String value = key + ":---:"+ map.get(key) + "\n";
			writer.write(value.getBytes());
		}
		writer.close();
	}
	
	public static void writeLine(String fileName, Queue<String> line) throws IOException{
		FileOutputStream writer = new FileOutputStream(fileName);
		for(String value: line){
			writer.write((value + "\n").getBytes());
		}
		writer.close();
	}
	

}
