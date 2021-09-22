import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

public class DiscordBot extends WebSocketAdapter{
	static String channelID;
	static String token;
	
	public JsonObject getJsonObjectFromString(String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject obj = jsonReader.readObject();
		jsonReader.close();
		return obj;
	}
	
	public String getGatewayURL(String token) throws IOException {
		URL url = new URL("https://discord.com/api/gateway/bot");
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bot " + token);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("User-Agent", "");
		InputStream in = con.getInputStream();
		String dat = "";
		int c = in.read();
		while(c != -1) {
			dat += (char)c;
			c = in.read();
		}
		
		System.out.println(dat);
		in.close();
		con.disconnect();
		
		JsonObject obj = getJsonObjectFromString(dat);
		return obj.getString("url");
	}
	
	public void sendMessageToChannel(String message) throws IOException {
		String msg = "{\"content\":\""+message+"\"}";
		URL url = new URL("https://discord.com/api/channels/"+channelID+"/messages");
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Bot " + token);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("User-Agent", "");

		OutputStream out = con.getOutputStream();
		out.write(msg.getBytes());
		out.flush();
		out.close();
		
		con.getInputStream().close();
		con.disconnect();
	}
	
	public DiscordBot(String id, String tok) throws IOException {
		this.channelID = id;
		this.token = tok;
		String op2 = "{\"op\":2,\"d\":{\"token\":\""+token+"\",\"intents\":513,\"properties\":{\"$os\":\"windows\",\"$browser\":\"chrome\",\"$device\":\"chrome\"}}}";

		WebSocketFactory wsf = new WebSocketFactory();
		WebSocket ws = wsf.createSocket("wss://gateway.discord.gg");
		ws.addListener(this);
		try {
			ws.connect();
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
		
		sleep(500);
		ws.sendText(op2);
		sleep(60000);
		
		
		ws.disconnect();
	}
	
	private void messageSent(String message, String author) {
		if(message.equalsIgnoreCase("hello")) {
			try {
				sendMessageToChannel("Why, hello right back at you!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
		System.out.println("connected");
		try {
			sendMessageToChannel("Disbot in da hooooouuuuuuusssssseeee!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
		System.out.println("disconnected");
	}
	
	@Override
	public void onError(WebSocket websocket, WebSocketException cause) {
		System.out.println("OnError: " + cause);
	}
	
	@Override
	public void onFrame(WebSocket websocket, WebSocketFrame frame) {
		JsonObject obj = getJsonObjectFromString(frame.getPayloadText());
		System.out.println(obj);
		String type = obj.getString("t");
		if(type.equals("MESSAGE_CREATE")) {
			JsonObject d = obj.getJsonObject("d");
			String message = d.getString("content");
			JsonObject author = d.getJsonObject("author");
			String user = author.getString("username");
			messageSent(message, user);
		}
	}
	

	
	public static void main(String[] args) throws IOException {
		String channelID = "888097387409707021";
		String token = "";
		new DiscordBot(channelID, token);
	}
}
