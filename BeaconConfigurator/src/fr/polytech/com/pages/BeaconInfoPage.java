package fr.polytech.com.pages;

import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.util.Formatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fr.polytech.com.models.Beacon;



public class BeaconInfoPage extends JDialog{
	private boolean deleteFlag=false;
	
	public BeaconInfoPage(Frame parent,Beacon beacon) {  
        super(parent,true);
        
        initFrame(beacon);  
    }
	
	private void initFrame(Beacon beacon) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  
        //setVisible(true);
        setTitle("Beacon");  
        setSize(400, 250);
        setResizable(false); 
        Point mousePoint=MouseInfo.getPointerInfo().getLocation();
        setLocation(mousePoint.x+100,mousePoint.y);
        setLayout(null);
        
        JLabel uuidLabel=new JLabel("uuid:");
        uuidLabel.setBounds(50,20,300,20);
        add(uuidLabel);
        
        
        JTextField uuidSallonJTF=new JTextField();
        //want format the input like "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        uuidSallonJTF.setBounds(50,50,300,20);
        if(beacon.getUid()!=null) {
        	uuidSallonJTF.setText(beacon.getUid());
        }
        add(uuidSallonJTF);
        
        JButton saveBtn=new JButton("Sauvegarder");
        saveBtn.setBounds(60,100,130,40);
        add(saveBtn);
        saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(uuidSallonJTF.getText().isEmpty())
				{
					int ret = JOptionPane.showConfirmDialog(null, "Ne peut pas être vides","Attention",JOptionPane.DEFAULT_OPTION);
				}
				else {
					beacon.setUid(uuidSallonJTF.getText());
					dispose();
				}
			}
		});
        
        JButton deleteBtn=new JButton("Supprimer");
        deleteBtn.setBounds(210,100,130,40);
        add(deleteBtn);
        deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int ret = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de le supprimer?","Attention",JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.YES_OPTION) {
					deleteFlag=true;
					setVisible(false);
				}else {
					
				}
			}
		});
        
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
