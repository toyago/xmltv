package org.technicode.xmltv.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "channel")
public class XmlTvChannel extends XmlTvEntry{
	
	@XmlAttribute
	private String id;
	
	@XmlElement(name = "display-name")
	private List<String> displayNames;
	
	@XmlElement
	private XmlTvIcon icon;
	
	public void setIcon(String src) {
		this.icon = new XmlTvIcon();
		this.icon.setSrc(src);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDisplayNames(List<String> displayNames) {
		this.displayNames = displayNames;
	}

}
