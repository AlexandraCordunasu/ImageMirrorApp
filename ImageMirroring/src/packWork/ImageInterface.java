package packWork;

import java.io.IOException;

// Interfaa pentru procesarea imaginilor
public interface ImageInterface {

    // Metoda pentru procesarea imaginilor
    byte[] mirror() throws IOException;
}
