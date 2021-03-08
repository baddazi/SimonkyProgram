
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.Optional;
import java.util.Queue;

import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UzivatelskeRozhrani extends Application {

	private static Scanner sc;
	private String path = "Files";
	private String nameOfFile = "napsanaData.txt";
	private Stage primStage;
	private TableView<Zaznam> table = new TableView<Zaznam>();
	private Queue<Zaznam> zaznamy;
	private String separator = ";";
	private List<File> other;
	private File main;
	private String nameOfMainPicture = "main.jpg";
	private File lastFile = null;
	private LinkedList<MyImageView> pictures = null;
	private File[] listOfFiles;
	private MyImageView rightClick;
	private ComboBox<String> combo;
	private String selectedTyp = "";

	private final String title = "Simonky ultimátní program na šperky";

	@Override
	public void start(Stage arg0) throws Exception {

		fillQueue();
		createTable();

		primStage = arg0;
		Scene scene = new Scene(getParrent());
		primStage.setScene(scene);

		primStage.show();

		primStage.setTitle(title);

	}

	private void fillQueue() {

		fillScanner();
		zaznamy = new LinkedList<Zaznam>();
		while (sc.hasNext()) {
			zaznamy.add(foundZaznam());
		}

	}

	private Zaznam foundZaznam() {

		int countOfWord = 7;
		boolean end = false;
		String words[] = new String[countOfWord];
		int k = 0;
		while (!end) {
			String word = "";
			String pom = sc.next();
			if (!pom.equals(separator)) {
				while (!pom.equals(separator)) {
					word = word + pom;
					pom = sc.next();

				}
				words[k] = word;
				k++;
			} else {
				end = true;
			}

		}
		Zaznam temp = new Zaznam(words);
		temp.setUz(this);
		return temp;
	}

	private void fillScanner() {

		File mainFile = null;
		try {
			mainFile = new File(path + File.separator + nameOfFile);
			sc = new Scanner(mainFile, "utf-8");

			sc.useDelimiter("");
		} catch (FileNotFoundException e) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nenalezen Soubor");
			alert.setHeaderText("Soubor nenalezen, vytvářím nový");
			alert.setContentText("Najdeteho v " + path + File.separator + nameOfFile);
			createNewFile();
			alert.showAndWait();
		}

	}

	private void createNewFile() {

		File f = null;
		try {
			f = new File(path + File.separator + nameOfFile);
			File g = new File(path);
			if (!g.exists())
				g.mkdir();
			f.createNewFile();
			sc = new Scanner(f);
			sc.useDelimiter("");
		} catch (FileNotFoundException e) {
			createExceptionAlert("Soubory nelze najít, ani po vytvoření, chyba číslo 1", e);

		} catch (IOException e) {
			createExceptionAlert("Nespecifiková chyba při vytváření souboru, chyba číslo 2", e);

		}

	}

	private void createExceptionAlert(String contentText, Exception e) {

		String title = "Error";
		String headerText = "Neočekávaná chyba";
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText + ", " + e.getMessage());
		alert.showAndWait();
		Platform.exit();

	}

	public void setPodrobneScene(int id, boolean manage) {
		primStage.setScene(new Scene(getParrent2(id, manage)));

	}

	private Parent getParrent2(int id, boolean manage) {
		BorderPane b = new BorderPane();
		BorderPane z = new BorderPane();
		Button zpet = new Button("Zpět");
		zpet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primStage.setScene(new Scene(getParrent()));
			}
		});

		zpet.setPadding(new Insets(10));

		Iterator<Zaznam> iterator = zaznamy.iterator();
		boolean end = false;

		Zaznam temp1 = null;
		while (iterator.hasNext() && !end) {
			temp1 = (Zaznam) iterator.next();
			if (temp1.getId() == id) {
				end = true;
			}
		}
		final Zaznam temp = temp1;
		HBox box = new HBox();
		VBox box1 = new VBox();

		GridPane grid = new GridPane();

		Label nazevText = new Label("Název:");
		TextField nazev = new TextField();
		nazev.setText(temp.getNazev());
		nazev.setEditable(false);

		Label idText = new Label("ID:");
		TextField id1 = new TextField();
		id1.setText(temp.getId() + "");
		id1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					id1.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		id1.setEditable(false);

		Label tipText = new Label("Tip:");
		ComboBox<String> tip = new ComboBox<String>();
		tip.getItems().add(TipSperku.getStringByEnum(TipSperku.NAUSNICE));
		tip.getItems().add(TipSperku.getStringByEnum(TipSperku.NARAMEK));
		tip.getItems().add(TipSperku.getStringByEnum(TipSperku.RETIZEK));
		tip.getItems().add(TipSperku.getStringByEnum(TipSperku.SADA));
		tip.getItems().add(TipSperku.getStringByEnum(TipSperku.ANDEL));
		tip.getItems().add(TipSperku.getStringByEnum(TipSperku.OSTATNI));

		tip.getSelectionModel().select(TipSperku.getStringByEnum(temp.getTip()));
		tip.setDisable(true);

		Label predCenaText = new Label("Předpokládána cena:");
		TextField predCena = new TextField();
		predCena.setText(temp.getPredpokladanaCena() + "");
		predCena.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					predCena.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		predCena.setEditable(false);

		Label pravaCenaText = new Label("Správná cena:");
		TextField pravaCena = new TextField();
		pravaCena.setText(temp.getPravaCena() + "");
		pravaCena.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					pravaCena.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}
		});
		pravaCena.setEditable(false);

		Label prodanoText = new Label("Počet kusů");
		TextField prodano = new TextField();
		prodano.setText(temp.getSkladem() + "");
		prodano.setEditable(false);

		Label photoText = new Label("Přidej další fotky: ");
		Button photo = new Button("Vyber");
		VBox photo1 = new VBox();
		Label nameOfSelectedPhoto = new Label();
		photo.setDisable(true);
		photo.setPadding(new Insets(20));

		photo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fl = new FileChooser();
				fl.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg", "*jpeg"));
				if (lastFile != null)
					if (lastFile.exists())
						fl.setInitialDirectory(lastFile);
				other = fl.showOpenMultipleDialog(primStage);
				if (other != null && !other.isEmpty()) {
					nameOfSelectedPhoto.setText(getNameOfFiles(other));
					Iterator<File> it = other.iterator();
					lastFile = new File(getPath(it.next()));
				}
			}
		});
		photo1.getChildren().addAll(photo, nameOfSelectedPhoto);

		grid.add(nazevText, 0, 0);
		grid.add(nazev, 1, 0);

		grid.add(idText, 0, 1);
		grid.add(id1, 1, 1);

		grid.add(tipText, 0, 2);
		grid.add(tip, 1, 2);

		grid.add(predCenaText, 0, 3);
		grid.add(predCena, 1, 3);

		grid.add(pravaCenaText, 0, 4);
		grid.add(pravaCena, 1, 4);

		grid.add(prodanoText, 0, 5);
		grid.add(prodano, 1, 5);

		grid.add(photoText, 0, 6);
		grid.add(photo1, 1, 6);

		TextArea text = new TextArea();
		Label textText = new Label("Popis:");
		text.setText(temp.getPopis());
		text.setEditable(false);
		VBox b1 = new VBox();
		b1.getChildren().addAll(textText, text);
		b1.setPadding(new Insets(10));
		grid.setPadding(new Insets(10));
		MyImageView view = temp.getMainImage();

		box.getChildren().addAll(view, grid, b1);
		box.setPadding(new Insets(20));
		box1.setPadding(new Insets(20));

		Zaznam old = new Zaznam(temp.getId(), temp.getNazev(), temp.getPopis(), temp.getPredpokladanaCena(),
				temp.getPravaCena(), temp.getTip(), temp.getSkladem());
		Button uprav = new Button("Upravit");
		uprav.setPadding(new Insets(10));
		uprav.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (uprav.getText().equals("Upravit")) {
					id1.setEditable(true);
					text.setEditable(true);
					tip.setDisable(false);
					nazev.setEditable(true);
					predCena.setEditable(true);
					pravaCena.setEditable(true);
					prodano.setEditable(true);
					setPictureListener(id, temp);
					photo.setDisable(false);

					uprav.setText("Ulož změny");
				} else {
					int isSame = isSame(old, id1, text, tip, nazev, predCena, pravaCena, prodano, other);
					if (isSame == 0) {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Údaj se nezměnili");
						alert.setHeaderText("Nelze uložit nezměněné údaje");
						alert.setContentText("Údaje se nějak nezměnili, nelze je uložit");
						alert.showAndWait();
						return;
					} else {

						if (isSame == 2 || isSame == 3) {

							if (Integer.parseInt(id1.getText()) == old.getId()
									|| checkId(Integer.parseInt(id1.getText()))) {

								if (Integer.parseInt(id1.getText()) != old.getId()) {

									File f = new File(path + File.separator + old.getId() + File.separator);
									File g = new File(path + File.separator + id1.getText() + File.separator);

									f.renameTo(g);

								}

								Zaznam new1 = new Zaznam(Integer.parseInt(id1.getText()), nazev.getText(),
										text.getText(), Integer.parseInt(predCena.getText()),
										Integer.parseInt(pravaCena.getText()),
										TipSperku.getEnumByString((String) tip.getSelectionModel().getSelectedItem()),
										Integer.parseInt(prodano.getText()));

								ithemChange(old, new1);
							} else {

								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Změněné id je již použávni");
								alert.setHeaderText("Toto id nelze použít");
								alert.setContentText(
										"Toto id je již používáno. Vyber prosím jiné, nebo záznam s tímto id smaž");
								alert.showAndWait();

							}
						}
						if (isSame == 1 || isSame == 3) {

							for (File file : other) {
								try {
									Files.copy(file.toPath(),
											(new File(path + File.separator + id + File.separator + file.getName()))
													.toPath(),
											StandardCopyOption.REPLACE_EXISTING);

								} catch (IOException e) {
									createExceptionAlert("Chyba při kopírování souborů, chyba číslo 8", e);
								}
							}
							other = null;

							setPodrobneScene(id, false);
						}

					}

					uprav.setText("Upravit");
				}

			}

		});

		z.setLeft(zpet);
		z.setRight(uprav);

		b.setTop(z);

		b.setCenter(box);
		b.setBottom(loadPiucture(id));
		setPictureDetailClick(temp);

		if (manage)
			uprav.fire();

		return b;
	}

	private void setPictureDetailClick(Zaznam z) {
		Iterator<MyImageView> it = pictures.iterator();
		while (it.hasNext()) {
			MyImageView temp = it.next();

			temp.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					MouseButton button = event.getButton();
					if (button == MouseButton.PRIMARY) {
						pictureNewWindow(temp);
					}

				}

			});
		}

	}

	private void setPictureListener(int id, Zaznam z) {

		Iterator<MyImageView> it = pictures.iterator();

		while (it.hasNext()) {
			MyImageView temp = it.next();

			ContextMenu cm = new ContextMenu();
			MenuItem mi1 = new MenuItem("Smazat fotku");
			MenuItem mi2 = new MenuItem("Zvolit za hlavní fotku");
			String nameOfFile = temp.getFile().getName();

			if (nameOfFile.equals(nameOfMainPicture)) {
				cm.getItems().addAll(mi1);
			} else
				cm.getItems().addAll(mi1, mi2);

			mi1.setOnAction((ActionEvent event) -> {

				deletePicture(rightClick.getFile());
				setPodrobneScene(id, true);
			});

			mi2.setOnAction((ActionEvent event) -> {

				changeToMain(rightClick.getFile());
				z.setImage();
				setPodrobneScene(id, true);
			});

			temp.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					MouseButton button = event.getButton();
					if (button == MouseButton.SECONDARY) {
						rightClick = temp;
					}

					if (button == MouseButton.PRIMARY) {
						pictureNewWindow(temp);
					}

				}

			});
			temp.setOnContextMenuRequested(event -> {
				ContextMenuEvent e = (ContextMenuEvent) event;
				cm.show(temp, e.getScreenX(), e.getScreenY());
			});

		}

	}

	private void pictureNewWindow(MyImageView select) {
		Stage stage = new Stage();
		stage.setTitle("Detail obrázku");

		HBox h = new HBox();
		HBox h1 = new HBox();
		HBox h2 = new HBox();

		MyImageView im = addImageViewofDetailedPicture(select);

		Button before = new Button("Předchozí");
		before.setPadding(new Insets(20));
		before.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Iterator<MyImageView> it = pictures.iterator();
				MyImageView result = null;
				while (it.hasNext()) {
					MyImageView temp = it.next();
					if (temp.equals(select)) {
						break;
					}
					result = temp;

				}

				if (result == null)
					result = pictures.peekLast();

				stage.close();
				pictureNewWindow(result);

			}

		});

		Button after = new Button("Následující");
		after.setPadding(new Insets(20));
		after.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Iterator<MyImageView> it = pictures.iterator();
				MyImageView result = null;
				while (it.hasNext()) {
					MyImageView temp = it.next();
					if (temp.equals(select)) {
						result = temp;
						break;
					}

				}

				if (it.hasNext())
					result = it.next();
				else
					result = pictures.peekFirst();

				stage.close();
				pictureNewWindow(result);

			}

		});

		h1.getChildren().add(before);
		h1.setAlignment(Pos.CENTER);
		h1.setPadding(new Insets(5));

		h2.getChildren().add(after);
		h2.setAlignment(Pos.CENTER);
		h2.setPadding(new Insets(5));

		h.getChildren().addAll(h1, im, h2);

		Scene scene = new Scene(h);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case LEFT:
					before.fire();
					break;
				case RIGHT:
					after.fire();
					break;

				}
			}
		});

		stage.setScene(scene);
		stage.show();

	}

	private MyImageView addImageViewofDetailedPicture(MyImageView temp) {
		MyImageView im = null;
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		try {
			Image image = new Image(temp.getFile().toURI().toURL().toString());
			if (image.getWidth() > screenBounds.getWidth() * 0.85 || image.getHeight() > screenBounds.getHeight() * 0.9)
				image = new Image(temp.getFile().toURI().toURL().toString(), screenBounds.getWidth() * 0.85,
						screenBounds.getHeight() * 0.9, true, false);

			im = new MyImageView(image);
		} catch (MalformedURLException e) {
			createExceptionAlert("Nespecifikovaná chyba url fotky, chyba číslo 10", e);
		}

		return im;

	}

	private void changeToMain(File file) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Hlavní fotka");
		alert.setHeaderText("Změna hlavní fotky");
		alert.setContentText("Při potvrzení dojde ke změně hlavní fotky, ale přijdeš o veškerá neuložená data");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			String path = getPath(file);
			File main = new File(path + File.separator + nameOfMainPicture);

			String newNameOfFile = generatedNameofFile(path);

			try {
				Files.move(main.toPath(), main.toPath().resolveSibling(newNameOfFile));

			} catch (IOException e) {

				createExceptionAlert("Chyba při přesouvání fotky, chyba číslo 11", e);
			}

			try {

				Files.move(file.toPath(), file.toPath().resolveSibling(nameOfMainPicture));

			} catch (IOException e) {

				createExceptionAlert("Chyba při přesouvání fotky, chyba číslo 12", e);
			}

		}

	}

	private String generatedNameofFile(String path) {
		Random rand = new Random();
		String newNameOfFile = "GeneratedName" + rand.nextInt() + ".jpg";

		while (new File(path + File.separator + newNameOfFile).exists()) {
			newNameOfFile = "GeneratedName" + rand.nextInt(100) + ".jpg";
		}

		return newNameOfFile;

	}

	private void deletePicture(File file) {

		if (file.getName().equals(nameOfMainPicture)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Nedovolená akce");
			alert.setHeaderText("Nelze smazat hlavní fotku");
			alert.setContentText("Pokud chceš tuto fotku smazat, musíš nejdříve zvolit jinou fotku jako hlavní");
			alert.showAndWait();
			return;
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Smazání fotky");
		alert.setHeaderText("Nevratné smazání fotky");
		alert.setContentText("Při potvrezní bude nevratně fotka smazán a přijdeš o veškeré neuložené změny.");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			file.delete();

		}

	}

	private void ithemChange(Zaznam old, Zaznam new1) {

		Iterator<Zaznam> it = zaznamy.iterator();

		while (it.hasNext()) {
			Zaznam pom = it.next();
			if (pom.getId() == old.getId()) {
				it.remove();
				break;
			}

		}

		zaznamy.add(new1);
		rewriteAll();

		fillQueue();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Úspěšná změna");
		alert.setHeaderText("Prvek byl aktualizován");
		alert.setContentText("Prvek byl úspěšně aktualizován a uložen");
		setPodrobneScene(new1.getId(), false);
		alert.showAndWait();

	}

	private int isSame(Zaznam temp, TextField id1, TextArea text, ComboBox<String> tip, TextField nazev,
			TextField predCena, TextField pravaCena, TextField prodano, List<File> files) {
		boolean result = false;
		boolean result1 = false;

		if (text.getText().equals(""))
			text.setText(" ");

		if (temp.getId() != Integer.parseInt(id1.getText()))
			result = true;

		if (!temp.getPopis().equals(text.getText()))
			result = true;

		if (TipSperku.getEnumByString((String) tip.getSelectionModel().getSelectedItem()) != temp.getTip())
			result = true;

		if (!temp.getNazev().equals(nazev.getText()))
			result = true;

		if (temp.getPredpokladanaCena() != Integer.parseInt(predCena.getText()))
			result = true;

		if (temp.getPravaCena() != Integer.parseInt(pravaCena.getText()))
			result = true;

		if (Integer.parseInt(prodano.getText()) != temp.getSkladem())
			result = true;

		if (files != null && !files.isEmpty())
			result1 = true;

		if (result && result1)
			return 3;
		if (result)
			return 2;
		if (result1)
			return 1;
		return 0;
	}

	private Node loadPiucture(int id) {

		VBox box = new VBox();
		pictures = new LinkedList<MyImageView>();

		File folder = new File(path + File.separator + id + File.separator);
		listOfFiles = folder.listFiles();
		int k = 0;

		for (File file : listOfFiles) {
			if (file.isFile()) {
				try {
					FileInputStream fi = new FileInputStream(file.getAbsolutePath());
					MyImageView image = new MyImageView(new Image(fi, 150, 150, true, false));
					image.setFile(new File(file.getAbsolutePath()));
					pictures.add(image);
					fi.close();
				} catch (FileNotFoundException e) {
					createExceptionAlert("Vybraná fotka nenalezena, chyba číslo 3", e);

				} catch (IOException e) {
					createExceptionAlert("Neznámá chyba při koupírování fotek, chyba číslo 4", e);

				}
				k++;
			}
		}

		int s = k % 5;

		s++;
		int l = 0;
		if (k == 0)
			return box;

		Iterator<MyImageView> it = pictures.iterator();

		for (int i = 0; i <= s; i++) {

			HBox b = new HBox();
			b.setPadding(new Insets(10));
			while (it.hasNext() && l < 5) {
				MyImageView im = it.next();
				HBox b2 = new HBox();
				b2.setPadding(new Insets(10));

				b2.getChildren().add(im);
				mouseEnterPicture(b2);
				b.getChildren().add(b2);
				l++;
			}
			l = 0;
			box.getChildren().add(b);
			if (!it.hasNext())
				break;
		}

		return box;

	}

	private void mouseEnterPicture(HBox b2) {
		b2.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				b2.setStyle("-fx-background-color: rgb(" + 217 + "," + 243 + ", " + 255 + ");");
			}
		});

		b2.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				b2.setStyle("-fx-background-color: none");
			}
		});

	}

	private Parent getParrent() {
		table.getItems().clear();
		BorderPane test = new BorderPane();
		BorderPane rootPaneBP = new BorderPane();

		rootPaneBP.setTop(createFiltr());
		rootPaneBP.setCenter(table);

		test.setTop(getMenu());
		test.setCenter(rootPaneBP);

		if (selectedTyp.length() > 0)
			changeCombo(selectedTyp);
		table.sort();
		return test;
	}

	private Node createFiltr() {

		HBox box = new HBox();
		Label label = new Label();
		label.setText("Filtr: ");
		label.setFont(new Font("Arial", 15));
		combo = new ComboBox<String>();
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.NAUSNICE));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.NARAMEK));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.RETIZEK));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.SADA));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.ANDEL));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.OSTATNI));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.VSE));

		combo.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				changeCombo(arg2);
				selectedTyp = arg2;
			}

		});

		if (selectedTyp.length() > 0)

			combo.getSelectionModel().select(selectedTyp);
		else
			combo.getSelectionModel().selectLast();

		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(40));

		box.getChildren().addAll(label, combo);

		return box;
	}

	private void createTable() {
		table.setEditable(false);
		createColums();

	}

	private void changeCombo(String arg2) {

		if (TipSperku.getEnumByString(arg2) == TipSperku.VSE) {
			table.getItems().clear();
			table.getItems().addAll(zaznamy);
			return;
		}
		table.getItems().clear();
		Queue<Zaznam> pr = new LinkedList<Zaznam>();
		Iterator<Zaznam> iterator = zaznamy.iterator();
		while (iterator.hasNext()) {
			Zaznam temp = (Zaznam) iterator.next();
			if (temp.getTip() == TipSperku.getEnumByString(arg2)) {
				pr.add(temp);
			}
		}

		table.getItems().addAll(pr);
		table.sort();

	}

	private void rewriteAll() {
		Writer fr;
		try {

			// fr = new FileWriter(path + File.separator + nameOfFile);
			fr = new OutputStreamWriter(new FileOutputStream(new File(path + File.separator + nameOfFile)),
					StandardCharsets.UTF_8);
			Iterator<Zaznam> it = zaznamy.iterator();

			while (it.hasNext()) {

				fr.write(it.next().getTextToWrite());
			}
			fr.close();
		} catch (IOException e) {
			createExceptionAlert("Neznáma chyba při přepisování souboru, chyba číslo 5", e);

		}

	}

	private void createColums() {

		TableColumn<Zaznam, Integer> id = new TableColumn<Zaznam, Integer>("ID");
		TableColumn<Zaznam, String> nazev = new TableColumn<Zaznam, String>("Název");
		TableColumn<Zaznam, String> tip = new TableColumn<Zaznam, String>("Tip");
		TableColumn<Zaznam, Integer> prepCena = new TableColumn<Zaznam, Integer>("Předpokládaná Cena");
		TableColumn<Zaznam, Integer> pravaCena = new TableColumn<Zaznam, Integer>("Pravá Cena");
		TableColumn<Zaznam, MyImageView> foto = new TableColumn<Zaznam, MyImageView>("Hlavní fotka");
		TableColumn<Zaznam, ButtonMy> button = new TableColumn<Zaznam, ButtonMy>("Prohlednuti");
		TableColumn<Zaznam, Integer> check = new TableColumn<Zaznam, Integer>("Počet kusů");

		check.setCellValueFactory(new PropertyValueFactory<Zaznam, Integer>("skladem"));
		id.setCellValueFactory(new PropertyValueFactory<Zaznam, Integer>("id"));
		nazev.setCellValueFactory(new PropertyValueFactory<Zaznam, String>("nazev"));
		tip.setCellValueFactory(new PropertyValueFactory<Zaznam, String>("tipString"));
		prepCena.setCellValueFactory(new PropertyValueFactory<Zaznam, Integer>("predpokladanaCena"));
		pravaCena.setCellValueFactory(new PropertyValueFactory<Zaznam, Integer>("pravaCena"));
		foto.setCellValueFactory(new PropertyValueFactory<Zaznam, MyImageView>("mainImage"));
		foto.setPrefWidth(250);
		button.setCellValueFactory(new PropertyValueFactory<Zaznam, ButtonMy>("button"));

		table.getColumns().addAll(id, foto, nazev, tip, prepCena, pravaCena, check, button);
		table.getItems().addAll(zaznamy);

		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		ContextMenu cm = new ContextMenu();
		MenuItem mi1 = new MenuItem("Smaž záznam");

		mi1.setOnAction((ActionEvent event) -> {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Smazání produktu");
			alert.setHeaderText("Nevratné smazání produktu");
			alert.setContentText("Veškerá data o produktu budou nevratně smazána");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Zaznam item = table.getSelectionModel().getSelectedItem();
				smaz(item);
			}

		});

		cm.getItems().add(mi1);

		table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.SECONDARY) {
					cm.show(table, t.getScreenX(), t.getScreenY());
				}
			}
		});

		table.setRowFactory(tv -> {
			TableRow<Zaznam> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Zaznam rowData = row.getItem();
					setPodrobneScene(rowData.getId(), false);
				}
			});
			return row;
		});

		table.getSortOrder().add(id);

	}

	private void smaz(Zaznam item) {

		Iterator<Zaznam> it = zaznamy.iterator();

		while (it.hasNext()) {
			if (it.next().getId() == item.getId()) {
				it.remove();
				break;
			}

		}

		File f = new File(path + File.separator + item.getId());

		File[] listOfFiles = f.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				file.delete();

			}
		}

		f.delete();

		rewriteAll();
		fillQueue();

		primStage.setScene(new Scene(getParrent()));

	}

	public static void main(String[] args) {
		launch(args);
	}

	private Node getMenu() {
		MenuBar menuMB = new MenuBar();
		Menu menu = new Menu("Nový");
		Menu menu2 = new Menu("Export");
		MenuItem novy = new MenuItem("Zadat nový produkt");
		MenuItem export = new MenuItem("Export do PDF");
		novy.setOnAction(event -> {
			pridatNovyPScena();
		});
		export.setOnAction(event -> {
			exportScena();
		});

		menu.getItems().add(novy);
		menu2.getItems().add(export);
		menuMB.getMenus().addAll(menu, menu2);
		return menuMB;
	}

	private void exportScena() {
		primStage.setScene(new Scene(getRootExport()));

	}

	private Parent getRootExport() {
		BorderPane main = new BorderPane();
		int insets = 5;
		int insets2 = 10;

		BorderPane buttons = new BorderPane();
		Button zpet = new Button("Zpět");
		zpet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primStage.setScene(new Scene(getParrent()));
			}
		});
		zpet.setPadding(new Insets(10));

		Button export = new Button("Export");

		export.setPadding(new Insets(10));

		buttons.setLeft(zpet);
		buttons.setRight(export);

		HBox hbox = new HBox();
		VBox left = new VBox();
		VBox right = new VBox();

		// left
		VBox tip = new VBox();

		HBox h = new HBox();

		Label leftLabel = new Label("Které produkty budou v PDF?");
		leftLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 15));
		leftLabel.setPadding(new Insets(0, 0, insets2, 0));

		HBox h1 = new HBox();
		Label allTipText = new Label("Všechny typy produktů");
		CheckBox allTip = new CheckBox();
		allTip.setSelected(true);
		TipSperku[] tips = new TipSperku[6];

		h1.getChildren().addAll(allTip, allTipText);

		VBox v1 = new VBox();
		CheckBox nausnice = new CheckBox();
		nausnice.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tips[0] = TipSperku.NAUSNICE;

				} else {
					tips[0] = null;
				}

			}
		});
		nausnice.setDisable(true);
		Label nausniceText = new Label("Náušnice");
		v1.getChildren().addAll(nausnice, nausniceText);
		v1.setAlignment(Pos.CENTER);
		v1.setPadding(new Insets(insets));

		VBox v2 = new VBox();
		CheckBox naramek = new CheckBox();
		naramek.setDisable(true);
		naramek.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tips[1] = TipSperku.NARAMEK;

				} else {
					tips[1] = null;
				}

			}
		});
		Label naramekText = new Label("Náramek");
		v2.getChildren().addAll(naramek, naramekText);
		v2.setAlignment(Pos.CENTER);
		v2.setPadding(new Insets(insets));

		VBox v3 = new VBox();
		CheckBox retizek = new CheckBox();
		retizek.setDisable(true);
		retizek.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tips[2] = TipSperku.RETIZEK;

				} else {
					tips[2] = null;
				}

			}
		});
		Label retizekText = new Label("Řetízek");
		v3.getChildren().addAll(retizek, retizekText);
		v3.setAlignment(Pos.CENTER);
		v3.setPadding(new Insets(insets));

		VBox v4 = new VBox();
		CheckBox sada = new CheckBox();
		sada.setDisable(true);
		sada.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tips[3] = TipSperku.SADA;

				} else {
					tips[3] = null;
				}

			}
		});
		Label sadaText = new Label("Sada");
		v4.getChildren().addAll(sada, sadaText);
		v4.setAlignment(Pos.CENTER);
		v4.setPadding(new Insets(insets));

		VBox v5 = new VBox();
		CheckBox andelicek = new CheckBox();
		andelicek.setDisable(true);
		andelicek.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tips[4] = TipSperku.ANDEL;

				} else {
					tips[4] = null;
				}

			}
		});
		Label andelicekText = new Label("Andělíček");
		v5.getChildren().addAll(andelicek, andelicekText);
		v5.setAlignment(Pos.CENTER);
		v5.setPadding(new Insets(insets));

		VBox v6 = new VBox();
		CheckBox ostatni = new CheckBox();
		ostatni.setDisable(true);
		ostatni.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					tips[5] = TipSperku.OSTATNI;

				} else {
					tips[5] = null;
				}

			}
		});
		Label ostatniText = new Label("Ostatní");
		v6.getChildren().addAll(ostatni, ostatniText);
		v6.setAlignment(Pos.CENTER);
		v6.setPadding(new Insets(insets));

		allTip.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					nausnice.setDisable(true);
					naramek.setDisable(true);
					retizek.setDisable(true);
					andelicek.setDisable(true);
					ostatni.setDisable(true);
					sada.setDisable(true);

					nausnice.setSelected(false);
					naramek.setSelected(false);
					retizek.setSelected(false);
					andelicek.setSelected(false);
					ostatni.setSelected(false);
					sada.setSelected(false);

				} else {

					nausnice.setDisable(false);
					naramek.setDisable(false);
					retizek.setDisable(false);
					andelicek.setDisable(false);
					ostatni.setDisable(false);
					sada.setDisable(false);

					nausnice.setSelected(false);
					naramek.setSelected(false);
					retizek.setSelected(false);
					andelicek.setSelected(false);
					ostatni.setSelected(false);
					sada.setSelected(false);

				}

			}
		});

		h.getChildren().addAll(v1, v2, v3, v4, v5, v6);
		tip.getChildren().addAll(leftLabel, h1, h);

		hbox.getChildren().addAll(left, right);

		// pocet kusu

		VBox storage = new VBox();
		HBox h2 = new HBox();
		HBox h3 = new HBox();
		Label allStorageText = new Label("Všechny bez ohledu na počet kusů");
		CheckBox allStorage = new CheckBox();
		allStorage.setSelected(true);
		h2.getChildren().addAll(allStorage, allStorageText);

		ToggleGroup group = new ToggleGroup();

		int[] storages = new int[2];
		storages[0] = -1;
		storages[1] = -1;
		VBox v20 = new VBox();
		RadioButton toInfinity = new RadioButton();
		Label infinityText = new Label("1ks a více");
		toInfinity.setToggleGroup(group);
		toInfinity.setSelected(true);
		toInfinity.setDisable(true);
		toInfinity.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {

					storages[0] = 1;
					storages[1] = Integer.MAX_VALUE;

				} else {

				}

			}
		});
		v20.getChildren().addAll(toInfinity, infinityText);
		v20.setPadding(new Insets(insets));
		// v20.setAlignment(Pos.CENTER);
		v20.setAlignment(Pos.TOP_CENTER);

		VBox v21 = new VBox();
		HBox pom = new HBox();
		RadioButton range = new RadioButton();
		Label rangeText = new Label("Rozmezí počtu kusů");
		range.setToggleGroup(group);
		range.setDisable(true);
		Label oddo = new Label("od - do");
		Label pomlcka = new Label(" - ");
		TextField od = new TextField();
		od.setPrefWidth(45);
		od.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					od.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					String text = od.getText();
					if (text.length() != 0)
						storages[0] = Integer.parseInt(text);
					else
						storages[0] = -1;
				}
			}
		});
		TextField do1 = new TextField();
		do1.setPrefWidth(45);

		do1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					do1.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					String text = do1.getText();
					if (text.length() != 0)
						storages[1] = Integer.parseInt(text);
					else
						storages[1] = -1;

				}

			}
		});

		range.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (arg2) {
					od.setDisable(false);
					do1.setDisable(false);
					od.setText("");
					do1.setText("");

				} else {
					od.setDisable(true);
					do1.setDisable(true);
				}

			}
		});
		od.setDisable(true);
		do1.setDisable(true);

		// pom.getChildren().addAll(oddo, od, pomlcka, do1);
		pom.getChildren().addAll(od, pomlcka, do1);

		v21.getChildren().addAll(range, rangeText, oddo, pom);
		v21.setPadding(new Insets(insets));
		v21.setAlignment(Pos.CENTER);

		allStorage.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					toInfinity.setDisable(true);
					range.setDisable(true);
					od.setDisable(true);
					do1.setDisable(true);

				} else {
					toInfinity.setDisable(false);
					range.setDisable(false);
					if (range.isSelected()) {
						od.setDisable(false);
						do1.setDisable(false);
					}

				}

			}
		});

		h3.getChildren().addAll(v20, v21);

		storage.getChildren().addAll(h2, h3);

		// cena

		ToggleGroup group2 = new ToggleGroup();
		VBox price = new VBox();
		HBox h4 = new HBox();
		HBox h5 = new HBox();
		Label AllpricesText = new Label("Všechny bez ohledu na cenu");
		CheckBox Allprices = new CheckBox();
		Allprices.setSelected(true);
		h4.getChildren().addAll(Allprices, AllpricesText);
		int prices[] = new int[2];
		prices[0] = -1;
		prices[1] = -1;

		VBox v30 = new VBox();
		Label priceFromText = new Label("Cena od");
		RadioButton priceFrom = new RadioButton();
		priceFrom.setSelected(true);
		TextField from1 = new TextField();
		from1.setPrefWidth(40);
		from1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					from1.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					String text = from1.getText();
					if (text.length() == 0) {
						prices[0] = -1;
						prices[1] = -1;
					} else {
						prices[0] = Integer.parseInt((text));
						prices[1] = Integer.MAX_VALUE;
					}
				}
			}
		});
		v30.getChildren().addAll(priceFrom, priceFromText, from1);
		v30.setAlignment(Pos.CENTER);
		v30.setPadding(new Insets(insets));

		VBox v31 = new VBox();
		Label priceToText = new Label("Cena do");
		RadioButton priceTo = new RadioButton();
		TextField to1 = new TextField();
		to1.setPrefWidth(40);
		to1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					to1.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					String text = to1.getText();
					if (text.length() == 0) {
						prices[0] = -1;
						prices[1] = -1;
					} else {
						prices[0] = Integer.parseInt((text));
						prices[1] = Integer.MAX_VALUE;
					}
				}
			}
		});
		v31.getChildren().addAll(priceTo, priceToText, to1);
		v31.setAlignment(Pos.CENTER);
		v31.setPadding(new Insets(insets));

		VBox v32 = new VBox();
		HBox h6 = new HBox();
		Label priceFromToText = new Label("Cena od do");
		RadioButton priceFromTo = new RadioButton();
		TextField from2 = new TextField();
		from2.setPrefWidth(40);
		from2.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					from2.setText(newValue.replaceAll("[^\\d]", ""));
				} else {
					String text = from2.getText();
					if (text.length() == 0) {
						prices[0] = -1;

					} else {
						prices[0] = Integer.parseInt((text));

					}

				}
			}
		});

		TextField to2 = new TextField();
		to2.setPrefWidth(40);
		to2.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					to2.setText(newValue.replaceAll("[^\\d]", ""));
				} else {

					String text = to2.getText();
					if (text.length() == 0) {

						prices[1] = -1;
					} else {
						prices[1] = Integer.parseInt((text));
					}
				}

			}
		});
		Label pomlcka2 = new Label(" - ");
		priceFrom.setDisable(true);
		priceFrom.setToggleGroup(group2);
		priceTo.setDisable(true);
		priceTo.setToggleGroup(group2);
		priceFromTo.setDisable(true);
		priceFromTo.setToggleGroup(group2);
		from1.setDisable(true);
		to1.setDisable(true);
		from2.setDisable(true);
		to2.setDisable(true);

		Allprices.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					priceFrom.setDisable(true);
					priceTo.setDisable(true);
					priceFromTo.setDisable(true);
					from1.setDisable(true);
					to1.setDisable(true);
					from2.setDisable(true);
					to2.setDisable(true);

				} else {
					priceFrom.setDisable(false);
					priceTo.setDisable(false);
					priceFromTo.setDisable(false);
					if (priceFrom.isSelected())
						from1.setDisable(false);
					if (priceTo.isSelected())
						to1.setDisable(false);
					if (priceFromTo.isSelected()) {
						from2.setDisable(false);
						to2.setDisable(false);
					}

				}

			}
		});

		priceFrom.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					from1.setDisable(false);
					from1.setText("");
					to1.setText("");
					to2.setText("");
					from2.setText("");
				} else
					from1.setDisable(true);

			}
		});

		priceTo.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					to1.setDisable(false);
					to1.setText("");
					from1.setText("");
					to2.setText("");
					from2.setText("");

				} else
					to1.setDisable(true);

			}
		});

		priceFromTo.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					from2.setDisable(false);
					to2.setDisable(false);
					from2.setText("");
					to2.setText("");
					to1.setText("");
					from1.setText("");

				} else {
					from2.setDisable(true);
					to2.setDisable(true);
				}

			}
		});

		h6.getChildren().addAll(from2, pomlcka2, to2);
		v32.getChildren().addAll(priceFromTo, priceFromToText, h6);
		v32.setAlignment(Pos.CENTER);
		v32.setPadding(new Insets(insets));

		h5.getChildren().addAll(v30, v31, v32);
		price.getChildren().addAll(h4, h5);

		price.setPadding(new Insets(insets2));
		tip.setPadding(new Insets(insets2));
		storage.setPadding(new Insets(insets2));
		left.getChildren().addAll(tip, storage, price);

		//// right

		Label rightLabel = new Label("Které informace o produktu zobrazit v PDF?");
		rightLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 15));
		rightLabel.setPadding(new Insets(insets2, insets2, insets2, insets));

		HBox h40 = new HBox();
		CheckBox showID = new CheckBox();
		showID.setSelected(true);
		Label showIDText = new Label("Zobrazit ID produktu");
		h40.getChildren().addAll(showID, showIDText);
		h40.setPadding(new Insets(insets));

		HBox h41 = new HBox();
		CheckBox showTip = new CheckBox();
		showTip.setSelected(true);
		Label showTipText = new Label("Zobrazit typ produktu");
		h41.getChildren().addAll(showTip, showTipText);
		h41.setPadding(new Insets(insets));

		HBox h42 = new HBox();
		CheckBox showPrice = new CheckBox();
		showPrice.setSelected(true);
		Label showPriceText = new Label("Zobrazit cenu");
		h42.getChildren().addAll(showPrice, showPriceText);
		h42.setPadding(new Insets(insets));

		HBox h43 = new HBox();
		CheckBox showStorage = new CheckBox();
		showStorage.setSelected(true);
		Label showStorageText = new Label("Zobrazit počet ks skladem");
		h43.getChildren().addAll(showStorage, showStorageText);
		h43.setPadding(new Insets(insets));

		VBox v44 = new VBox();
		HBox h44 = new HBox();
		HBox h443 = new HBox();
		CheckBox showAllPicture = new CheckBox();
		showAllPicture.setSelected(true);
		Label showAllPictureText = new Label("Zobrazit všechny fotky");

		h44.setPadding(new Insets(insets));
		h44.getChildren().addAll(showAllPicture, showAllPictureText);
		ToggleGroup group3 = new ToggleGroup();

		VBox h441 = new VBox();
		Label onlyMainText = new Label("Zobrazit jen hlavní fotku");
		RadioButton onlyMain = new RadioButton();
		onlyMain.setSelected(true);
		onlyMain.setToggleGroup(group3);
		onlyMain.setDisable(true);

		h441.getChildren().addAll(onlyMain, onlyMainText);
		h441.setPadding(new Insets(insets));
		h441.setAlignment(Pos.TOP_CENTER);

		VBox h442 = new VBox();
		Label countOfPictureText = new Label("Zobrazit konkrétní počet fotek");
		RadioButton countOfPicture = new RadioButton();
		countOfPicture.setToggleGroup(group3);
		countOfPicture.setDisable(true);
		TextField addCountOfPicture = new TextField();
		addCountOfPicture.setPrefWidth(40);

		addCountOfPicture.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					addCountOfPicture.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}
		});
		addCountOfPicture.setDisable(true);

		h442.getChildren().addAll(countOfPicture, countOfPictureText, addCountOfPicture);
		h442.setPadding(new Insets(insets));
		h442.setAlignment(Pos.CENTER);
		h443.setPadding(new Insets(insets, insets, insets, insets2));
		h443.getChildren().addAll(h441, h442);
		v44.getChildren().addAll(h44, h443);

		HBox h45 = new HBox();
		CheckBox showDescription = new CheckBox();
		showDescription.setSelected(true);
		Label showDescriptionText = new Label("Zobrazit popis produktu");
		h45.getChildren().addAll(showDescription, showDescriptionText);
		h45.setPadding(new Insets(insets));

		showAllPicture.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					onlyMain.setDisable(true);
					countOfPicture.setDisable(true);
					addCountOfPicture.setDisable(true);

				} else {
					onlyMain.setDisable(false);
					countOfPicture.setDisable(false);
					if (countOfPicture.isSelected()) {
						addCountOfPicture.setDisable(false);
					}

				}

			}
		});

		countOfPicture.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {

					addCountOfPicture.setDisable(false);

				} else {

					addCountOfPicture.setDisable(true);
				}

			}
		});

		right.getChildren().addAll(rightLabel, h40, h41, h42, h43, h45, v44);

		export.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Queue<Zaznam> selected = null;
				String text = addCountOfPicture.getText();
				int countPicture;
				if (text.length() == 0)
					countPicture = -1;
				else
					countPicture = Integer.parseInt(text);
				if (isChosenDataCorect(tips, prices, storages, countPicture, allTip.isSelected(),
						allStorage.isSelected(), Allprices.isSelected(), showAllPicture.isSelected(),
						countOfPicture.isSelected())) {
					if (onlyMain.isSelected())
						countPicture = -1;

					selected = chooseExportedData(zaznamy, tips, prices, storages, allTip.isSelected(),
							allStorage.isSelected(), Allprices.isSelected());

					if (!selected.isEmpty())
						PDFCreator.createPDF(selected, showID.isSelected(), showTip.isSelected(),
								showPrice.isSelected(), showStorage.isSelected(), showDescription.isSelected(),
								showAllPicture.isSelected(), countPicture);
					else {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Žádná data k vyexportování");
						alert.setHeaderText("Zadaným kritériím neodopovádají žádná data ");
						alert.setContentText(
								"Zadaným požadavkům neodpovídají žádná data, a proto je nelze vyexportovat");
						alert.showAndWait();

					}
				}

			}

		});

		main.setTop(buttons);
		main.setCenter(hbox);
		return main;
	}

	private boolean isChosenDataCorect(TipSperku[] tips, int[] prices, int[] storages, int countPicture, boolean allTip,
			boolean allStorage, boolean allprices, boolean allPicture, boolean countOfPicture) {

		if (!allTip) {
			boolean isNotChossen = true;
			for (int i = 0; i < tips.length; i++) {

				if (tips[i] != null)
					isNotChossen = false;

			}
			if (isNotChossen) {
				// alert empty type
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Není zvolen typ produktu");
				alert.setHeaderText("Musí být zvolen typ produktu");
				alert.setContentText("Aby bylo možné vytvořit PDF soubor, musí být zvolen alespoň jeden typ produktu");
				alert.showAndWait();

				return false;
			}

		}

		if (!allStorage) {

			if (storages[0] == -1 || storages[1] == -1) {
				// alert skladem nevyplneno pole
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Prázdné pole");
				alert.setHeaderText("Není vyplněné rozmezí počtu kusů");
				alert.setContentText(
						"Aby bylo možné vytvořit PDF soubor, musí být vyplněné rozmezí počtu kusů skladem");
				alert.showAndWait();
				return false;
			}

			if (storages[0] > storages[1]) {
				// alert nelogicky skladem

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Nelogická kritéria");
				alert.setHeaderText("Počet kusů \"od\" je větší než počet  kusů \"do\"");
				alert.setContentText(
						"Aby bylo možné vytvořit PDF soubor, musí být správně vyplněny pole s rozmezím počtu kusů skladem");
				alert.showAndWait();
				return false;
			}

		}

		if (!allprices) {

			if (prices[0] == -1 || prices[1] == -1) {
				// alert cena nevyplneno pole
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Prázdné pole");
				alert.setHeaderText("Není vyplněné pole s cenou");
				alert.setContentText("Aby bylo možné vytvořit PDF soubor, musí být vyplněné pole určující cenu");
				alert.showAndWait();
				return false;
			}

			if (prices[0] > prices[1]) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Nelogická kritéria");
				alert.setHeaderText("Cena \"od\" je větší než počet kusů \"do\"");
				alert.setContentText("Aby bylo možné vytvořit PDF soubor, musí být správně vyplněny pole s cenou");
				alert.showAndWait();
				return false;
			}

		}

		if (!allPicture && countOfPicture) {
			if (countPicture == -1) {
				// alert pocet fotek nevzplneno
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Prázdné pole");
				alert.setHeaderText("Není vyplněné pole s počet fotek");
				alert.setContentText("Aby bylo možné vytvořit PDF soubor, musí být zvolen počet fotek");
				alert.showAndWait();
				return false;
			}
		}

		return true;

	}

	private Queue<Zaznam> chooseExportedData(Queue<Zaznam> zaznamy, TipSperku[] tips, int[] prices, int[] storages,
			boolean tip, boolean storage, boolean price) {
		Iterator<Zaznam> it;

		if (tip && storage && price)
			return zaznamy;

		Queue<Zaznam> selected1;
		if (!tip) {
			selected1 = new LinkedList<Zaznam>();
			it = zaznamy.iterator();
			while (it.hasNext()) {
				Zaznam pom = it.next();
				for (int i = 0; i < tips.length; i++) {
					if (tips[i] != null && tips[i].equals(pom.getTip())) {
						selected1.add(pom);
						break;
					}
				}

			}

		} else
			selected1 = zaznamy;

		Queue<Zaznam> selected2;
		if (!price) {
			selected2 = new LinkedList<Zaznam>();
			it = selected1.iterator();
			while (it.hasNext()) {
				Zaznam pom = it.next();
				int cena = pom.getPravaCena();
				if (cena == 0)
					cena = pom.getPredpokladanaCena();
				if (prices[0] <= cena && cena <= prices[1]) {
					selected2.add(pom);
				}
			}

		} else
			selected2 = selected1;

		Queue<Zaznam> selected3;
		if (!storage) {
			selected3 = new LinkedList<Zaznam>();
			it = selected2.iterator();
			while (it.hasNext()) {
				Zaznam pom = it.next();

				if (storages[0] <= pom.getSkladem() && pom.getSkladem() <= storages[1]) {
					selected3.add(pom);
				}

			}
		} else
			selected3 = selected2;

		return selected3;
	}

	private void pridatNovyPScena() {
		primStage.setScene(new Scene(getRootNovy()));
	}

	private Parent getRootNovy() {
		BorderPane b = new BorderPane();
		Button zpet = new Button("Zpět");
		zpet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primStage.setScene(new Scene(getParrent()));
			}
		});
		zpet.setPadding(new Insets(10));

		GridPane gr = new GridPane();
		TextField id = new TextField();
		CheckBox ch = new CheckBox();
		ch.setSelected(true);
		Label chText = new Label("Automaticky určit ID");
		Label idText = new Label("ID: ");

		ch.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				changeIdEditable(newValue, id, idText);

			}
		});

		id.setDisable(true);

		id.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					id.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		Label nazevText = new Label("Název: *");
		TextField nazev = new TextField();

		Label predCenaText = new Label("Předpokládaná Cena: ");
		TextField predCena = new TextField();
		predCena.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					predCena.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		Label novaCenaText = new Label("Pravá Cena: ");
		TextField novaCena = new TextField();
		novaCena.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					novaCena.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		Label comboText = new Label("Tip: ");
		ComboBox<String> combo = new ComboBox<String>();
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.NAUSNICE));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.NARAMEK));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.RETIZEK));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.SADA));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.ANDEL));
		combo.getItems().add(TipSperku.getStringByEnum(TipSperku.OSTATNI));

		combo.getSelectionModel().selectFirst();

		Label popisText = new Label("Popis: ");
		TextArea popis = new TextArea();

		Label mainPhotoText = new Label("Vyber hlavní fotku: *");
		Label nameOfSelectedMain = new Label();
		VBox mainPhoto1 = new VBox();
		Button mainPhoto = new Button("Vyber");
		mainPhoto.setPadding(new Insets(20));

		mainPhoto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fl = new FileChooser();
				fl.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg", "*jpeg"));
				if (lastFile != null)
					if (lastFile.exists())
						fl.setInitialDirectory(lastFile);
				main = fl.showOpenDialog(primStage);

				if (main != null) {
					nameOfSelectedMain.setText(main.getName());
					lastFile = new File(getPath(main));
				}

			}
		});
		mainPhoto1.getChildren().addAll(mainPhoto, nameOfSelectedMain);

		Label otherPhotoText = new Label("Vyber ostatní fotky: ");
		Label chosenFilesOther = new Label();
		VBox otherPhoto1 = new VBox();
		Button otherPhoto = new Button("Vyber");
		otherPhoto.setPadding(new Insets(20));

		otherPhoto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fl = new FileChooser();
				fl.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg", "*jpeg"));
				if (lastFile != null)
					if (lastFile.exists())
						fl.setInitialDirectory(lastFile);
				other = fl.showOpenMultipleDialog(primStage);

				if (other != null && !other.isEmpty()) {
					chosenFilesOther.setText(getNameOfFiles(other));
					Iterator<File> it = other.iterator();
					lastFile = new File(getPath(it.next()));
				}
			}

		});
		otherPhoto1.getChildren().addAll(otherPhoto, chosenFilesOther);

		Label sklademText = new Label("Počet kusů: ");
		TextField skladem = new TextField();
		skladem.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					skladem.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		skladem.setPadding(new Insets(5));

		gr.add(chText, 0, 0);
		gr.add(ch, 1, 0);

		gr.add(idText, 0, 1);
		gr.add(id, 1, 1);

		gr.add(nazevText, 0, 2);
		gr.add(nazev, 1, 2);

		gr.add(comboText, 0, 3);
		gr.add(combo, 1, 3);

		gr.add(predCenaText, 0, 4);
		gr.add(predCena, 1, 4);

		gr.add(novaCenaText, 0, 5);
		gr.add(novaCena, 1, 5);

		gr.add(popisText, 0, 7);
		gr.add(popis, 1, 7);

		gr.add(sklademText, 0, 6);
		gr.add(skladem, 1, 6);

		gr.add(mainPhotoText, 0, 8);
		gr.add(mainPhoto1, 1, 8);

		gr.add(otherPhotoText, 0, 9);
		gr.add(otherPhoto1, 1, 9);

		gr.setPadding(new Insets(30));

		Button uloz = new Button("Uložit");
		uloz.setPadding(new Insets(10));

		uloz.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int id1 = createId();
				if (!ch.isSelected() && id.getText().equals("") || nazev.getText().equals("") || main == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Chybí povinné údaje");
					alert.setHeaderText("Některý z povinných udajů chybí");
					alert.setContentText(
							"Musí být vyplněno alespoň název, id (pokud není generováno samo), a hlavní fotka");
					alert.showAndWait();

					return;
				}
				if (ch.isSelected())
					id1 = createId();
				else
					id1 = Integer.parseInt(id.getText());

				if (checkId(id1))
					addnewIthem(id1, nazev, popis, predCena, novaCena, combo, skladem);
				else {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Kolize ID");
					alert.setHeaderText("Stejná ID " + id1);
					alert.setContentText("Zadané ID již existuje. Nelze mít dvě stejná id. Prosím zvolte jiné ID");
					alert.showAndWait();
				}

			}
		});

		b.setLeft(zpet);
		b.setRight(uloz);

		VBox box = new VBox();
		box.getChildren().addAll(b, gr);

		return box;
	}

	private String getNameOfFiles(List<File> other) {
		String name = "";

		for (File file : other) {

			name = name + " " + file.getName();

		}
		return name;
	}

	private boolean checkId(int id1) {
		Iterator<Zaznam> it = zaznamy.iterator();
		Zaznam pom = null;
		while (it.hasNext()) {
			pom = (Zaznam) it.next();
			if (pom.getId() == id1)
				return false;
		}
		return true;
	}

	private int createId() {
		Iterator<Zaznam> it = null;
		Zaznam pom = null;
		int id = -1;
		boolean isUniq;

		do {
			isUniq = true;
			it = zaznamy.iterator();
			id++;
			while (it.hasNext()) {

				pom = (Zaznam) it.next();
				if (pom.getId() == id) {
					isUniq = false;
					continue;
				}

			}

		} while (!isUniq);

		return id;
	}

	private void addnewIthem(int id, TextField nazev, TextArea popis, TextField predCena, TextField novaCena,
			ComboBox<String> tip, TextField skladem) {
		addPicture(id);

		int predCena1;

		if (predCena.getText().equals(""))
			predCena1 = 0;
		else
			predCena1 = Integer.parseInt(predCena.getText());

		int novaCena1;

		if (novaCena.getText().equals(""))
			novaCena1 = 0;
		else
			novaCena1 = Integer.parseInt(novaCena.getText());

		String popis1;
		if (popis.getText().equals(""))
			popis1 = " ";
		else
			popis1 = popis.getText();
		int skladem1;
		if (skladem.getText().equals(""))
			skladem1 = 0;
		else
			skladem1 = Integer.parseInt(skladem.getText());

		Zaznam zam = new Zaznam(id, nazev.getText(), popis1, predCena1, novaCena1,
				TipSperku.getEnumByString((String) tip.getSelectionModel().getSelectedItem()), skladem1);

		main = null;
		other = null;

		writeOneNewIthemToFile(zam);
	}

	private void addPicture(int id) {

		new File(path + File.separator + id).mkdir();

		File temp1 = new File(path + File.separator + id + File.separator + nameOfMainPicture);

		try {
			Files.copy(Paths.get(main.getAbsolutePath()), Paths.get(temp1.getAbsolutePath()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			createExceptionAlert("Neznámá chyba při kopírování fotky, chyba číslo 6", e);
		}

		if (other == null)
			return;

		for (File file : other) {
			try {
				Files.copy(file.toPath(),
						(new File(path + File.separator + id + File.separator + file.getName())).toPath(),
						StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException e) {

				createExceptionAlert("Chyba při kopírování souborů, chyba číslo 7", e);

			}
		}

	}

	private String getPath(File main) {

		String a = main.getAbsolutePath();
		int i = a.length() - 1;
		while (true) {

			if (a.charAt(i) == File.separator.charAt(0)) {
				break;
			}
			i--;

		}

		String result = main.getAbsolutePath().substring(0, i);
		return result;
	}

	private void writeOneNewIthemToFile(Zaznam zam) {

		try {
			File file = new File(path + File.separator + nameOfFile);

			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
			bw.write(zam.getTextToWrite());
			bw.close();
		} catch (IOException e) {
			createExceptionAlert("Nespecifikovaná chyba při vkládání nové položky, chyba číslo 9", e);
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Položka vložena");
		alert.setHeaderText("Položka uložena");
		alert.setContentText("Úspěšné uložení nové položky");
		fillQueue();
		pridatNovyPScena();
		alert.showAndWait();

	}

	private void changeIdEditable(Boolean newValue, TextField id, Label popis) {
		if (newValue) {
			id.setDisable(true);
			popis.setText(popis.getText().substring(0, popis.getText().length() - 1));
		} else {

			id.setDisable(false);
			popis.setText(popis.getText() + "*");
		}

	}

}
