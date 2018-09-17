package com.tabulaex.scraping.trasfermarket;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class WhoScored {
	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("whoscored");
		MongoCollection<org.bson.Document> collection = database.getCollection("players");

		

			try {
				
				
				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpGet httpGet = new HttpGet(
						"https://www.whoscored.com/StatisticsFeed/1/GetPlayerStatistics?category=summary&subcategory=all&statsAccumulationType=0&isCurrent=true&playerId=&teamIds=&matchId=&stageId=15404&tournamentOptions=5&sortBy=Rating&sortAscending=&age=&ageComparisonType=&appearances=&appearancesComparisonType=&field=Overall&nationality=&positionOptions=&timeOfTheGameEnd=&timeOfTheGameStart=&isMinApp=true&page=1&includeZeroValues=&numberOfPlayersToPick=10000");
				// da cambiare in base al vostro cookie
				httpGet.setHeader("cookie", "visid_incap_774904=tqLi/4EDSWmmDjU2L3v0Guj8n1sAAAAAQUIPAAAAAAD53VY+jPKKDceZeWnGhMPF; incap_ses_287_774904=n/unceP02QqjIA69K6L7A+j8n1sAAAAAQmSPYzQUGhaCnDApuWf4OA==; _ga=GA1.2.1205226309.1537211677; _gid=GA1.2.1156471876.1537211677; permutive-session=%7B%22session_id%22%3A%22cb558a16-ac67-4e74-ac86-b731382ca397%22%2C%22last_updated%22%3A%222018-09-17T19%3A14%3A37.184Z%22%7D; __ybotpvd=1; permutive-id=f71eeafe-4ff3-44c4-a7c1-8bdd8374dfbc; _pdfps=%5B%5D; __gads=ID=991cffff30ef6680:T=1537211632:S=ALNI_Mb3BPPUy6vfBfljFi7BWvbBWHesxA; fonce_current_user=1; nav45977=94bd2144c5d86e2d219f794bf09|2_261; _gat=1");
				httpGet.setHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
				httpGet.setHeader("x-requested-with","XMLHttpRequest");
				httpGet.setHeader("referer", "https://www.whoscored.com/Regions/108/Tournaments/5/Seasons/6974/Stages/15404/PlayerStatistics/Italy-Serie-A-2017-2018");
				httpGet.setHeader("authority","www.whoscored.com");
				// da cambiare in base alla vostra richiesta
				httpGet.setHeader("model-last-mode","zeAj2L/+d3zThawVVA4ilMitIneKhwuflKxAilSWd3g=");

				CloseableHttpResponse response1 = httpclient.execute(httpGet);

				try {
					System.out.println(response1.getStatusLine());
					HttpEntity entity1 = response1.getEntity();
					String responseString = EntityUtils.toString(entity1, "UTF-8");

					// parse JSON
					JsonObject obj = (new JsonParser()).parse(responseString).getAsJsonObject();
					JsonArray giocatori = obj.get("playerTableStats").getAsJsonArray();

					for (JsonElement giocatore : giocatori) {
						String _id = giocatore.getAsJsonObject().get("playerId").getAsString();
						String name = giocatore.getAsJsonObject().get("name").getAsString();
						Document checkDoc = new Document();
						System.out.println("_id " + _id + " --> " + name);
						checkDoc.append("_id", _id);
						if (collection.count(checkDoc) <= 0) {
							// potete creare voi l'oggetto document, oppure provare a usare quello esistente
							// Document myDoc = new Document();
							Document myDoc = Document.parse(giocatore.toString());
							myDoc.append("_id", _id);
							myDoc.append("name", name);
							collection.insertOne(myDoc);
						}
					}

					
				} finally {
					response1.close();
				}
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		mongoClient.close();
	}
}
