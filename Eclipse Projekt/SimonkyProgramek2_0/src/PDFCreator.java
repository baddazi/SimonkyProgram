import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class PDFCreator {
	private static PDDocument doc;
	private static PDType0Font font = null;
	private static int positonYInPage = 740;
	private static String path = "Files" + File.separator;
	private static String pdfName = "šperky.pdf";
	private static ProgressBar progress;
	private static PDPage page;
	private static int sizeOfLineX = 95;
	private static int sizeOfLineY = 15;
	private static PDPageContentStream contentStream = null;
	private static boolean cancel;
	private static int position = 1;
	private static LocalDateTime now  = null;
	private static  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  

	private static float addElementToPdf(Zaznam pom, int count, int position, float lastY, Label label, boolean showID,
			boolean showTypOfPrduct, boolean price, boolean storage, boolean description, boolean allPhoto,
			int countPicture) {

		float y;
		boolean isFit = isFit(lastY, pom);
		if (isFit && lastY != 0) {
			y = lastY;
		} else {
			y = 740;
			page = new PDPage();
		}

		String path2 = path + pom.getId() + File.separator + "main.jpg";

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				double procento = (double) position / count;
				progress.setProgress(procento);
				label.setText(" " + ((int) (procento * 100)) + "%");
			}
		});

		try {

			contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true);
			if (y == 740) {
				contentStream.beginText();
				contentStream.setFont(font, 7);
				contentStream.newLineAtOffset(450, 35);
				
				
				contentStream.showText("Vytvořeno: " + dtf.format(now));
				contentStream.endText();
			}

			BufferedImage bufferGambar = ImageIO.read(new File(path2));
			int h = bufferGambar.getHeight();
			int w = bufferGambar.getWidth();

			ImageIO.write(bufferGambar, "jpg", new File(path2));
			PDImageXObject pdImage = PDImageXObject.createFromFile(path2, doc);

			int sizeOfName = 50;
			int size = 150;
			float xBasicInfo = 220;
			int cena = pom.getPravaCena();
			if (cena == 0)
				cena = pom.getPredpokladanaCena();
			float newH = size;
			float newW = size;
			float basicY = y;
			if (h > w) {
				newW = (float) w / h * size;
				xBasicInfo = newW + 30 + 40;
				basicY = basicY - size + 10;
				contentStream.drawImage(pdImage, 40, basicY, newW, size);
			} else {
				newH = (float) h / w * size;
				basicY = basicY - newH + 10;
				contentStream.drawImage(pdImage, 40, basicY, size, newH);
			}

			contentStream.beginText();
			contentStream.setFont(font, 14);
			contentStream.setLeading(20);
			contentStream.newLineAtOffset(xBasicInfo, y);

			if (pom.getNazev().length() > sizeOfName) {
				String nazev = "Název:     " + pom.getNazev();
				int lastSpace = 0;
				int k = nazev.length() / sizeOfName;

				for (int i = 0; i < k; i++) {
					lastSpace = lastSpace(nazev, sizeOfName);
					String nazevFirst = nazev.substring(0, lastSpace);
					contentStream.showText(nazevFirst);
					contentStream.newLine();
					nazev = "                " + nazev.substring(lastSpace);
				}

				contentStream.showText(nazev);
				contentStream.newLine();

			} else {
				String nazev = "Název:     " + pom.getNazev();
				contentStream.showText(nazev);
				contentStream.newLine();
			}

			if (showID) {
				contentStream.showText("ID:            " + pom.getId());
				contentStream.newLine();
			}
			if (showTypOfPrduct) {
				contentStream.showText("Typ:         " + TipSperku.getStringByEnum(pom.getTip()));
				contentStream.newLine();
			}
			if (price) {
				contentStream.showText("Cena:       " + cena + " Kč");
				contentStream.newLine();
			}
			if (storage) {
				contentStream.showText("Skladem: " + pom.getSkladem() + " Ks");
				contentStream.newLine();
			}

			contentStream.endText();
			contentStream.beginText();
			contentStream.setFont(font, 12);
			contentStream.setLeading(sizeOfLineY);
			int k = 0;
			y = y - newH;

			if (description && pom.getPopis().length() != 0 && !pom.getPopis().equals(" ")) {

				contentStream.newLineAtOffset(40, y);

				String popis = " " + pom.getPopis().replace("\n", "");

				int sizeOfLine = sizeOfLineX;

				if (popis.length() > sizeOfLine) {

					int lastSpace = 0;
					k = popis.length() / sizeOfLine;

					for (int i = 0; i < k; i++) {
						lastSpace = lastSpace(popis, sizeOfLine);
						String nazevFirst = popis.substring(0, lastSpace);
						contentStream.showText(nazevFirst);
						contentStream.newLine();
						popis = popis.substring(lastSpace);
					}

					contentStream.showText(popis);
					contentStream.newLine();

				} else {
					k = 1;
					contentStream.showText(popis);
					contentStream.newLine();
				}
			}

			contentStream.endText();

			if (pom.getPopis().length() != 0 && description) {
				y = y - k * 20;
			}

			File[] listOfFiles = new File(path + pom.getId()).listFiles();
			if ((allPhoto || countPicture != -1) && listOfFiles.length > 1) {
				y = y - 100 - 20;
				float x = 40;
				size = 100;
				int i = 0;

				for (File file : listOfFiles) {

					if (i == countPicture && !allPhoto)
						break;

					newW = size;

					if (file.getName().equals("main.jpg"))
						continue;

					BufferedImage bG = ImageIO.read(file);

					h = bG.getHeight();
					w = bG.getWidth();

					ImageIO.write(bG, "jpg", file);
					PDImageXObject image = PDImageXObject.createFromFile(file.getAbsolutePath(), doc);

					if (h > w) {
						newW = (float) w / h * size;

						contentStream.drawImage(image, x, y, newW, size);
					} else {
						newH = (float) h / w * size;
						contentStream.drawImage(image, x, y, size, newH);
					}

					x = x + newW + 20;
					if (x > 560) {
						x = 40;
						y = y - size - 20;
					}
					i++;

				}

			}
			y = y - 80;
			if (!isFit) {
				doc.addPage(page);

			}
			contentStream.close();
		} catch (IOException e) {

			e.printStackTrace();

		}

		return y;

	}

	private static boolean isFit(float lastY, Zaznam z) {

		float y = 200;
		int sizeOfLineX = 95;
		int k = z.getPopis().length() / sizeOfLineX;
		y = y + k * sizeOfLineY;

		float x = 40;
		int size = 100;
		float newW = size;
		float newH = size;

		File[] listOfFiles = new File(path + z.getId()).listFiles();
		for (File file : listOfFiles) {
			newW = size;

			BufferedImage bG = null;
			try {
				bG = ImageIO.read(file);
			} catch (IOException e) {

				e.printStackTrace();
			}
			int h = bG.getHeight();
			int w = bG.getWidth();

			if (h > w) {
				newW = (float) w / h * size;

			} else {
				newH = (float) h / w * size;

			}

			x = x + newW + 20;
			if (x > 560) {
				x = 40;
				y = y + size + 20;
			}

		}

		y = y + 20;

		if (y <= lastY - 60)
			return true;

		return false;
	}

	public static void createPDF(Queue<Zaznam> zaznamy, boolean showID, boolean showTypOfPrduct, boolean price,
			boolean storage, boolean description, boolean allPhoto, int countPicture) {

		Label label = new Label(" 0%");
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Exportuji");
		alert.setHeaderText("Vyčkejte, právě probíhá export");
		alert.setContentText("Tato akce může trvat v řádu vteřin až minut v závislosti na velikosti exportovaných dat");
		doc = new PDDocument();
		alert.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> cancel = true);
		Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (ok.getText().equals("Zrušit"))
					cancel = true;
			}

		});
		ok.setText("Zrušit");

		HBox box = new HBox();
		cancel = false;

		progress = new ProgressBar();
		progress.setProgress(-1F);
		box.getChildren().addAll(progress, label);

		alert.setGraphic(box);

		alert.show();
		if(now==null)
		  now = LocalDateTime.now();  
		
		try {

			File f = new File(path + "Font" + File.separator + "OpenSans-Regular.ttf");
			font = PDType0Font.load(doc, f);

		} catch (IOException e1) {
			e1.printStackTrace();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					alert.setHeaderText(e1.toString());
				}
			});
		}

		Thread t = new Thread() {

			public void run() {
				float y = 0;
				Iterator<Zaznam> it = zaznamy.iterator();
				int count = 0;
				position = 1;
				while (it.hasNext()) {
					count++;
					it.next();
				}

				it = zaznamy.iterator();
				while (it.hasNext()) {

					y = addElementToPdf(it.next(), count, position, y, label, showID, showTypOfPrduct, price, storage,
							description, allPhoto, countPicture);
					position++;

					if (cancel)
						break;

				}

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ok.setText("OK");
						alert.setTitle("Vyexportováno");
						alert.setHeaderText("Export dat proběhl úspěšně");
						alert.setContentText("Vyexportovaná data lze nalézt v souboru " + pdfName);
					}
				});

				try {
					doc.save(pdfName);
					doc.close();
					now=null;
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		};

		t.start();

	}

	private static int lastSpace(String nazev, int position) {
		int pos = position;
		while (nazev.charAt(pos) != ' ') {
			pos--;
		}
		return pos;
	}

}
