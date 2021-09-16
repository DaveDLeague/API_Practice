import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowMeDoggies {
	JFrame window = new JFrame();
	JFrame window2;
	JPanel panel = new JPanel();
	JButton button = new JButton("Show me DOGGIES!!");
	
	public ShowMeDoggies() {
		button.addActionListener((e)->{
			try {
				showRandomDog();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		panel.add(button);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
	public void showRandomDog() throws IOException {
		if(window2 != null) {
			window2.dispose();
		}
		window2 = new JFrame();
//		window2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window2.setVisible(true);
		JLabel label = new JLabel();
		URL url = new URL("https://dog.ceo/api/breeds/image/random");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream responseStream = connection.getInputStream();
		JsonReader jReader = Json.createReader(responseStream);
		JsonObject jObj = jReader.readObject();
		jReader.close();
		responseStream.close();
		
		String response = jObj.getString("message");
		System.out.println(response);
		label.setIcon(new ImageIcon(new URL(response)));
		window2.add(label);
		window2.pack();
	}
	
	public static void main(String[] args) {
		new ShowMeDoggies();
	}
}
