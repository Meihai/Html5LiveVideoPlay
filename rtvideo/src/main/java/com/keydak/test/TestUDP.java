package com.keydak.test;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.Test;

public class TestUDP {
    // 发送端
    @Test
    public void send() {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            // 创建一个数据报，每一个数据报不能大于64k，都记录着数据信息，发送端的IP、端口号，以及要发送到
            // 的接收端的IP、端口号。
            byte[] b = "我是发送端".getBytes();
            DatagramPacket pack = new DatagramPacket(b, 0, b.length,
                    InetAddress.getByName("192.168.1.101"), 9090);
                        ds.send(pack);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
    // 接收端
    @Test
    public void receive() {
          DatagramSocket ds = null;
          try {
              ds = new DatagramSocket(9090);
              byte[] b = new byte[1024];
              DatagramPacket pack = new DatagramPacket(b, 0, b.length);
              ds.receive(pack);
              String str = new String(pack.getData(), 0, pack.getLength());
              System.out.println(str);
            }catch (IOException e) {
                e.printStackTrace();
            }
          finally{
              if(ds != null){
                  ds.close();
              }
          }
    }
}