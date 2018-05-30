package org.technicode.xmltv.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.technicode.xmltv.dao.ChannelRepository;
import org.technicode.xmltv.model.Channel;
import org.technicode.xmltv.model.Epg;
import org.technicode.xmltv.model.EpgProvider;

@Component("telemanService")
public class TelemanService {

	private static String API = "http://www.teleman.pl";
	
	@Autowired
	private ChannelRepository channelRepository;

	public List<Channel> getChannels() throws IOException {
		Document doc = Jsoup.connect(API).get();
		Element stationsIndex = doc.getElementById("stationsIndex");
		if(stationsIndex != null) {
			List<Channel> channels = new ArrayList<Channel>();
			Elements a = stationsIndex.getElementsByTag("a");
			for(Element channelBody : a) {
				String href = channelBody.attr("href");
				String name = channelBody.text();
				Channel channel = new Channel(name, href, EpgProvider.TELEMAN);
				channels.add(channel);
			}
			return channels;
		}
		return null;
	}
	
	public List<Epg> getEpg(int days, Channel channel) throws IOException, URISyntaxException {
		LocalDate date = LocalDate.now();
		List<Epg> channelEpg = new ArrayList<Epg>();
		LocalDate lastScan = null;
		for(int i=0;i<days;i++) {
			LocalDate epgDate = date.plusDays(i);
			if(channel.getLastScan() == null || (channel.getLastScan() != null && 
					epgDate.isAfter(channel.getLastScan()))) {
				List<Epg> dayEpg = getEpg(epgDate, channel);
				if(dayEpg != null) {
					channelEpg.addAll(dayEpg);
					lastScan = epgDate;
				} else {
					break;
				}
			}
		}
		if(lastScan != null) {
			channel.setLastScan(lastScan);
		}
		return channelEpg;
	}
	
	public List<Epg> getEpg(LocalDate date, Channel channel) throws IOException, URISyntaxException {
		List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
		urlParams.add(new BasicNameValuePair("hour", "-1"));
		urlParams.add(new BasicNameValuePair("date", date.toString()));
		System.out.println(urlParams.toString());
		Response resp = Jsoup.connect(createUrl(API + channel.getUrl(), urlParams)).method(Method.GET).execute();
		System.out.println("Status:" + resp.statusCode());
		if(resp.statusCode() != 200) {
			System.out.println("Status:" + resp.statusCode() + " in date: " + date.toString());
			return null;
		} else {
			Document doc = resp.parse();
			//Document doc = Jsoup.connect(createUrl(API + channel.getUrl(), urlParams)).get();
			Elements stationItems = doc.getElementsByClass("stationItems");
			if(stationItems != null && stationItems.size() > 0) {
				Elements li = stationItems.get(0).getElementsByTag("li");
				List<Epg> epgs = new ArrayList<Epg>();
				for(Element element : li) {
					if(element.className() != null && !element.className().equals("ad")) {
						Epg epg = new Epg();
						if(channel.getId() != null) {
							epg.setChannel(channel);
						}
						epg.setDay(date);
						Element start = getFirstElement(element.getElementsByTag("em"));
						if(start != null && start.text() != null) {
							epg.setStart(parseEmTime(start.text()));
						}
						Element detail = getFirstElement(element.getElementsByClass("detail"));
						if(detail != null) {
							Element name = getFirstElement(detail.getElementsByTag("a"));
							if(name!= null) {
								epg.setTitle(name.text());
								String url = name.attr("href");
								epg.setUrl(url);
							}
							Elements pList = detail.getElementsByTag("p");
							if(pList != null && pList.size() > 0) {
								for(Element p : pList) {
									Element genre = getFirstElement(p.getElementsByClass("genre"));
									if(genre != null) {
										epg.setGenre(genre.text());
									} else {
										epg.setDescr(p.text());
									}
								}
							}
						}
						epgs.add(epg);
					}
				}
				return epgs;
			} else {
				return null;
			}
		}
	}
	
	public void updateChannels() throws IOException {
		List<Channel> dbChannels = channelRepository.findByEpgProvider(EpgProvider.TELEMAN);
		Map<String, Channel> channelMap = getChannelMap(dbChannels);
		List<Channel> channels = getChannels();
		List<Channel> channelsToSave = new ArrayList<Channel>();
		for(Channel channel : channels) {
			if(channelMap.get(channel.getName()) == null) {
				channelsToSave.add(channel);
				//channelRepository.save(channel);
			} else{
				//TODO add support for changes in channel - link
			}
		}
		if(channelsToSave.size() > 0) {
			channelRepository.saveAll(channelsToSave);
		}
	}
	
	private Map<String, Channel> getChannelMap(List<Channel> channels){
		Map<String, Channel> channelMap = new HashMap<String, Channel>();
		for(Channel channel : channels) {
			channelMap.put(channel.getName(), channel);
		}
		return channelMap;
	}
	
	private Element getFirstElement(Elements elements) {
		if(elements != null && elements.size() > 0) {
			return elements.get(0);
		}
		return null;
	}
	
	private String createUrl(String url, List<NameValuePair> params) throws URISyntaxException{
		if(params != null && params.size() > 0){
			URIBuilder urlBuilder = new URIBuilder(url);
			urlBuilder.addParameters(params);
			return urlBuilder.build().toString();
		} else{
			return url;
		}
	}
	
	private LocalTime parseEmTime(String em) {
		if(em != null) {
			if(em.length() > 5) {
				//TODO add support for HBO date
			} else {
				return parseTime(em);
			}
		}
		return null;
	}
	
	private LocalTime parseTime(String em) {
		String[] timeParts = em.split(":");
		if(timeParts.length == 2) {
			int hour = Integer.valueOf(timeParts[0]);
			int minute = Integer.valueOf(timeParts[1]);
			LocalTime time = LocalTime.of(hour, minute);
			return time;
		}
		return null;
	}
	
}
