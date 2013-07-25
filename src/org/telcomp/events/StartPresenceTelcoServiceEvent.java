package org.telcomp.events;

import java.util.HashMap;
import java.util.Random;
import java.io.Serializable;

public final class StartPresenceTelcoServiceEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long id;
	
	private String parameter;
	private String value;

	public StartPresenceTelcoServiceEvent(HashMap<String, ?> hashMap) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.parameter = (String) hashMap.get("parameter");
		this.value = (String) hashMap.get("value");
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof StartPresenceTelcoServiceEvent) && ((StartPresenceTelcoServiceEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String getParameter(){
		return this.parameter;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String toString() {
		return "startPresenceEvent[" + hashCode() + "]";
	}
}
