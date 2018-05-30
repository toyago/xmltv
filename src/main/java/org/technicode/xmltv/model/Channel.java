package org.technicode.xmltv.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "CHANNEL")
public class Channel extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6646209020344014872L;
	
	public Channel(){
		
	}
	
	public Channel(String name, String url, EpgProvider epgProvider) {
		this.name = name;
		this.url = url;
		this.epgProvider = epgProvider;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "channel")
	private List<Epg> epg;
	
	@Enumerated(EnumType.STRING)
	private EpgProvider epgProvider;
	
	private String name;
	
	private String code;
	
	private String url;
	
	private LocalDate lastScan;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Epg> getEpg() {
		return epg;
	}

	public void setEpg(List<Epg> epg) {
		this.epg = epg;
	}

	public EpgProvider getEpgProvider() {
		return epgProvider;
	}

	public void setEpgProvider(EpgProvider epgProvider) {
		this.epgProvider = epgProvider;
	}

	public LocalDate getLastScan() {
		return lastScan;
	}

	public void setLastScan(LocalDate lastScan) {
		this.lastScan = lastScan;
	}

}
