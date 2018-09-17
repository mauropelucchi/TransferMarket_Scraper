package com.tabulaex.scraping.trasfermarket;

import java.io.FileWriter;

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

			Document doc = Jsoup.connect("https://www.transfermarkt.it/serie-a/transfers/wettbewerb/IT1/saison_id/2018")
					.userAgent("Firefox").timeout(60000).get();

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

						Element from = mybox.select(".vereinprofil_tooltip").get(1);
						String to = (element.select(".vereinprofil_tooltip").size() > 0)
								? element.select(".vereinprofil_tooltip").get(1).text()
								: "-";
						String value = element.select(".rechts").text();
						String market = element.select(".rechts.mw-transfer-cell").text();

						writer.append("\"Acquisto\";\"" + name + "\";\"" + role + "\";\"" + age + "\";"
								+ season.replace("€", "") + "\";\"" + nation + "\";\"" + from.text().replace("€", "")
								+ "\";\"" + to + "\";\"" + market.replace("€", "") + "\";\"" + value.replace("€", "")
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
								+ season.replace("€", "") + "\";\"" + nation + "\";\"" + from.text().replace("€", "")
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
