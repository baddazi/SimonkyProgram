



import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyImageView extends ImageView {
	private File f = null;

	public MyImageView(Image image) {
		super(image);
	}

	public File getFile() {
		return f;
	}

	public void setFile(File p) {
		this.f = p;
	}
	
	

}
