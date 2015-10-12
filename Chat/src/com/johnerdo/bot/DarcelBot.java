package com.johnerdo.bot;

import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JTextPane;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import com.johnerdo.commands.Commands;



public class DarcelBot extends PircBot {
	public Commands commands;
	String channel;
	public static JTextPane  chatBox;
	public static boolean readCommands = false;
	public DarcelBot(String channel, JTextPane  chatBox) throws IOException{
		this.chatBox = chatBox;
		this.channel = channel;
		this.commands = new Commands();
        this.setVerbose(true);

        this.setName("darcelbot");
        this.setLogin("darcelbot");

        try {
        	this.connect("irc.twitch.tv", 6667, "oauth:r4ykhpl985qi85oydfkt3z2xk95cj8"); // Not the key I'm using
        } catch (NickAlreadyInUseException e) {
            System.err.println("Nickname is currently in use");
        } catch (IrcException e) {
            System.err.println("Server did not accept connection");
            e.printStackTrace();
        }
        //this.sendMessage(channel, "Connected woot");
	}

    public static void main(String[] args) throws Exception {
    	DarcelBot bot = new DarcelBot("#emre801", null);
    }

    @Override
    protected void onConnect() {
        System.out.println("Connected!");
        joinChannel(channel);
        super.onConnect();
    }
    
    public static HashMap<String,Color> nameToColor = new HashMap<String,Color>();
    public static Color retrieveColor(String name){
    	if(!nameToColor.containsKey(name)){
    		Random rand = new Random();
    		float r = rand.nextFloat();
    		float g = rand.nextFloat();
    		float b = rand.nextFloat();
    		Color randomColor = new Color(r, g, b);
    		nameToColor.put(name, randomColor);
    	}
    	return nameToColor.get(name);
    }
    
    public static void writeChannelStuff(String sender, String message){
    	Calendar now = Calendar.getInstance();
    	MainGUI.appendToPane(DarcelBot.chatBox,now.get(Calendar.HOUR) +":" + now.get(Calendar.MINUTE)+ " ", Color.GRAY);
    	//MainGUI.appendToPane(DarcelBot.chatBox,sender +": ",retrieveColor(sender));
		MainGUI.appendToPane(DarcelBot.chatBox,sender,retrieveColor(sender));
		MainGUI.appendToPane(DarcelBot.chatBox,": " +message+"\n",Color.black);
    }
    
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message){
    	if(chatBox != null){
    		//chatBox.append("<"+sender + ">: " + message +"\n");
    		writeChannelStuff(sender,message);
            //System.out.println(chatBox.getLineCount());
            /*
            if(chatBox.getLineCount() > 50){
            	int end;
				try {
					end = chatBox.getLineEndOffset(0);
					chatBox.replaceRange("", 0, end);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
            	
            //}
    	}
    	if("!quit".equals(message)){
    		this.disconnect();
    		System.exit(1);
    	}
    	try {
    		if(readCommands)
    			commands.readMessage(message, sender, this, channel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

   /* @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        System.out.println(login + " joined channel " + channel);
        super.onJoin(channel, sender, login, hostname);
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        for (User user : users) {
            System.out.println(user);
        }
        super.onUserList(channel, users);
    }*/
}