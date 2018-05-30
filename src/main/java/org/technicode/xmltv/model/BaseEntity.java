package org.technicode.xmltv.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 966029222707924589L;

	@Id
	@GeneratedValue
	protected Long id;
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public boolean isNew() {
		return (this.getId() == null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj != null) && (obj instanceof BaseEntity) && this.getId() != null) {
			return (this.getId().equals(((BaseEntity)obj).getId()));			
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

	@Override
	public String toString() {
		return "BaseEntity [id=" + id + "]";
	}

}
