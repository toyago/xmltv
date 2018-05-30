package org.technicode.xmltv.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.technicode.xmltv.core.TelemanService;
import org.technicode.xmltv.core.XmlTvService;
import org.technicode.xmltv.dao.ChannelRepository;
import org.technicode.xmltv.dao.EpgRepository;
import org.technicode.xmltv.model.ActionType;
import org.technicode.xmltv.model.Channel;
import org.technicode.xmltv.model.Epg;
import org.technicode.xmltv.model.EpgProvider;


@Component
public class Main {
	
    public static void main( String[] args ){
    	ApplicationContext context = new AnnotationConfigApplicationContext("org.technicode.xmltv.conf");
    	Main app = context.getBean(Main.class);
    	app.start(args);
    }

    @Autowired
    private XmlTvService xmlTvService;
    
    @Autowired
    private TelemanService telemanService;
    
    @Autowired
    private EpgRepository epgRepository;
    
    @Autowired
    private ChannelRepository channelRepository;
    
    private void start(String[] args) {
    	
    	if(args.length > 0) {
    		ActionType actionType = ActionType.valueOf(args[0]);
    		switch(actionType) {
			case SCAN_EPG:
				if(args.length != 3) {
					System.out.println("Wrong arguments. Should be: 'SCAN_EPG PROVIDER DAYS'");
				} else {
					scanEpg(Integer.valueOf(args[2]), EpgProvider.valueOf(args[1]));
				}
				break;
			case UPDATE_CHANNELS:
				updateChannels(EpgProvider.valueOf(args[1]));
				break;
			case CREATE_XMLTV:
				createXmlTvFile(EpgProvider.valueOf(args[1]), Integer.valueOf(args[2]));
				break;
			case GEN_XMLTV:
				autoCreateXmlTv(EpgProvider.valueOf(args[1]), Integer.valueOf(args[2]));
				break;
			default:
				System.out.println("Arguments are missing");
				break;
    		}
    	} else {
    		System.out.println("Arguments are missing");
    	}
    }
    
    private void autoCreateXmlTv(EpgProvider epgProvider, int days) {
    	scanEpg(days, epgProvider);
    	for(int i=1;i<=days;i++) {
    		createXmlTvFile(epgProvider, i);
    	}
    }
    
    private void createXmlTvFile(EpgProvider epgProvider, int days) {
    	try {
			xmlTvService.createXmlTv(epgProvider, days);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void updateChannels(EpgProvider epgProvider) {
    	try {
			telemanService.updateChannels();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void scanEpg(int days, EpgProvider epgProvider) {
    	try {
	    	List<Channel> channels = channelRepository.findByEpgProvider(epgProvider);
	    	for(Channel channel : channels) {
	    		System.out.println(channel.getName());
				List<Epg> epgs = telemanService.getEpg(days, channel);
				epgRepository.saveAll(epgs);
				channelRepository.save(channel);
			}
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    }
}
