package packWork;

import java.io.IOException;

public class ImageProducer extends Thread {
    private final Pipe pipe;
    private final String filePath;

    //constructor 
    public ImageProducer(Pipe pipe, String filePath) {
        this.pipe = pipe;
        this.filePath = filePath;
    }

    @Override
    public void run() {
    	byte[] imageData = null;
    	//prima data ne citim imaginea  sub forma unui sir de bytes
		try {
			imageData = ImageUtils.readImageData(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//ne declaram lungimea primului segemnt pe care vrem sa l parcugerm(primele trei or sa aiba lungime egala, al 4-lea 
		//o sa reprezinte ce ramane din imagine
		int segmentSize = imageData.length / 4;
	    
		//eliberam productia
		Static.producerInit.release();
    	int segmentNo = 0;
    	int remainingDataSize=imageData.length;
    	int sentDataSize = 0;
    	
        while(segmentNo < 4){
        		//daca am ajuns la ultimul segment trebuie sa vedem cu cat am ramas din imagine ca sa o 
        		//luam pe toata odata sa nu pierdem bytes.
        		if(segmentNo == 3)
        		{
        			segmentSize = imageData.length - segmentSize * 3;
        		}
            	try {
					pipe.setSegmentSize(segmentSize);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
                byte[] segment = new byte[segmentSize];
                
                
                System.arraycopy(imageData, sentDataSize, segment, 0, segmentSize);
                // Blocam semaforul pentru a putea scrie date in pipe doar cand consumatorul nu citestes
                try {
					Static.mpty.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
                //trimitem datele catre consumator cu ajuotrul pipe ului
                synchronized(pipe){
	                try {
						pipe.sendData(segment);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
                }
                
                System.out.println("{PRODUCER}Segmentul " + (segmentNo + 1) + " a fost trimis.");
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                // Eliberam semaforul care blocheaza consumatorul din a citi date din pipe
                Static.full.release();
                
                segmentNo += 1;
                
                sentDataSize += segmentSize;
                remainingDataSize -= segmentSize;
//                System.out.println("{PRODUCER} SentDataSize " + sentDataSize + " | remainingDataSize" + remainingDataSize + " | segmentSize " + segmentSize);

        }

        
        System.out.println("Producatorul a terminat de produs");
    }
}
