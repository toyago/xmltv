package org.technicode.xmltv.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElements;

@XmlRootElement(name = "tv")
public class XmlTv {
	
	public XmlTv() {
		this.entries = new ArrayList<XmlTvEntry>();
	}
	
	List<XmlTvEntry> entries;
	
	@XmlElements({
	    @XmlElement(name="channel", type=XmlTvChannel.class),
	    @XmlElement(name="programme", type=XmlTvProgramme.class)
	})
	List<XmlTvEntry> getEmployees() {
	    return entries;
	}

	public void setEntries(List<XmlTvEntry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(XmlTvEntry entry) {
		this.entries.add(entry);
	}
	
}
