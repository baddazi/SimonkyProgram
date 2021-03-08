import javafx.event.EventHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Zaznam  {
	private UzivatelskeRozhrani uz;
	private String cesta = "Files";
	private int id;
	private String nazev;
	private String popis;
	private int predpokladanaCena;
	private int pravaCena;
	private TipSperku tip;
	private String tipString;
	private MyImageView mainImage;
	private ButtonMy button;
	private int skladem;
	private String separator = ";";

	public Zaznam(int id, String nazev, String popis, int predpokladanaCena, int pravaCena, TipSperku tip,
			int skladem) {
		this.id = id;
		this.nazev = nazev;
		this.popis = popis;
		this.predpokladanaCena = predpokladanaCena;
		this.pravaCena = pravaCena;
		this.tip = tip;
		tipString = tip.getStringByEnum(tip);
		mainImage = foundImage();

		button = new ButtonMy(id);
		button.setText("Prohédni");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uz.setPodrobneScene(button.getIdm(),false);
			}
		});

		this.skladem = skladem;

	}

	public Zaznam(String[] zaznam) {
		seperate(zaznam);
	}

	private void seperate(String[] zaznam) {

		id = Integer.parseInt(zaznam[0]);
		nazev = zaznam[1];
		popis = zaznam[2];
		predpokladanaCena = Integer.parseInt(zaznam[3]);
		pravaCena = Integer.parseInt(zaznam[4]);
		tip = (TipSperku.getEnumByString(zaznam[5]));
		tipString = zaznam[5];
		mainImage = foundImage();

		button = new ButtonMy(id);
		button.setText("Prohédni");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uz.setPodrobneScene(id,false);
			}
		});

		this.skladem = Integer.parseInt(zaznam[6]);

	}

	public int getSkladem() {
		return skladem;
	}

	public UzivatelskeRozhrani getUz() {
		return uz;
	}

	public void setUz(UzivatelskeRozhrani uz) {
		this.uz = uz;
	}

	public void setImage() {
		mainImage = foundImage();
	}

	private MyImageView foundImage() {

		try {
			FileInputStream fi = new FileInputStream(cesta + File.separator + id + File.separator + "main.jpg");

			MyImageView im = new MyImageView(new Image(fi,100,100,true,false));
			fi.close();
			
			return im;

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return null;
	}

	public MyImageView getMainImage() {
		return mainImage;
	}

	public String getTipString() {
		return tipString;
	}

	public void settTpString(String tipString) {
		this.tipString = tipString;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ButtonMy getButton() {
		return button;
	}

	public void setButton(ButtonMy button) {
		this.button = button;
	}

	public String getNazev() {
		return nazev;
	}

	public void setNazev(String nazev) {
		this.nazev = nazev;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public int getPredpokladanaCena() {
		return predpokladanaCena;
	}

	public void setPredpokladanaCena(int predpokladanaCena) {
		this.predpokladanaCena = predpokladanaCena;
	}

	public int getPravaCena() {
		return pravaCena;
	}

	public void setPravaCena(int pravaCena) {
		this.pravaCena = pravaCena;
	}

	public TipSperku getTip() {
		return tip;
	}

	public void setTip(TipSperku tip) {
		this.tip = tip;
	}
	public void setSkladem(int skladem) {
		this.skladem = skladem;
	}

	public String getTextToWrite() {

		return this.id + separator + this.nazev + separator + this.popis + separator + this.predpokladanaCena
				+ separator + this.pravaCena + separator + TipSperku.getStringByEnum(tip) + separator + skladem
				+ separator + separator;

	}



	public String toString() {

		return "id: " + id + ", název: " + nazev + ", popis: " + popis + ", předpokládaná cena: " + predpokladanaCena
				+ ", pravá cena: " + pravaCena + ", tip: " + tip.getStringByEnum(tip);

	}

}
