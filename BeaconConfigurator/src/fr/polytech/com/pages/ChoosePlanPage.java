package fr.polytech.com.pages;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Element;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChoosePlanPage extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	private static Map<String, String> plansMap;
	private JPanel jPanel;  
    private JComboBox<String> plansCombo;
    private JButton jButton;
    private JSONObject data;
	
	public ChoosePlanPage(String sallon) throws IOException {  
        super();
        initFrame(sallon);  
    }
	
	private void initFrame(String sallon) throws IOException {
		setTitle("Beacon Configurator");
		setVisible(true);
		setSize(400, 250);
		setResizable(false);
		setLocationRelativeTo(null);
		data=getData(sallon);
		plansMap=getPlanList(data);
		Point mousePoint=MouseInfo.getPointerInfo().getLocation();
        //setLocation(mousePoint.x+100,mousePoint.y);  
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        plansCombo=new JComboBox<String>();
        plansCombo.setEditable(false);
        
        for(Entry<String, String> eo : plansMap.entrySet()){
        	plansCombo.addItem(eo.getKey());
        }
        jPanel = new JPanel();  
        jPanel.setBorder((BorderFactory.createTitledBorder("Choisir un Plan")));
        
        jPanel.add(plansCombo);  
        add(jPanel, BorderLayout.CENTER);
          
        JPanel panel = new JPanel();  
        jButton = new JButton("OK");  
          
        jButton.addActionListener(new ActionListener() {  
			@Override  
            public void actionPerformed(ActionEvent e) {  
				String imagePath=plansMap.get(plansCombo.getSelectedItem().toString());
				BeaconDesignerPage beaconDesignerPage=new BeaconDesignerPage(imagePath,data);
                dispose();
            }  
        });  
  
        panel.add(jButton);  
        add(panel, BorderLayout.SOUTH);  
        JPanel panelN = new JPanel();  
        add(panelN, BorderLayout.NORTH);  
        JPanel panelW = new JPanel();  
        add(panelW, BorderLayout.WEST);  
        JPanel panelE = new JPanel();  
        add(panelE, BorderLayout.EAST);  
          
        // 给JPanel追加垂直滚动条  
        addScroll();  
        
	}
	
	 public void addScroll() {  
	        Container scrollPanel = getContentPane();  
	        JScrollPane jScrollPane = new JScrollPane(jPanel);    
	        scrollPanel.setPreferredSize(new Dimension(400, 280));  
	        scrollPanel.add(jScrollPane);  
	        scrollPanel.setVisible(true);  
	    } 
	
	private static JSONObject getData(String sallon) throws IOException{
		OkHttpClient client = new OkHttpClient();
		JSONObject dataObject = null;
		Request request = new Request.Builder()
		  .url("http://itineraire.polytech.univ-tours.fr/server/get.php?id="+sallon)
		  .get()
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "09148002-cd74-9ea3-6998-48db67c26ec0")
		  .build();

		Response response = client.newCall(request).execute();
		
		if(response.isSuccessful()) {
			dataObject=JSONObject.fromObject(response.body().string());
		}else {
			throw new IOException("Unexpected code " + response);
		}
		return dataObject;
	}
	 
	private static Map<String,String> getPlanList(JSONObject dataObject) throws IOException {
		plansMap=new HashMap<String,String>();
		JSONArray levelsArray=JSONArray.fromObject(dataObject.getJSONArray("levels"));
		for(int i = 0; i < levelsArray.size(); i++) {
			JSONObject levelObject=levelsArray.getJSONObject(i);
			plansMap.put(levelObject.getString("name"), levelObject.getString("background"));
		}
		return plansMap;
	}
	


}
