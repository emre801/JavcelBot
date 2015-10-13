package com.johnerdo.commands;

import java.util.HashMap;

public class ImageIconURL {

	
	public static HashMap<String, String> imgToURLMapping = new HashMap<String, String>();
	public static void setupImgToUrlMapping(){
		imgToURLMapping.put("Kappa", "http://slangit.com/images/shortcuts/twitch/admins/kappa.png");
		imgToURLMapping.put("biblethump", "http://slangit.com/images/shortcuts/twitch/admins/biblethump.png");
		imgToURLMapping.put("frankerz", "http://slangit.com/images/shortcuts/twitch/admins/frankerz.png");
		//http://i.imgur.com/QXPWj7t.png
		imgToURLMapping.put("!----!", "http://i.imgur.com/QXPWj7t.png");
	}
	
}
