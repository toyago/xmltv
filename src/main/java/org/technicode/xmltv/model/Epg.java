package org.technicode.xmltv.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EPG")
public class Epg extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3315350914781622866L;
	
	@ManyToOne(targetEntity = org.technicode.xmltv.model.Channel.class)
	@JoinColumn(name = "channelId")
	private Channel channel;
	
	private LocalDate day;
	
	private LocalTime start;
	
	//private LocalTime end;
	
	private String title;
	
	private String descr;
	
	private String genre;
	
	private String url;

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	/*public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}*/

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	

}
