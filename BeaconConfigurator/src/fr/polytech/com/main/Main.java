package fr.polytech.com.main;

import java.awt.EventQueue;
import java.util.Locale;

import fr.polytech.com.pages.ChooseSallonPage;


public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					
					Locale.setDefault(Locale.FRANCE);  
					ChooseSallonPage chooseSallonPage=new ChooseSallonPage();
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
