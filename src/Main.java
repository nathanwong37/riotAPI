import java.net.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Main {
	static final String SUMMONER_NAME = "ShootYourShots";
	static final String API_KEY = "RGAPI-72a22c1e-0b64-469b-8429-59286cb961ce";
	static final String ACCOUNT_URL = "https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + SUMMONER_NAME
			+ "?api_key=" + API_KEY;
	static final String GAME_URL = "https://na1.api.riotgames.com/lol/spectator/v4/active-games/by-summoner/";
	static long gameID = 0;

	public static void main(String args[]) {
		String ID = getAccountId();
		boolean playing = inGame(ID);
		if (playing) {
			sendText();
			System.out.println("Success");
		}
	}

//Method calls an api, and gets accountID for the specific username
	private static String getAccountId() {
		try {
			URL url = new URL(ACCOUNT_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			InputStream response = con.getInputStream();
			int status = con.getResponseCode();
			// success
			if (status == 200) {
				JSONParser parse = new JSONParser();
				JSONObject obj = (JSONObject) parse.parse(new InputStreamReader(response, "UTF-8"));
				String accountId = (String) obj.get("id");
				return accountId;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Failure");
			return "";
		}
	}

//MEthod takes User ID calls api
	private static boolean inGame(String ID) {
		try {
			URL url = new URL(GAME_URL + ID + "?api_key=" + API_KEY);
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("GET");
			connect.connect();
			int status = connect.getResponseCode();
			// sucess
			if (status == 200) {
				JSONParser parse = new JSONParser();
				JSONObject obj = (JSONObject) parse.parse(new InputStreamReader(connect.getInputStream(), "UTF-8"));
				if (gameID != (long) obj.get("gameId")) {
					// New Game
					gameID = (long) obj.get("gameId");
					return true;
				}
				return false;
			} else {
				throw new Exception();
			}

		} catch (Exception e) {
			return false;
		}
	}

	private static boolean sendText() {
		// String to = "2064848118@vtext.com";
		String to = "4252406966@tmomail.net";
		String from = "nathanwongspam34@gmail.com";
		String host = "smtp.gmail.com";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		SmtpAuthenticator authentication = new SmtpAuthenticator();
		//TBh no clue what the bottom line does
		try {
			javax.mail.Message message = new MimeMessage(Session.getInstance(properties, authentication));
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("League Game");
			message.setText("Your Son is in a Leauge of Legends Game Right Now.");

			Transport.send(message);
			return true;

		} catch (Exception mex) {
			System.out.println(mex);
			return false;
		}
	}
}