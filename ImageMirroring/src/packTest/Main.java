package packTest;

import java.util.Scanner;

import packWork.ImageProducer;
import packWork.ImageConsumer;
import packWork.Pipe;
import packWork.WriterResult;

// Clasa principalã de test pentru aplicaia de procesare a imaginilor
public class Main {

    public static void main(String ...varArgs) {
        Scanner scanner = new Scanner(System.in);
//        System.out.println(varArgs[0]);
        try {
        	
        	//Citim de la tastatura calea catre imaginea sursa si catre cea destinatie.
            System.out.print("Introduceti calea imaginii sursã (BMP): ");
            String inputFilePath = scanner.next();
            //C:\\Users\\alexa\\Desktop\\ImageMirroring\\src\\packTest\\image1.bmp
            System.out.println("Calea introdusã: " + inputFilePath);
            
            System.out.print("Introduceti calea imaginii de iesire BMP): ");
            //C:\\Users\\alexa\\Desktop\\ImageMirroring\\src\\packTest\\out.bmp
            String outputImagePath = scanner.next();
            
            //instantiem obiecte pentru clasele noastre.
            Pipe producerToConsumerPipe = new Pipe();
            Pipe consumerToWriterPipe = new Pipe();
            ImageProducer imgprod = new ImageProducer(producerToConsumerPipe,inputFilePath);
            ImageConsumer imgcons =  new ImageConsumer(producerToConsumerPipe, consumerToWriterPipe);
            WriterResult writer = new WriterResult(consumerToWriterPipe,outputImagePath );
            
            
            //pornim firele de executie pentru producator consumator si scriere
            imgprod.start();
            imgcons.start();       
            writer.start();
            
            //se asteapta ca toate firele de executie sa se termine inainte de a continua
            imgprod.join();
            imgcons.join();
            writer.join();
 
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        } finally {
        	//inchidem obiectul scanner care ne ajuta la citire
            scanner.close();
        }
    }
}
