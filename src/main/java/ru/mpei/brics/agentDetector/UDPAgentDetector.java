package ru.mpei.brics.agentDetector;

import jade.core.AID;
import jade.core.Agent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;
import ru.mpei.brics.agent.BaseAgent;
import ru.mpei.brics.model.AIDWrapper;
import ru.mpei.brics.agentDetector.utils.PacketBuilder;
import ru.mpei.brics.agentDetector.utils.PcapHelper;
import ru.mpei.brics.agentDetector.interfaces.DetectorMethodsInterface;
import ru.mpei.brics.utils.JacksonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class UDPAgentDetector implements DetectorMethodsInterface {
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
    private ScheduledFuture<?> sendThread, discoverThread, deleteThread;
    private final PcapHelper pcapHelper;
    private final BaseAgent myAgent;
    private final String iFace;;
    private final int port;
    private final int period;
    private String data;
    private byte[] packet;
    private final Map<AID, Long> activeAgents = new ConcurrentHashMap<>();

    public UDPAgentDetector(BaseAgent a, String interfaceName, int period, int port) {
        this.myAgent = a;
        this.iFace = interfaceName;
        this.period = period;
        this.port = port;
        this.pcapHelper = new PcapHelper(this.iFace, this.period);
    }

    @Override
    public void startDiscovering() {
        if(this.discoverThread == null){
            log.info("{} start searching agents", myAgent.getLocalName());
            this.discoverThread = pcapHelper.startPacketsCapturing(this.port, new MyPacketListener(), ses);
        } else {
            log.info("Discover thread already started");
        }
    }

    @Override
    public void startSending() {

        if (this.sendThread == null){
            log.info("{} start sending packets", myAgent.getLocalName());
            if(this.data == null){
                AIDWrapper aidWrapper = new AIDWrapper(myAgent.getAID());
                this.data = JacksonHelper.toJackson(aidWrapper);
            }

            if(this.packet == null){
                this.packet = new PacketBuilder(this.iFace)
                        .addHeader(this.data)
                        .addUdpPart(this.port)
                        .addPayload();
            }

            this.sendThread = ses.scheduleAtFixedRate(() ->
                    this.pcapHelper.sendPacket(this.packet),
                    this.period, this.period,
                    TimeUnit.MILLISECONDS
            );
        } else {
            log.info("Sending thread already started");
        }
    }

    public void deadAgentRemoving() {
        if(this.deleteThread == null){
            this.deleteThread = ses.scheduleAtFixedRate(() -> {
                for (AID aid : this.activeAgents.keySet()) {
                    if (System.currentTimeMillis() - activeAgents.get(aid) > 3L * this.period) {
                        this.activeAgents.remove(aid);
                        log.warn("Agent {} loose agent {}", myAgent.getLocalName(), aid.getLocalName());
                    }
                }

                if (this.discoverThread.isDone()) {
                    terminate();
                    log.error("Agent {} was stopped due to an error in the detector", myAgent.getADetector());
                }

            }, 500, 500, TimeUnit.MILLISECONDS);
        } else {
            log.info("Removing thread already started");
        }
    }

    @Override
    public void stopSending() {
        if (this.sendThread != null) {
            this.sendThread.cancel(true);
            this.sendThread = null;
        }
        this.packet = null;
        this.data = null;
    }

    @Override
    public void stopDiscovering() {
        if (this.discoverThread != null) {
            this.discoverThread.cancel(true);
            this.discoverThread = null;
        }
        if (this.deleteThread != null) {
            this.deleteThread.cancel(true);
            this.deleteThread = null;
        }
    }

    @Override
    public List<AID> getActiveAgents() {
        return new ArrayList<>(activeAgents.keySet());
    }

    private String parsePacket(byte[] data){
        if (data.length < 14) return null;
        int offset = (iFace.equals("\\Device\\NPF_Loopback") ? 4 /*local*/ : 14 /*ethernet*/) + 20 /*ipv4*/ + 8 /* udp */;

        byte[] dataByte = new byte[data.length-offset];
        System.arraycopy(data,offset,dataByte,0,dataByte.length);
        return new String(dataByte);
    }

    public void terminate() {
        stopDiscovering();
        stopSending();
        myAgent.getCfg().getService().unsubscribe(myAgent.getCfg());
        myAgent.doDelete();
        log.warn("Agent {} stopped", myAgent.getLocalName());
    }

    @NoArgsConstructor
    private class MyPacketListener implements PacketListener {
        @Override
        public void gotPacket(Packet packet) {
            String strInfo = parsePacket(packet.getRawData());
            AID receivedAID = JacksonHelper.fromJackson(strInfo, AIDWrapper.class).createAid();
            if(!(receivedAID.equals(myAgent.getAID()))){
                boolean alreadyExist = activeAgents.containsKey(receivedAID);
                activeAgents.put(receivedAID, System.currentTimeMillis());
                if (!alreadyExist) log.warn("Agent {} find {}", myAgent.getLocalName(), receivedAID.getLocalName());;
            }
        }
    }
}
