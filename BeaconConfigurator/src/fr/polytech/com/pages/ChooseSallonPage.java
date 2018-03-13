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
import java.util.List;

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

public class ChooseSallonPage extends JFrame{
	private static final long serialVersionUID = 1L;
	private static List<String> idsList;
	private JPanel jPanel;  
    private JComboBox<String> idsCombo;
    private JButton jButton;
	
	public ChooseSallonPage() throws IOException {  
        super();  
        initFrame();  
    }
	
	private void initFrame() throws IOException {
		setTitle("Beacon Configurator");
		setVisible(true);
		setSize(400, 250);
		setLocationRelativeTo(null);
		setResizable(false);
		
		idsList=getIdListOfSallon();
		Point mousePoint=MouseInfo.getPointerInfo().getLocation();
        //setLocation(mousePoint.x+100,mousePoint.y);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        idsCombo=new JComboBox<String>();
        idsCombo.setEditable(false);
        
        for(String id:idsList) {
        	idsCombo.addItem(id);
        }
        jPanel = new JPanel();  
        jPanel.setBorder((BorderFactory.createTitledBorder("Choisir un Sallon")));
        
        jPanel.add(idsCombo);  
        add(jPanel, BorderLayout.CENTER);
          
        JPanel panel = new JPanel();  
        jButton = new JButton("OK");  
          
        jButton.addActionListener(new ActionListener() {  
              
            @Override  
            public void actionPerformed(ActionEvent e) {  
				try {
					ChoosePlanPage choosePlanPage=new ChoosePlanPage(idsCombo.getSelectedItem().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               
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
	
	private static List<String> getIdListOfSallon() throws IOException {
		idsList=new ArrayList<String>();
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("http://itineraire.polytech.univ-tours.fr/server")
		  .get()
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "09148002-cd74-9ea3-6998-48db67c26ec0")
		  .build();

		Response response = client.newCall(request).execute();
		if(response.isSuccessful()) {
			JSONArray jsonArray=JSONArray.fromObject(response.body().string());
			for (int i = 0; i < jsonArray.size(); i++) {    
		        idsList.add(jsonArray.getJSONObject(i).getString("id"));
		    }  
			System.out.println(idsList);
		}else {
			throw new IOException("Unexpected code " + response);
		}
		return idsList;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		getIdListOfSallon();
		
	}


}
