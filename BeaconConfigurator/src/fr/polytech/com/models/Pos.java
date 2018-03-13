package fr.polytech.com.models;

import java.io.Serializable;
import java.security.cert.X509CRLEntry;

public class Pos implements Serializable{
	private int x;
	private int y;
	
	public Pos(int x,int y) {
		this.x=x;
		this.y=y;
	}
	

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
//	@Override
//	public String toString() {
//		return "Pos [x=" + x + ", y=" + y + "]";
//	}
	
}
