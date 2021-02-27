package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.shared.Packet;
import com.topperbibb.hacktcnj2021.shared.PingPacket;
import com.topperbibb.hacktcnj2021.shared.PongPacket;

public class ClientEventListener {
    public void received(Packet p, Client client) {
        if (p instanceof PingPacket) {
            client.sendPacket(new PongPacket());
        }
    }
}
