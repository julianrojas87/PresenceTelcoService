package org.telcomp.events;

import java.util.HashMap;
import java.util.Random;
import java.io.Serializable;

public final class EndPresenceTelcoServiceEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long id;
	private String userID;
	private String state;

	public EndPresenceTelcoServiceEvent(HashMap<String, ?> hashMap) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.userID = (String) hashMap.get("userID");
		this.state = (String) hashMap.get("state");
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof EndPresenceTelcoServiceEvent) && ((EndPresenceTelcoServiceEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String getUserID(){
		return this.userID;
	}
	
	public String getState(){
		return this.state;
	}
	
	public String toString() {
		return "endPresenceEvent[" + hashCode() + "]";
	}
}
