package org.technicode.xmltv.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "programme")
public class XmlTvProgramme extends XmlTvEntry{
	
	@XmlAttribute
	private String start;
	
	@XmlAttribute
	private String stop;
	
	@XmlAttribute
	private String channel;
	
	@XmlElement(name = "title")
	private XmlTvTitle title;
	
	@XmlElement(name = "sub-title")
	private XmlTvSubTitle subTitle;
	
	@XmlElement(name = "desc")
	private XmlTvDescr descr;
	
	@XmlElement(name = "category")
	private XmlTvCategory category;

	public void setStart(String start) {
		this.start = start;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setTitle(String title) {
		this.title = new XmlTvTitle(title);
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = new XmlTvSubTitle(subTitle);
	}

	public void setDescr(String descr) {
		this.descr = new XmlTvDescr(descr);
	}

	public void setCategory(String category) {
		this.category = new XmlTvCategory(category);
	}
	
	
	

}
