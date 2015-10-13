package com.johnerdo.bot;

import java.io.IOException;
import java.util.HashSet;

import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pokejava.ModelClass;

public class GetInfo extends ModelClass {
	
	public static HashSet<String> moderators = new HashSet<String>();
	public static HashSet<String> viewers = new HashSet<String>();
	public void getInfo(String channel){
		 try {
			channel = channel.replace("#", "");
			String data = Request.Get("http://tmi.twitch.tv/group/user/"+channel+"/chatters").execute().returnContent().asString();
			JSONObject root = parse(data);
			JSONObject object = root.getJSONObject("chatters");
			JSONArray modArray = object.getJSONArray("moderators");
			
			for(int i =0;i<modArray.length();i++){
				String value = (String)modArray.get(i);
				System.out.println("Moderator" + value);
				moderators.add(value.toLowerCase());
			}
			
			JSONArray viewerArray = object.getJSONArray("viewers");
			
			for(int i =0;i<viewerArray.length();i++){
				String value = (String)viewerArray.get(i);
				viewers.add(value.toLowerCase());
			}
			System.out.println(viewers.size());
			
			
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
