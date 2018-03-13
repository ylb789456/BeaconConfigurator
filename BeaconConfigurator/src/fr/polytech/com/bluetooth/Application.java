package fr.polytech.com.bluetooth;



public class Application {
	
	
	public static void main(String[] args) {
		
	
		BluetoothService bluetoothService = new BluetoothService();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bluetoothService.stop();//
	}
	
}
