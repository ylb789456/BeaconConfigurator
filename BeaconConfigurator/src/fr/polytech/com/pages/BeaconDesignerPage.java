package fr.polytech.com.pages;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import fr.polytech.com.tools.CircleJButton;
import fr.polytech.com.tools.DnDAdapter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import fr.polytech.com.models.*;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.crypto.Data;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.omg.CORBA.PUBLIC_MEMBER;


public class BeaconDesignerPage extends JFrame{
	

	/**
	 * Status of mouse
	 */
	public static int mouseStatus=0;
	private static int locaX=0;
	private static int locaY=0;
	private static int count=-1;
	
	/**
	 * 构造方法的辅助字符串
	 */
	private int mouseLocaX=0;
	private int mouseLocaY=0;
	private static int length=0;
	/**
	 * If click nothing
	 */
	public static final int NO=0;
	
	/**
	 * 
	 */
	
	public static final int Beacon=1;
	private ImageIcon imageIcon;
	private Dimension frameSize;
	private JSONArray beaconJsonArray;
	private List<Beacon> beaconsList;
	
	public BeaconDesignerPage(String background,JSONObject data) {
		try {
			launch(background,data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void launch(String background,JSONObject data) throws Exception
	{
		beaconsList=new ArrayList<Beacon>();
		imageIcon = new ImageIcon();
		imageIcon.setImage(getImageBackground(background));
		setIconImage(imageIcon.getImage());
	    frameSize = new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
	    addImageByRepaint();
	    beaconJsonArray = getBeaconArray(data);
	    setBounds(locaX, locaY, imageIcon.getIconWidth(), imageIcon.getIconHeight());
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		MouseWork mouseWork=new MouseWork();
		addMouseListener(mouseWork);
		addMouseMotionListener(mouseWork);
		setTitle("Beacon Configurator");
		setLocationRelativeTo(null);
		JButton beaconSetBtn=new JButton();
		beaconSetBtn.setText("Beacon");
		beaconSetBtn.setSize(600,150);
		beaconSetBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mouseStatus=Beacon;
			}
		});
		JButton saveBtn=new JButton("Sauvegarder");
		saveBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				try {
					saveBeacons(data);
					postData(data);
					int ret = JOptionPane.showConfirmDialog(null, "Vous souhaitez configurer la balise Dans les autres étages?","Attention",JOptionPane.YES_NO_OPTION);
					if(ret==JOptionPane.YES_OPTION) {
						ChoosePlanPage choosePlanPage=new ChoosePlanPage(data.getString("id"));
						dispose();
					}else {
						System.exit(0);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton deleteBtn=new JButton("Supprimer les balises existantes");
		deleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				BeaconExistListPage beaconExistListPage=new BeaconExistListPage(beaconJsonArray);
			}
		});
		setLayout(new FlowLayout());
		add(beaconSetBtn);
		add(saveBtn);
		add(deleteBtn);
		setVisible(true);		
	}
	
	
	
	private class ImagePanel extends JPanel {
        Dimension d;
        Image image;

        public ImagePanel(Dimension d, Image image) {
            super();
            this.d = d;
            this.image = image;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, d.width, d.height, this);
            repaint();
        }
    }
	
	
	
	
	private void addImageByRepaint() {
        ImagePanel imagePanel = new ImagePanel(frameSize, imageIcon.getImage());
        setContentPane(imagePanel);
        setVisible(true);
    }
	
	private class MouseWork extends MouseAdapter
	{
		public synchronized void mouseClicked(MouseEvent e) 
		{
			DnDAdapter dnd=new DnDAdapter();
			switch (mouseStatus)
			{
			case Beacon:
				JButton beaconBtn = new CircleJButton();
				beaconBtn.setBackground(Color.BLUE);
				Beacon beacon=new Beacon();
				beaconBtn.setBounds(mouseLocaX,mouseLocaY,30,30);
				add(beaconBtn);
				beaconBtn.addMouseMotionListener(dnd);
				beaconBtn.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 1) { 
							ButtonActionBody(beacon, beaconBtn);
							//System.out.println(beaconsList);
						}
						
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				ButtonActionBody(beacon, beaconBtn);
				beaconsList.add(beacon);
				mouseStatus=NO;
				break;
			}
		}
		public void mouseMoved(MouseEvent e) 
		{
			mouseLocaX=get(e.getX());
			mouseLocaY=get(e.getY());
		}
		private int get(int x) 
		{
			return x;
		}
	}
	
	private void ButtonActionBody(Beacon beacon,JButton beaconBtn) {
		BeaconInfoPage beaconInfoPage=new BeaconInfoPage(BeaconDesignerPage.this,beacon);
		beaconInfoPage.setVisible(true);
		if(beaconInfoPage.isDeleteFlag()) {
			beaconsList.remove(beacon);
			remove(beaconBtn);
		}else {
			beacon.setPos(new Pos(beaconBtn.getLocation().x,beaconBtn.getLocation().y));
		}
	}
	
	private JSONArray getBeaconArray(JSONObject data) {
		JSONArray beaconsArray=JSONArray.fromObject(data.getJSONArray("beacons"));
		return beaconsArray;
	}
	
	private void saveBeacons(JSONObject data) throws IOException {
		
		beaconJsonArray.addAll(JSONArray.fromObject(beaconsList));
		data.put("beacons", beaconJsonArray);
	}
	
	private void postData(JSONObject data) throws IOException {
		OkHttpClient client = new OkHttpClient();
		System.out.println(data.toString());
		MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
		String bodyString="------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"data\"\r\n\r\n"
				+ data.toString()+"\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
		RequestBody body = RequestBody.create(mediaType, bodyString);
		Request request = new Request.Builder()
				  .url("http://itineraire.polytech.univ-tours.fr/server/update.php")
				  .post(body)
				  .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
				  .addHeader("cache-control", "no-cache")
				  .addHeader("postman-token", "ac7dae91-5657-074a-32ca-30c7a4a1c3b5")
				  .build();

		Response response = client.newCall(request).execute();
		if(response.isSuccessful()) {
			int ret = JOptionPane.showConfirmDialog(null, "      Réussi!","Attention",JOptionPane.DEFAULT_OPTION);
		}else {
			int ret = JOptionPane.showConfirmDialog(null, response.body().string(),"Attention",JOptionPane.DEFAULT_OPTION);
		}
	}
	
	private Image getImageBackground(String background) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Image image;
		Request request = new Request.Builder()
		  .url("http://itineraire.polytech.univ-tours.fr/"+background)
		  .get()
		  .addHeader("cache-control", "no-cache")
		  .addHeader("postman-token", "b46738c1-b280-a6bf-b779-63bf33c94a22")
		  .build();

		Response response = client.newCall(request).execute();
		if(response.isSuccessful()) {
			InputStream inputStream = response.body().byteStream();
			image=ImageIO.read(inputStream);
		}else {
			throw new IOException("Unexpected code " + response);
		}
		return image;
	}
	
}
