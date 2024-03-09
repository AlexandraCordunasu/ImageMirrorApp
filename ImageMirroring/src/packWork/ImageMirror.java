package packWork;

public class ImageMirror extends ImageMirrorBase {

	//ne delcaram variabilele de care avem nevoie pentru a realiza mirroeul
	private final int width;
	private final int height;
	private byte[] imageData=null;
	
	//constructor de copiere cu parametrii 
	public ImageMirror(int width,int height, byte[] imageData){
		this.width=width;
		this.height=height;
		this.imageData=imageData;
	}
	
	//metoda cu ajutorul careia facem mirror la imagine
	//noi retinem imaginea sub forma unui sir de bytes primii(54) fiind dedicati pentru header si informatii legate de header
	@Override
	public byte[] mirror(){
		//ne cream un nou sir de bytes pentru a retine imaginea oglindita
		byte[] flippedImage = new byte[imageData.length];
		
		//primii 54 de bytes taman la fel deoarece acestia contin informati despre header
		for(int i = 0;i<54;i++){
			flippedImage[i]=imageData[i];
		}
		
		//parcurgem sirul ca si cum am parcurge o matrice. pentru ca un bit e gormast din trei parti RGB noi parcurgem din trei in trei sirul ca sa luam 
		//bytes pentru fiecare culoare si sa ii schimbam intre ei
		for(int heightIteration = 0; heightIteration < height*3; heightIteration+=3){
			for(int widthIteration = 0; widthIteration < width*3; widthIteration+=3){
				//pentru red
				flippedImage[54+heightIteration*width+widthIteration]= imageData[54+heightIteration*width + (width*3 -3 - widthIteration)];
//				System.out.println((54+heightIteration*width+widthIteration)+ " | " + (54+heightIteration*width + (width*3 -3 - widthIteration)));
				//pentru green
				flippedImage[54+heightIteration*width+widthIteration +1]= imageData[54+heightIteration*width + (width*3 -3 - widthIteration)+1];
//				System.out.println((54+heightIteration*width+widthIteration + 1)+ " | " + (54+heightIteration*width + (width*3 -3 - widthIteration)+1));
				//pentru blue
				flippedImage[54+heightIteration*width+widthIteration +2]= imageData[54+heightIteration*width + (width*3 -3 - widthIteration)+2];
				//System.out.println((54+heightIteration*width+widthIteration+2)+ " | " + (54+heightIteration*width + (width*3 -3 - widthIteration)+2));
				
				
			}
			//System.out.print('\n');
		}
		//returnam imaginea
		return flippedImage;
	}
}
