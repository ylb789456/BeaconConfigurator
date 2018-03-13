package fr.polytech.com.bluetooth;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;

import com.intel.bluetooth.RemoteDeviceHelper;

public class RemoteDeviceDiscovery {

	public final static Set<RemoteDevice> devicesDiscovered = new HashSet<RemoteDevice>();

	public static void runDiscovery() throws IOException, InterruptedException {
		findDevices();
	}

	private static void findDevices() throws IOException, InterruptedException {

		final Object inquiryCompletedEvent = new Object();

		devicesDiscovered.clear();

		DiscoveryListener listener = new DiscoveryListener() {
			public void inquiryCompleted(int discType) {
				System.out.println("#" + "Scan finished");
				synchronized (inquiryCompletedEvent) {
					inquiryCompletedEvent.notifyAll();
				}
			}

			@Override
			public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
				devicesDiscovered.add(remoteDevice);

				try {
					System.out.println("find device:" + remoteDevice.getFriendlyName(false));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			@Override
			public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
				System.out.println("#" + "servicesDiscovered");
			}

			@Override
			public void serviceSearchCompleted(int arg0, int arg1) {
				System.out.println("#" + "serviceSearchCompleted");
			}
		};

		synchronized (inquiryCompletedEvent) {

			LocalDevice ld = LocalDevice.getLocalDevice();

			System.out.println("#my name:" + ld.getFriendlyName());

			boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,listener);
			
			if (started) {
				System.out.println("#" + "wait for scanning end...");
				inquiryCompletedEvent.wait();
				LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(listener);
				System.out.println("#the number of devices finded:" + devicesDiscovered.size());
			}
		}

	}

	public static Set<RemoteDevice> getDevices() {

		return devicesDiscovered;
	}

	public static String deviceName(RemoteDevice d) {

		String address = d.getBluetoothAddress();

		String name = "";
		try {
			name = d.getFriendlyName(false);
		} catch (IOException e) {
			System.out.println("#Error: " + e.getMessage());
			try {
				name = d.getFriendlyName(false);
			} catch (IOException e2) {
				System.out.println("#Error: " + e2.getMessage());
			}

		}

		String rssi = "NA";

		String toret = "";

		if (BlucatState.csv)
			toret += (new Date()).getTime() + ", ";

		toret += BluCatUtil.clean(address) + ", " + "\"" + BluCatUtil.clean(name) + "\", " + "Trusted:"
				+ d.isTrustedDevice() + ", " + "Encrypted:" + d.isEncrypted();

		if (BlucatState.rssi) {
			try {
				rssi = String.valueOf(RemoteDeviceHelper.readRSSI(d));
			} catch (Throwable e) {

				String url = "btl2cap://" + d.getBluetoothAddress() + ":1";

				try {
					BlucatState.connection = Connector.open(url, Connector.READ_WRITE, true);
					rssi = String.valueOf(RemoteDeviceHelper.readRSSI(d));
					BlucatState.connection.close();

				} catch (IOException e1) {
				}
			}

			toret += ", " + rssi;

		}

		return toret;

	}

}
