package com.johnerdo.bot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import com.johnerdo.commands.Commands;



public class DarcelBot extends PircBot {
	public Commands commands;
	String channel;
	
	public DarcelBot(String channel) throws IOException{
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
        this.sendMessage(channel, "Connected woot");
	}

    public static void main(String[] args) throws Exception {
    	DarcelBot bot = new DarcelBot("#emre801");
    }

    @Override
    protected void onConnect() {
        System.out.println("Connected!");
        joinChannel(channel);
        super.onConnect();
    }
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message){
    	if("!quit".equals(message)){
    		this.disconnect();
    		System.exit(1);
    	}
    	try {
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