package fr.polytech.com.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothService implements Runnable {
	private Boolean stopFlag = false;

	private LocalDevice local = null;


	private StreamConnection streamConnection = null;

	private byte[] acceptdByteArray = new byte[1024];

	private DataInputStream inputStream;
	private StreamConnectionNotifier notifier;
	
	private  final static ExecutorService service = Executors.newCachedThreadPool();
	

	public BluetoothService() {
		try {
			BluCatUtil.doctorDevice(); 					
			RemoteDeviceDiscovery.runDiscovery();		
			// System.out.println(RemoteDeviceDiscovery.getDevices());
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			local = LocalDevice.getLocalDevice();

			if (!local.setDiscoverable(DiscoveryAgent.GIAC))
				System.out.println("Please set bluetooth visable");

			/*Set<RemoteDevice> devicesDiscovered = RemoteDeviceDiscovery.getDevices();		 runDiscovery
			if (devicesDiscovered.iterator().hasNext()) {									
				RemoteDevice first = devicesDiscovered.iterator().next();
				streamConnection = (StreamConnection) Connector.open("btspp://" + first.getBluetoothAddress() + ":1");
			}*/
			/**
			 *
			 */
			String url = "btspp://localhost:" +  new UUID(80087355).toString() + ";name=RemoteBluetooth";  
            notifier = (StreamConnectionNotifier)Connector.open(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
		service.submit(this);
	}

	@Override
	public void run() {
		try {
			String inStr = null;
			streamConnection = notifier.acceptAndOpen();				//闃诲鐨勶紝绛夊緟璁惧杩炴帴,杩欓噷灏辨病鏈変綔涓柇澶勭悊浜嗭紝濡傛灉娌℃湁杩炴帴璇ョ嚎绋嬪氨鏃犳硶鍏抽棴
			inputStream = streamConnection.openDataInputStream();
			int length;
			while (true) {
				if ((inputStream.available()) <= 0) {					//涓嶉樆濉炵嚎绋�
					if (stopFlag)										//UI鍋滄鍚庯紝鍏抽棴
						break;
					Thread.sleep(800);									//鏁版嵁闂撮殧姣旇緝闀匡紝鎵嬪姩鍫靛绾跨▼
				} else {
					length = inputStream.read(acceptdByteArray);
					if(length>0) {
						inStr = new String(acceptdByteArray,0,length);
						System.out.println(inStr);
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (streamConnection != null)
					streamConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void stop() {
		stopFlag = true;
		service.shutdown();
	}
}
