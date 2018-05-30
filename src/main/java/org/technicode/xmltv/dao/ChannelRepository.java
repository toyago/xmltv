package org.technicode.xmltv.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.technicode.xmltv.model.Channel;
import org.technicode.xmltv.model.EpgProvider;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
	
	List<Channel> findByEpgProvider(EpgProvider provider);

}
