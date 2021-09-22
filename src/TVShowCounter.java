import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TVShowCounter {
	JFrame window = new JFrame();
	JTextField jtf = new JTextField(40);
	JButton submit = new JButton("Submit");
	JLabel lab = new JLabel("Enter a show title:");
	int totalSeasons;
	int totalEpisodes;
	int[] episodes;
	
	public int getShowId(String show) throws IOException {
		URL url = new URL("https://api.tvmaze.com/singlesearch/shows?q=" + show);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream responseStream = connection.getInputStream();
		JsonReader jReader = Json.createReader(responseStream);
		JsonObject jObj = jReader.readObject();
		jReader.close();
		responseStream.close();
		System.out.println(jObj);
		return jObj.getInt("id");
	}
	
	public String getShowEpisodeData(String showTitle) throws IOException {
		int id = getShowId(showTitle);
		
		URL url = new URL("https://api.tvmaze.com/shows/"+id+"/seasons");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream responseStream = connection.getInputStream();
		JsonReader jReader = Json.createReader(responseStream);
		JsonArray jArray = jReader.readArray();
		totalSeasons = jArray.size();
		episodes = new int[totalSeasons];
		for(int i = 0; i < totalSeasons; i++) {
			JsonObject jobj = jArray.getJsonObject(i);
			episodes[i] = jobj.getInt("episodeOrder");
			totalEpisodes += episodes[i];
		}
		
		jReader.close();
		responseStream.close();
		
		String res = "Total Seasons: " + totalSeasons + "\nTotal Episodes: " + totalEpisodes + '\n';
		for(int i = 0; i < totalSeasons; i++) {
			res += "Season " + (i + 1) +": " + episodes[i] + " episodes\n";
		}
		
		return res;
	}
	
	public TVShowCounter() throws IOException {
		submit.addActionListener((e)->{
			String text = jtf.getText();
			if(text.length() > 0) {
				String res;
				try {
					res = getShowEpisodeData(text);
					JOptionPane.showMessageDialog(null, res, text, JOptionPane.DEFAULT_OPTION);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		window.setLayout(new FlowLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.add(lab);
		window.add(jtf);
		window.add(submit);
		window.pack();
	}
	public static void main(String[] args) throws IOException {
		new TVShowCounter();
	}
}
