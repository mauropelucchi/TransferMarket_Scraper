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
public class TransferMarket_Scraper 
{
	 public static void main(String[] args) {
	        try {
	            
	            String filename ="TransferMarket_Data.csv";
	            FileWriter writer = new FileWriter(filename);
	            writer.append("name;role;age;season;nation;from;from_l;to;to_l;value");
	            writer.append("\n");
	            
	            int c = 0;
	            int page = 0;
	            
	            boolean ok = true;
	            while(ok) {
	                Document doc = Jsoup
	                        .connect("https://www.transfermarkt.it/serie-a/transferrekorde/wettbewerb/IT1/saison_id/alle/land_id/75/ausrichtung//spielerposition_id/alle/altersklasse/alle/leihe//w_s//zuab/0/plus/1/galerie/0/page/" + page + "?ajax=yw1")
	                        .userAgent("Opera").timeout(3000).get();

	                Elements elements = doc.select("table.items > tbody > tr");
	                for (Element element : elements) {
	                    
	                    Element name = element.select(".spielprofil_tooltip").first();
	                    Element role = element.select("td:nth-child(2) > table.inline-table > tbody > tr:nth-child(2) > td").first();
	                    Element age = element.select("td:nth-child(3)").first();
	                    Element season = element.select("td:nth-child(4)").first();
	                    String nation = element.select(".flaggenrahmen").attr("title");
	                    Element from = element.select("td:nth-child(6) > table > tbody > tr:nth-child(1) > td.hauptlink > a").first();
	                    Element from_l = element.select("td:nth-child(6) > table > tbody > tr:nth-child(2) > td > a").first();
	                    Element to = element.select(" td:nth-child(7) > table > tbody > tr:nth-child(1) > td.hauptlink > a").first();
	                    Element to_l = element.select("td:nth-child(7) > table > tbody > tr:nth-child(2) > td > a").first();
	                    Element value = element.select(".rechts").first();
	                    
	                    if(to_l == null) {
	                    	to_l = element.select("td:nth-child(7) > table > tbody > tr:nth-child(2) > td > img").first();
	                    }
	                    if(from_l == null) {
	                    	from_l = element.select("td:nth-child(6) > table > tbody > tr:nth-child(2) > td > img").first();
	                    }
	                    
	                    writer.append("\"" + name.text() + "\";\""  + role.text() + "\";\"" + age.text() + "\";"+ season.text().replace("€","") + "\";\"" + nation + "\";\"" + from.text().replace("€","") + "\";\"" + from_l.text().replace("€","") + "\";\"" + to.text().replace("€","") + "\";\"" + to_l.text().replace("€","") + "\";\"" + value.text().replace("€","") + "\"");
	                    writer.append("\n");
	                    
	                    
	                    System.out.println("name:" + name.text());
	                    System.out.println("role:" + role.text());
	                    c++;
	                    System.out.println("c:" + c);
	                }
	            
	                if(elements.size() < 1) {
	                	ok = false;
	                }
	                page++;
	            }
	            
	            writer.close();
	            
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
}
