package org.technicode.xmltv.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "icon")
public class XmlTvIcon {
	
	public XmlTvIcon() {
		
	}
	
	public XmlTvIcon(String src) {
		this.src = src;
	}
	
	@XmlAttribute
	private String src;

	public void setSrc(String src) {
		this.src = src;
	}

}
