package com.johnerdo.commands;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;

import javax.swing.text.BadLocationException;

import com.johnerdo.bot.DarcelBot;
import com.johnerdo.bot.MainGUI;
import com.johnerdo.pokemonInfo.Pokemon;

public class Commands {
	public HashMap<String, String> basicCommands, friendCodes, inGameNames;
	public Queue<String> line;
	
	public Commands() throws FileNotFoundException{
		basicCommands = ReadFilesForSetup.buildHashMap("basicCommands.txt");
		friendCodes = ReadFilesForSetup.buildHashMap("friendCodes.txt");
		inGameNames = ReadFilesForSetup.buildHashMap("inGameNames.txt");
		line = ReadFilesForSetup.buildLine("line.txt");
	}
	public void addCommand(String command, String output){
		basicCommands.put(command, command);
	}
	public void addFriendCode(String userName, String FC){
		friendCodes.put(userName, FC);
	}
	public void addIGN(String userName, String ign){
		inGameNames.put(userName, ign);
	}
	public String addPersonToLine(String person){
		if(line.contains(person)){
			return "@"+person + " you are already in line";
		}else{
			line.add(person);
			return "@"+person + " you have been added into the line";
		}
	}
	public String removePersonFromLine(String person){
		if(line.contains(person)){
			line.remove(person);
			return "@"+person + " you have been removed";
		}else{
			return "";
		}
	}
	
	public String retrieveCommand(String command){
		if(basicCommands.containsKey(command)){
			return basicCommands.get(command);
		}
		return null;
	}
	
	public String formatFriendCode(String fc){
		return fc.substring(0, 4) + "-" + fc.substring(4, 8) + "-" + fc.substring(8);
	}
	
	public String retrieveFriendCode(String username){
		if(friendCodes.containsKey(username)){
			return formatFriendCode(friendCodes.get(username));
		}
		return null;
	}
	
	public String retrieveInGameNames(String username){
		if(inGameNames.containsKey(username)){
			return inGameNames.get(username);
		}
		return null;
	}
	public String nextInLine() throws IOException{
		if(line.size() == 0){
			return "Battle line is empty";
		}
		String username = line.remove();
		String ign = this.retrieveInGameNames(username);
		String fc = this.retrieveFriendCode(username);
		if(ign == null){
			ign = "(Enter IGN please)";
		}
		if(fc == null){
			fc = "(Enter Friend Code, please)";
		}
		String responce = "@"+ username + ", you are the next for a batte, FC: " + fc + " IGN: "+ign ;
		ReadFilesForSetup.writeLine("line.txt", line);
		return responce;
	}
	
	public String FCInput(String[] words, String sender) throws IOException{
		if(words.length == 1){
			return this.retrieveFriendCode(sender);
		}else{
			String friendCode = words[1];
			if(friendCode.startsWith("@"))
				return this.retrieveFriendCode(friendCode.replaceAll("@", ""));
			
			friendCode = friendCode.replaceAll( "[^\\d]", "" );
			if(friendCode.length()==12){
				this.addFriendCode(sender, friendCode);
				ReadFilesForSetup.writeHashMap("friendCodes.txt", friendCodes);
				return "@" + sender + " your friend code has been saved";
			}else{
				return "@" + sender + " you have entered an incorrect friend code";
			}
		}
	}
	public String respondToIGN(String sender, String[] words) throws IOException{
		if(words.length == 1){
			return retrieveIGNWithSender(sender);
		}else{
			if(words[1].startsWith("@"))
				return retrieveIGNWithSender(words[1].replace("@", ""));
			this.addIGN(sender, words[1]);
			ReadFilesForSetup.writeHashMap("inGameNames.txt", inGameNames);
			return "@"+sender +"ign has been added";
		}
	}
	
	public String retrieveIGNWithSender(String sender){
		String ign = this.retrieveInGameNames(sender);
		if(ign == null)
			return "You have not entered an IGN";
		return "@"+sender+ " : " + ign;
	}
	
	public String executeMessage(String m, String sender) throws IOException{
		String[] words = m.split(" ");
		if(basicCommands.containsKey(words[0]) && words.length == 1 ){
			return basicCommands.get(words[0]);
		}else if("fc".equalsIgnoreCase(words[0])){
			return FCInput(words,sender);
		}
		else if("add".equalsIgnoreCase(words[0])){
			String response = this.addPersonToLine(sender);
			ReadFilesForSetup.writeLine("line.txt", line);
			return response;
		}
		else if("ign".equalsIgnoreCase(words[0])){
			return respondToIGN(sender,words);
		}
		else if("remove".equalsIgnoreCase(words[0])){
			String response = this.removePersonFromLine(sender);
			ReadFilesForSetup.writeLine("line.txt", line);
			return response;
		}else if("next".equalsIgnoreCase(words[0])){
			String response = this.nextInLine();
			return response;
		} else if("line".equalsIgnoreCase(words[0])){
			String response = "";
			int counter = 1;
			for(String value: line){
				response += counter  +".) " + value + " ";
			}
			return response;
		} else if("pk".equalsIgnoreCase(words[0])){
			String response = pokemonInfo(words[1]);
			return response;
		} 
		return null;
	}
	
	public String pokemonInfo(String pokeLookUp){
		Pokemon pokemon = new Pokemon(pokeLookUp);
		return pokemon.getName()+ " " + pokemon.getAttack()+ "/" + pokemon.getDefense()+ "/" + pokemon.getSpAttack()+ "/" + pokemon.getSpDefense()+ "/" + pokemon.getSpeed();
	}
	
	public void readMessage(String message, String sender, DarcelBot bot, String channel) throws IOException, BadLocationException{
		if(message.equals("null"))
			return;
		sender = sender.toLowerCase();
		String[] messages = message.split("!");
		for(String m:messages){
			String response = this.executeMessage(m,sender);
			if(response!=null){
				if(DarcelBot.chatBox != null){
					//DarcelBot.chatBox.append(response);
					MainGUI.appendToPane(DarcelBot.chatBox,response+"\n",DarcelBot.retrieveColor("darcelbot"));
				}
				bot.sendMessage(channel, response);
			}
		}
		
	}


}
