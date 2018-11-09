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

		// https://www.whoscored.com/StatisticsFeed/1/GetPlayerStatistics?category=summary&subcategory=all&statsAccumulationType=0&isCurrent=true&playerId=&teamIds=&matchId=&stageId=15404&tournamentOptions=5&sortBy=Rating&sortAscending=&age=&ageComparisonType=&appearances=&appearancesComparisonType=&field=Overall&nationality=&positionOptions=&timeOfTheGameEnd=&timeOfTheGameStart=&isMinApp=false&page=1&includeZeroValues=&numberOfPlayersToPick=10
        // https://www.whoscored.com/StatisticsFeed/1/GetPlayerStatistics?category=summary&subcategory=all&statsAccumulationType=0&isCurrent=true&playerId=&teamIds=&matchId=&stageId=15404&tournamentOptions=5&sortBy=Rating&sortAscending=&age=&ageComparisonType=&appearances=&appearancesComparisonType=&field=Overall&nationality=&positionOptions=&timeOfTheGameEnd=&timeOfTheGameStart=&isMinApp=true&page=1&includeZeroValues=&numberOfPlayersToPick=10000
			try {
				
				
				
				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpGet httpGet = new HttpGet(
						"https://www.whoscored.com/StatisticsFeed/1/GetPlayerStatistics?category=summary&subcategory=all&statsAccumulationType=0&isCurrent=true&playerId=&teamIds=&matchId=&stageId=15404&tournamentOptions=5&sortBy=Rating&sortAscending=&age=&ageComparisonType=&appearances=&appearancesComparisonType=&field=Overall&nationality=&positionOptions=&timeOfTheGameEnd=&timeOfTheGameStart=&isMinApp=false&page=1&includeZeroValues=&numberOfPlayersToPick=10000");
				// da cambiare in base al vostro cookie
				httpGet.setHeader("cookie", "_gat=1; _pdfps=%5B%5D; _ga=GA1.2.1951728936.1526926745; _gid=GA1.2.1868847928.1541748360; permutive-session=%7B%22session_id%22%3A%2226ef3a74-2828-478f-9d20-0745e2b52244%22%2C%22last_updated%22%3A%222018-11-09T07%3A25%3A59.739Z%22%7D; incap_ses_108_774904=eHu0cf5gLhBK/7g8v7N/AYY25VsAAAAAalEQ9Ua3+mbhICGz1xUYEQ==; vg=a3f549bb-72c1-4c32-9d61-fc582ae1807e; nav45977=94bd18d92ee5cb28ae348202009|2_261; fonce_current_user=1; euconsent=BOUPYefOUPYefABABAITBE-AAAAcd7_______9______9uz_Gv_r_f__33e8_39v_h_7_-___m_-33d4-_1vV11yPg1urfIr1NpjQ6OGsA; _psegs=%5B1920%2C1930%2C2126%2C2441%2C4848%2C3376%2C3377%2C1956%2C1907%2C2300%5D; cto_lwid=f3feee78-21bc-4d5e-be74-1b9ef32b6e9a; __gads=ID=f64a05400650b122:T=1526926747:S=ALNI_MZv_xhnvgTTxaQCACLHdCno6sTDZQ; permutive-id=4d6d8f08-2d77-4b01-8ba9-e1989d9d8525; visid_incap_774904=V5VnW7WJTM+DoGhkmDOlHJYNA1sAAAAAQUIPAAAAAAAxQT/TU5e7zcVnC4kJDOqJ");
				httpGet.setHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
				httpGet.setHeader("x-requested-with","XMLHttpRequest");
				httpGet.setHeader("referer", "https://www.whoscored.com/Regions/108/Tournaments/5/Seasons/6974/Stages/15404/PlayerStatistics/Italy-Serie-A-2017-2018");
				httpGet.setHeader("authority","www.whoscored.com");
				// da cambiare in base alla vostra richiesta
				// take the last key from browser cookie
				// Model-last-Mode: 0ef64nN1GnfGWHBk02sonUoFl6cL/4ycI28xmBJSTZ0=
				httpGet.setHeader("model-last-mode","0ef64nN1GnfGWHBk02sonUoFl6cL/4ycI28xmBJSTZ0=");

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
