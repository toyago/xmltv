package org.technicode.xmltv.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.technicode.xmltv.model.Channel;
import org.technicode.xmltv.model.Epg;

public interface EpgRepository extends JpaRepository<Epg, Long>{
	
	public List<Epg> findByChannelAndDayIsAfterAndDayIsBefore(Channel channel, 
			LocalDate start, LocalDate end);

}
