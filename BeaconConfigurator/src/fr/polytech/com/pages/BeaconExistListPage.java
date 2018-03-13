package fr.polytech.com.pages;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.util.Formatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fr.polytech.com.models.Beacon;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class BeaconExistListPage extends JFrame{
	private JComboBox<String> beaconsCombo;
	private JPanel jPanel;
	
	public BeaconExistListPage(JSONArray beaconJsonArray) {  
        super();  
        initFrame(beaconJsonArray);  
    }
	
	private void initFrame(JSONArray beaconJsonArray) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
        setVisible(true);
        setResizable(false); 
        setTitle("Supprimer les balises");  
        setSize(400, 250);
        setLocationRelativeTo(null);
        
        beaconsCombo=new JComboBox<String>();
        beaconsCombo.setEditable(false);
        setBeaconsCombo(beaconJsonArray);
        jPanel = new JPanel();  
        jPanel.add(beaconsCombo);  
        add(jPanel, BorderLayout.CENTER);
          
        JPanel panel = new JPanel();  
        JButton deleteBtn=new JButton("Supprimer");
        deleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				beaconJsonArray.remove(beaconsCombo.getSelectedIndex());
				setBeaconsCombo(beaconJsonArray);
			}
		});
        JButton quitBtn = new JButton("Quitter");
        quitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
        panel.add(deleteBtn);
        panel.add(quitBtn);
        
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
	
	private void setBeaconsCombo(JSONArray beaconJsonArray) {
		beaconsCombo.removeAllItems();
		if(beaconJsonArray.size()==0) {
        	beaconsCombo.getEditor().setItem("null");
        }else {
        	for(int i=0;i<beaconJsonArray.size();i++) {
            	JSONObject beacon=JSONObject.fromObject(beaconJsonArray.get(i)) ;
            	beaconsCombo.addItem(beacon.getString("uid"));
            }
        }
	}
	
	public void addScroll() {  
        Container scrollPanel = getContentPane();  
        JScrollPane jScrollPane = new JScrollPane(jPanel);    
        scrollPanel.setPreferredSize(new Dimension(400, 280));  
        scrollPanel.add(jScrollPane);  
        scrollPanel.setVisible(true);  
    }  

}
