package org.example;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import java.util.concurrent.TimeoutException;
import java.io.EOFException;

public class PacketCaptureDemo {
    public static void main(String[] args) {
        PcapHandle handle = null;

        try {
            // List all available network interfaces
            PcapNetworkInterface nif = Pcaps.findAllDevs().get(6); // Choose your desired interface
            System.out.println("Using interface: " + nif.getName());

            // Open the interface
            int snaplen = 65536; // Maximum packet size to capture
            int timeout = 10_000; // 10 seconds
            handle = nif.openLive(snaplen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeout);

            // Apply a filter (optional)
            // handle.setFilter("tcp", BpfProgram.BpfCompileMode.OPTIMIZE);

            System.out.println("Start capturing packets...");

            // Loop to capture multiple packets
            for (int i = 0; i < 10; i++) { // Adjust loop count as needed
                try {
                    Packet packet = handle.getNextPacketEx();
                    System.out.println("Captured packet: " + packet);
                } catch (TimeoutException e) {
                    System.out.println("Timeout while waiting for a packet.");
                } catch (EOFException e) {
                    System.out.println("End of capture stream: " + e.getMessage());
                }
            }

        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
    }
}