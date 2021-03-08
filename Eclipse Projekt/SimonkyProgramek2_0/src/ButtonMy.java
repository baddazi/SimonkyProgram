import javafx.scene.control.Button;

public class ButtonMy extends Button {
	
	private int idm;
	
	public ButtonMy(int idm) {
		this.idm=idm;
	}

	public int getIdm() {
		return idm;
	}

	public void setIdm(int id) {
		this.idm = id;
	}
	

}
