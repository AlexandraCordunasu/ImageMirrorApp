package packWork;

import java.io.IOException;

public class ImageConsumer extends Thread {
    private final Pipe inputPipe;
    private final Pipe outputPipe;
    
    static {
        // Bloc de initializare static pentru ImageConsumer
        System.out.println("Bloc de initializare static în ImageConsumer");
    }

    {
        // Bloc de initializare non-static pentru ImageConsumer
        System.out.println("Bloc de initializare non-static în ImageConsumer");
    }

    //constructor cu ajutorul caruia instantiez pipeurile de intrare si de iesire
    //pipe ul de intrare e cel cu care facem transmiterea de la producator la consumator
    //outputPipe e pipe ul cu care facem transmiterea de la consumator la scriere
    public ImageConsumer(Pipe inputPipe, Pipe outputPipe) {
        this.inputPipe = inputPipe;
        this.outputPipe = outputPipe;
    }

    @Override
    public void run() {
    	//Prima data astept ca producatorul sa si termene initializarea
    	try {
			Static.producerInit.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	//obtinem dimensiunea segemntului de la pipe ul de intrare
    	int segmentSize = inputPipe.getSegmentSize();
    	
    	//deblocam producatorul pentru a se ocupa si de celelalte segemnte de imagine.
    	Static.producerInit.release();
    	
    	//declaram variabila pentru rezultat
    	byte[] result = new byte[0];
    	
    	//variabila cu care numaram segmentele in care am impartit imaginea.
    	int segmentNo = 0;
    	while(true)
    	{
    		
    		try {
    			
				Static.full.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
    		synchronized(inputPipe)
    		{
    			try {
    				//primim un segment de date de la producator.
					byte[] segment = inputPipe.receiveData();
					
					//instantiem o noua variabila in care  vrem sa retinem rezultatul.
					
					byte[] new_result = new byte[result.length + segment.length]; 
					
					
					System.arraycopy(result, 0, new_result, 0, result.length);
					
	                System.arraycopy(segment, 0, new_result, result.length, segment.length);
	                
//	                System.out.println("{CONSUMER} Received segment of size " + segment.length  + "\n");
	                System.out.println("{CONSUMER} Received segment: " + (segmentNo +1 ) + "\n" );
	                try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	                result = new_result;
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
    		}
    		
    		Static.mpty.release();
    		
    		segmentNo +=1;
    		if(segmentNo > 3)
    			break;
    	}
    	//determinam lungimea si latimea imaginii, stiind cum arata formatul bmp si ce biti sunt pentru lungime si latime
		int width = result[18] + result[19] + result[20] + result[21];
		int height = result[22] + result[23] + result[24] + result[25];
    
//		System.out.println(width + " " + height);
		
		//instantiem un obiect de timp ImageMirror pentru a face opperatia de mirror cu ajutprul lui.
		//Ne folosim de constructprul cu parametrii
    	ImageMirror imageMirrorProcessor = new ImageMirror(width, height, result);
    	
    	//efectuam mirrorul
    	byte[] flippedImage = imageMirrorProcessor.mirror();
    	
    	//trimitem rezultatul prin pipe la writer
    	try {
    		System.out.println("{CONSUMER} Sending result of size " + result.length);
    		outputPipe.setSegmentSize(flippedImage.length);
			outputPipe.sendData(flippedImage);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    	
    	Static.consumerDone.release();
    	System.out.println("{CONSUMER} Consumer done");
    	try {
    		System.out.println("{CONSUMER} Waiting for writer to read");
			Static.writerDone.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	Static.writerDone.release();
    	
    }
}
