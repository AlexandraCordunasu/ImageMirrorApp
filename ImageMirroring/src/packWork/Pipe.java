package packWork;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Pipe extends AbstractClass {
    private final PipedOutputStream outputStream;
    private final PipedInputStream inputStream;
    private boolean producerFinished = false;
    private int segmentSize;
    
    public Pipe() {
        this.outputStream = new PipedOutputStream();
        this.inputStream = new PipedInputStream();
        
        try {
            this.inputStream.connect(this.outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //setez lungimea segmentului pe care il transmit la momentul respectiv catre consumer de la producer
    public void setSegmentSize(int segmentSize) throws InterruptedException
    {
    	//ma asigur ca pot seta lungimea altfel astept pana se poate seta
    	Static.canSetSegmentSize.acquire();
    	this.segmentSize = segmentSize;
    }
    //obtin lungimea segemntului
    public int getSegmentSize()
    {
    	return segmentSize;
    }

    //trimit informatiile catre consumer
    public void sendData(byte[] data) throws IOException, InterruptedException {
//    	System.out.println("{PIPE} Sent data is of size " + data.length);
         outputStream.write(data);
    }

    public byte[] receiveData() throws IOException, InterruptedException {
        byte[] buffer = new byte[segmentSize];
        int bytesRead = inputStream.read(buffer);
        
        System.out.println("{PIPE} Received " + bytesRead + " bytes");

        Static.canSetSegmentSize.release();
        return buffer;
    }
    
    public void setProducerFinished() {
        this.producerFinished = true;
    }

    public boolean isProducerFinished() {
        return this.producerFinished;
    }
}
