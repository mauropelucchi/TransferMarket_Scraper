package com.tabulaex.scraping.trasfermarket;

import java.io.FileWriter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class TransferMarket_Scraper2 {
	public static void main(String[] args) {
		try {

			String filename = "TransferMarket_Data_SeriaA.csv";
			FileWriter writer = new FileWriter(filename);
			writer.append("type;name;role;age;season;nation;from;to;market_value;value");
			writer.append("\n");

			String html = "";

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(
					"https://www.transfermarkt.it/serie-a/transfers/wettbewerb/IT1/saison_id/2018");
			httpGet.setHeader("cookie",
					"TMSESSID=fglie2laqou6a7r5bp9ue39n86; _tmlpu=5; _ga=GA1.2.1244034228.1537278261; _gid=GA1.2.1779269776.1537278261; __gads=ID=5a6e4f289e879382:T=1537278261:S=ALNI_Mb6_t7RxaZpTOWatyx6PR47njX3Eg; POPUPCHECK=1537364670705; utag_main=v_id:0165ececb53c002034c0ab99d1f803079001d07100bd0$_sn:1$_ss:0$_st:1537280685950$ses_id:1537278260541%3Bexp-session$_pn:14%3Bexp-session$collectCookieMode:3rdParty%3Bexp-session$dc_visit_transfermarkt-transfermarkt.de:1$dc_event_transfermarkt-transfermarkt.de:14%3Bexp-session$dip_events_this_session:14%3Bexp-session$dc_visit_dip-main:1$dc_event_dip-main:14%3Bexp-session$dc_region_transfermarkt-transfermarkt.de:eu-central-1%3Bexp-session$dc_region_dip-main:eu-central-1%3Bexp-session");
			httpGet.setHeader("user-agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
			httpGet.setHeader("authority", "www.transfermarkt.it");

			CloseableHttpResponse response1 = httpclient.execute(httpGet);

			try {
				System.out.println(response1.getStatusLine());
				HttpEntity entity1 = response1.getEntity();
				html = EntityUtils.toString(entity1, "UTF-8");
			} finally {
				response1.close();
			}

			Document doc = Jsoup.parse(html);
			Elements box = doc.select("div.box");
			int c = 1;
			for (Element mybox : box) {

				if (mybox.select("div.responsive-table").size() > 1) {
					Elements elements = mybox.select("div.responsive-table").get(0).select("table > tbody > tr");
					for (Element element : elements) {

						String name = element.select(".spielprofil_tooltip").first().text();
						String role = element.select(".pos-transfer-cell").text();
						String age = element.select(".zentriert.alter-transfer-cell").text();
						String season = "18/19";
						String nation = (element.select(".zentriert.nat-transfer-cell img").size() > 0)
								? element.select(".zentriert.nat-transfer-cell img").first().attr("title")
								: "-";

						Element to = mybox.select(".vereinprofil_tooltip").get(1);
						String from = (element.select(".vereinprofil_tooltip").size() > 0)
								? element.select(".vereinprofil_tooltip").get(1).text()
								: "-";
						String value = element.select(".rechts").text();
						String market = element.select(".rechts.mw-transfer-cell").text();

						writer.append("\"Acquisto\";\"" + name + "\";\"" + role + "\";\"" + age + "\";"
								+ season.replace("€", "") + "\";\"" + nation + "\";\"" + from
								+ "\";\"" + to.text() + "\";\"" + market.replace("€", "") + "\";\"" + value.replace("€", "")
								+ "\"");
						writer.append("\n");

						System.out.println("name:" + name);
						System.out.println("role:" + role);
						c++;
						System.out.println("c:" + c);
					}

					elements = mybox.select("div.responsive-table").get(1).select("table > tbody > tr");
					for (Element element : elements) {

						String name = element.select(".spielprofil_tooltip").first().text();
						String role = element.select(".pos-transfer-cell").text();
						String age = element.select(".zentriert.alter-transfer-cell").text();
						String season = "18/19";
						String nation = (element.select(".zentriert.nat-transfer-cell img").size() > 0)
								? element.select(".zentriert.nat-transfer-cell img").first().attr("title")
								: "-";

						Element from = mybox.select(".vereinprofil_tooltip").get(1);
						String to = (element.select(".vereinprofil_tooltip").size() > 0)
								? element.select(".vereinprofil_tooltip").get(1).text()
								: "-";
						String value = element.select(".rechts").text();
						String market = element.select(".rechts.mw-transfer-cell").text();

						writer.append("\"Cessione\";\"" + name + "\";\"" + role + "\";\"" + age + "\";"
								+ season.replace("€", "") + "\";\"" + nation + "\";\"" + from.text()
								+ "\";\"" + to + "\";\"" + market.replace("€", "") + "\";\"" + value.replace("€", "")
								+ "\"");
						writer.append("\n");

						System.out.println("name:" + name);
						System.out.println("role:" + role);
						c++;
						System.out.println("c:" + c);
					}
				}
			}

			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
