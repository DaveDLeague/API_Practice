import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordBot{
	public DiscordBot() throws IOException {
		String id = "888098066798895125";
		String token = "ODg4MDk4MDY2Nzk4ODk1MTI1.YUNvxw.l_VnGKrsktdRaoGwSpuZhsTxd7s";
		String secret = "qwE9IO_SDxcSDtJHQZijET-ldgMD8uYp";
		String websocketURL = "wss://gateway.discord.gg";
		
		URL url = new URL("https://discordapp.com/api/gateway");
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("User-Agent", "");
		InputStream responseStream = connection.getInputStream();
		String stream = "";
		int c = responseStream.read();
		while(c != -1) {
			stream += (char)c;	
			c = responseStream.read();
		}
		responseStream.close();
		
		System.out.println(stream);
	}
	
	public static void main(String[] args) throws IOException {
		new DiscordBot();
	}

}
