/**
 * @author Aikaterini Papadopoulou
 *
 */

import ithakimodem.*;
import java.io.*;
import java.util.*;


public class userApplication {
	
	
	public static void main(String[] args) throws IOException {
		//add session code to test ithaki modem
		//(new userApplication()).echo_request("E9634\r");
		//(new userApplication()).image_request("M9870\r");
		//(new userApplication()).image_request("M8652CAM=PTZ\r");
		//(new userApplication()).image_request("M8652CAM=PTZDIR=L\r");
		//(new userApplication()).image_request("G6052\r");    				//with error   
		//(new userApplication()).gps_request("P0733R=1003050\r");
		//(new userApplication()).gps_request_image("P0733T=225735403737T=225734403737T=225733403736T=225732403739T=225732403739\r");
		(new userApplication()).ack_request("Q0978\r" , "R7113\r");					//Ack Nack
	}
	
	
	
	public void echo_request(String code) throws IOException {
		
		Modem modem = new Modem();
		modem.setSpeed(80000);
		modem.setTimeout(2000);
		modem.open("ithaki");
		
		int modemRead = 0;
		int packetCounter = 0;
		int connectionTime = 240000;
		long start;
		long end;
		long duration;
		long current;
		
		FileOutputStream echo_packet = null;
		FileOutputStream echo_packet_duration = null;
		
		
		try {
			echo_packet = new FileOutputStream("echo_packet1.txt");
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		try {
			echo_packet_duration = new FileOutputStream("echo_packet2.txt");
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		modem.write("atd2310ithaki\r".getBytes());
		
		start = System.currentTimeMillis();
		end = System.currentTimeMillis();
		
		for(;;) {				
				try{
					modemRead=modem.read();
					if(modemRead==-1){
						break;
					}
					System.out.print((char)modemRead);
				}catch(Exception x){
					break;
				}		
		}
		
		while((end-start)<connectionTime) {
			current = System.currentTimeMillis();
			modem.write(code.getBytes());
			 
			for(;;){
				try{
					modemRead=modem.read();
					if(modemRead!=-1){
						echo_packet.write((Character.toString((char)modemRead)).getBytes());
					}
					else{
						echo_packet.write(("\t\t").getBytes());
						break;
					}
				}catch(Exception x){
					break;
				}
			}
			 
			end = System.currentTimeMillis();
			packetCounter += 1;
			
			duration=end-current;
			try {
				echo_packet.write(("Packet number: " + Integer.toString(packetCounter)).getBytes());
				echo_packet.write(("\t\t").getBytes());
				echo_packet.write(("Duration in milliseconds: " + Long.toString(duration)).getBytes());
				echo_packet.write(("\n").getBytes());
				echo_packet_duration.write((Long.toString(duration) + "\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
				
			System.out.println(duration);
		}
		
		try {
			echo_packet.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done");
		
		modem.close();
	}

	public void image_request(String code) {
		Modem modem = new Modem();
		modem.setSpeed(80000);
		modem.setTimeout(2000);
		modem.open("ithaki");
		
		int modemRead = 0;
		
		modem.write("atd2310ithaki\r".getBytes());
		
		for(;;){				
			try{
				modemRead=modem.read();
				if(modemRead==-1){
					break;
				}
				System.out.print((char)modemRead);
				
			}catch(Exception x){
				break;
			}		
		}
		
		modem.write(code.getBytes());
		
		try {
			@SuppressWarnings("resource")
			FileOutputStream image = new FileOutputStream("image_errorfree.jpeg");
			for(;;){
				try{
					modemRead=modem.read();
					if(modemRead!=-1) {
						image.write((byte)modemRead);	
					}
					else{
						System.out.println();
						break;
					}	
				}catch(Exception x){
					break;
				}	
			}
			
			image.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done");
		
		modem.close();
	}
	
	public void gps_request(String code) {
		Modem modem = new Modem();
		modem.setSpeed(80000);
		modem.setTimeout(2000);
		modem.open("ithaki");
		
		List<Character> gpsChars = new ArrayList<Character>();
		int modemRead = 0;
		
		FileOutputStream gps_code = null;
		
		
		try {
			gps_code = new FileOutputStream("gps_code1.txt");
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		modem.write("atd2310ithaki\r".getBytes());
		
		for(;;){				
			try{
				modemRead=modem.read();
				if(modemRead==-1){
					break;
				}
				System.out.print((char)modemRead);
				
			}catch(Exception x){
				break;
			}		
		}
		
		modem.write(code.getBytes());
		
		
		for(;;){
			try{
				modemRead=modem.read();
				gpsChars.add((char)modemRead);
				if(modemRead==-1){
					System.out.println("End of Transmission");
					break;
				}
				System.out.print((char)modemRead);	
				gps_code.write((Character.toString((char)modemRead)).getBytes());
			}catch(Exception x){
				break;
			}
				
		}
		
		
		System.out.println("Done");
		
		modem.close();
	}
	
	public void gps_request_image(String code) {
		Modem modem = new Modem();
		modem.setSpeed(80000);
		modem.setTimeout(2000);
		modem.open("ithaki");
		
		int modemRead = 0;
		
		modem.write("atd2310ithaki\r".getBytes());
		
		for(;;){				
			try{
				modemRead=modem.read();
				if(modemRead==-1){
					break;
				}
				System.out.print((char)modemRead);
				
			}catch(Exception x){
				break;
			}		
		}
		
		modem.write(code.getBytes());
		
		try {
			@SuppressWarnings("resource")
			FileOutputStream image = new FileOutputStream("gps_image.jpeg");
			for(;;){
				try{
					modemRead=modem.read();
					if(modemRead!=-1) {
						image.write((byte)modemRead);	
					}
					else{
						System.out.println();
						break;
					}	
				}catch(Exception x){
					break;
				}	
			}
			
			image.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done");
		
		modem.close();
	}
	
	public void ack_request(String ackCode , String nackCode) throws IOException {		
		
		Modem modem = new Modem();
		modem.setSpeed(80000);
		modem.setTimeout(2000);
		modem.open("ithaki");

		int modemRead = 0;
		int connectionTime = 11000;
		long start;
		long duration;
		long finish;
		long current = 0;
		long totalDuration = 0;
		int ackPackets = 0;
		int nackPackets = 0;

		FileOutputStream arq_packet = null;
		FileOutputStream arq_packet_duration = null;

		try {
			arq_packet = new FileOutputStream("arq_packet21.txt");
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		try {
			arq_packet_duration = new FileOutputStream("arq_packet_duration21.txt");
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}

		modem.write("atd2310ithaki\r".getBytes());
		
		for(;;) {
			try{
				modemRead=modem.read();
				if(modemRead==-1){
					break;
				}
				System.out.print((char)modemRead);
			}catch(Exception x){
				break;
			}
		}
		
		start = System.currentTimeMillis();
		current = System.currentTimeMillis();
		
		int xor = 0;
		int packetCounter = 0;
		String fcs_text = "";
		String packet = "";
		String message = "";
		int fcs = 0;
		String code = ackCode;
		
		while((System.currentTimeMillis()-start)<connectionTime) {
			modem.write(code.getBytes());
			try{
				modemRead=modem.read();
				if(modemRead==-1){
					break;
				}
				System.out.print((char)modemRead);
				arq_packet.write((Character.toString((char)modemRead)).getBytes());
				packet = packet + (char)modemRead;
				if(packet.endsWith("PSTOP")) {
					System.out.print("\n");
					arq_packet.write(("\n").getBytes());
					for(int i=31; i<47;i++) {
						message = message + packet.charAt(i);
						xor = xor ^ packet.charAt(i);
					}
					for (int i=49 ; i<52 ; i++) {
						fcs_text = fcs_text + packet.charAt(i);
					}
					fcs = Integer.parseInt(fcs_text);
					if (xor == fcs) {
						System.out.println("Correct Packet. Send next one");
						arq_packet.write(("Correct Packet. Send next one\n").getBytes());
						code = ackCode;
						finish = System.currentTimeMillis();
						duration = finish - current;
						packetCounter += 1;
						totalDuration += duration;
						
						System.out.println("Duration in milliseconds: " + duration + "\n");
						System.out.println("Packet Number: " + packetCounter + "\n");
						System.out.println("Total Duration in milliseconds: " + totalDuration + "\n");
						arq_packet.write(("Duration in milliseconds: " + Long.toString(duration) + "\n").getBytes());
						arq_packet.write(("Packet Number: " + Integer.toString(packetCounter) + "\n").getBytes());
						arq_packet.write(("Total Duration in milliseconds: " + Long.toString(totalDuration) + "\n\n").getBytes());
						arq_packet_duration.write((Long.toString(duration) + "\n").getBytes());
						current = System.currentTimeMillis();
						ackPackets += 1;
					}
					else {
						System.out.println("Incorrect Packet. Please resend\n");
						arq_packet.write(("Incorrect Packet. Please resend\n").getBytes());
						code = nackCode;
						nackPackets += 1;
					}
					
					
					packet = "";
					message = "";
					fcs_text = "";
					xor = 0;
					
				}
			}catch(Exception x){
				break;
			}
			
		}
		
		System.out.println("\nCorrect packets: " + ackPackets +"\n");
		System.out.println("\nIncorrect packets: " + nackPackets +"\n");
		arq_packet.write(("\nCorrect packets: " + Integer.toString(ackPackets) +"\n").getBytes());
		arq_packet.write(("\nIncorrect packets: " + Integer.toString(nackPackets) +"\n").getBytes());
		
		try {
			arq_packet.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			arq_packet_duration.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\nDone");
		modem.close();
		
		
	}
	
	
}