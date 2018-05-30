package org.technicode.xmltv.core;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.technicode.xmltv.dao.ChannelRepository;
import org.technicode.xmltv.dao.EpgRepository;
import org.technicode.xmltv.model.Channel;
import org.technicode.xmltv.model.Epg;
import org.technicode.xmltv.model.EpgProvider;
import org.technicode.xmltv.model.XmlTv;
import org.technicode.xmltv.model.XmlTvChannel;
import org.technicode.xmltv.model.XmlTvEntry;
import org.technicode.xmltv.model.XmlTvIcon;
import org.technicode.xmltv.model.XmlTvProgramme;

@Component("xmlTvService")
public class XmlTvService {
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private EpgRepository epgRepository;

	/*public void genXmlTv() throws JAXBException {
		XmlTv xmltv = new XmlTv();
		List<XmlTvEntry> entries = new ArrayList<XmlTvEntry>();
		
		XmlTvChannel channel = new XmlTvChannel();
		channel.setId("ideqweq");
		List<String> displayNames = new ArrayList<String>();
		displayNames.add("TVP1");
		displayNames.add("TVP 1");
		displayNames.add("TVP1 HD");
		channel.setDisplayNames(displayNames);
		channel.setIcon(new XmlTvIcon("http://teleman.tv/dasdas.jpg"));
		entries.add(channel);
		
		XmlTvProgramme programme = new XmlTvProgramme();
		entries.add(programme);
		
		xmltv.setEntries(entries);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlTv.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", 
				  "\n<!DOCTYPE tv SYSTEM  \"xmltv.dtd\">");
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.marshal(xmltv, System.out);
	}*/
	
	public void createXmlTv(EpgProvider provider, int days) throws JAXBException, IOException {
		List<Channel> channels = channelRepository.findByEpgProvider(provider);
		if(channels != null && channels.size() > 0) {
			XmlTv xmltv = new XmlTv();
			addChannels(channels, xmltv);
			addProgrammes(channels, xmltv, days);
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlTv.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", 
					  "\n<!DOCTYPE tv SYSTEM  \"xmltv.dtd\">");
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(xmltv, System.out);
			File file = new File("xmltv-" + days +".xml");
			file.createNewFile();
			jaxbMarshaller.marshal(xmltv, file);
		}
	}
	
	private void addChannels(List<Channel> channels, XmlTv xmltv) {
		for(Channel channel : channels) {
			addChannel(channel, xmltv);
		}
	}
	
	private void addProgrammes(List<Channel> channels, XmlTv xmltv, int days) {
		for(Channel channel : channels) {
			addProgramme(channel, xmltv, days);
		}
	}
	
	private void addProgramme(Channel channel, XmlTv xmltv, int days) {
		LocalDate today = LocalDate.now().minusDays(1);
		LocalDate lastDay = today.plusDays(days + 1);
		List<Epg> epgs = epgRepository.findByChannelAndDayIsAfterAndDayIsBefore(
				channel, today, lastDay);
		if(epgs != null && epgs.size() > 0) {
			XmlTvProgramme programme = null;
			for(Epg epg : epgs) {
				if(programme != null) {
					programme.setStop(epg.getStart().toString());
					xmltv.addEntry(programme);
				}
				programme = new XmlTvProgramme();
				programme.setChannel(channel.getCode());
				programme.setTitle(epg.getTitle());
				if(epg.getDescr() != null) {
					programme.setDescr(epg.getDescr());
				}
				if(epg.getGenre() != null) {
					programme.setCategory(epg.getGenre());
				}
				programme.setStart(epg.getStart().toString());
			}
			if(programme != null) {
				xmltv.addEntry(programme);
			}
		}
	}
	
	private void addChannel(Channel channel, XmlTv xmltv) {
		XmlTvChannel entry = new XmlTvChannel();
		entry.setId(channel.getCode());
		List<String> displayNames = new ArrayList<String>();
		displayNames.add(channel.getName());
		entry.setDisplayNames(displayNames);
		entry.setIcon("http://adasdasd.pl/asdasd.png");
		xmltv.addEntry(entry);
	}

}
