package packWork;

import java.io.FileOutputStream;
import java.io.IOException;

//clasa in care realizez scrierea imaginii procesate
public class WriterResult extends Thread {
    private final String outputPath;
    private final Pipe pipe;

    public WriterResult(Pipe pipe, String outputPath) {
        this.outputPath = outputPath;
        this.pipe = pipe;
    }

    @Override
    public void run() {
    	try {
			Static.consumerDone.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
        	byte[] toWrite = pipe.receiveData();
        	System.out.println("{WRITER} Received data of size " + toWrite.length);
        	Static.writerDone.release();
        	//System.out.println("{WRITER} Writer done");
            fileOutputStream.write(toWrite);
            //System.out.println("Result written to " + outputPath);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
