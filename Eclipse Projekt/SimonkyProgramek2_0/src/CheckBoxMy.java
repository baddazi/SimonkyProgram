import javafx.scene.control.CheckBox;

public class CheckBoxMy extends CheckBox {
	
private int idm;
	
	public CheckBoxMy(int idm) {
		this.idm=idm;
	}

	public int getIdm() {
		return idm;
	}

	public void setIdm(int id) {
		this.idm = idm;
	}
	

}
