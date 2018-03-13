package fr.polytech.com.models;

import java.io.Serializable;

import javax.swing.text.Position;

public class Beacon implements Serializable{
	private String uuid;
	private String uid;
	private Pos pos;
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Pos getPos() {
		return pos;
	}
	public void setPos(Pos pos) {
		this.pos = pos;
	}
	
//	@Override
//	public String toString() {
//		return "Beacon [uuid=" + uuid + ", uid=" + uid + ", pos=" + pos + "]";
//	}
	
	
}
