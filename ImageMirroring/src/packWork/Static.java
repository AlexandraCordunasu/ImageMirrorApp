package packWork;

import java.util.concurrent.Semaphore;

//aici imi declar variabilele cu ajutorul carora realizezi comunicarea intre threaduri
public class Static {

	public static Semaphore producerInit = new Semaphore(0);
	public static Semaphore mpty = new Semaphore(1);
	public static Semaphore full = new Semaphore(0);
	public static Semaphore writerDone = new Semaphore(0);
	public static Semaphore consumerDone = new Semaphore(0);
	public static Semaphore canSetSegmentSize = new Semaphore(1);
	
}
