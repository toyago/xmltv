package org.technicode.xmltv.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "title")
public class XmlTvTitle {
	
	public XmlTvTitle() {
		
	}
	
	public XmlTvTitle(String value) {
		this.lang = "pl";
		this.value = value;
	}
	
	@XmlAttribute
	private String lang;
	
	@XmlValue
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
