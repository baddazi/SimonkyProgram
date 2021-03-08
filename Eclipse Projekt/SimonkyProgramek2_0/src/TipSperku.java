
public enum TipSperku {

	NAUSNICE, NARAMEK, RETIZEK, SADA, ANDEL, OSTATNI, VSE;

	public static String getStringByEnum(TipSperku tip) {

		switch (tip) {
		case NAUSNICE:
			return "Náušnice";
		case NARAMEK:
			return "Náramek";
		case RETIZEK:
			return "Řetízek";
		case SADA:
			return "Sada";
		case ANDEL:
			return "Andělíček";
		case OSTATNI:
			return "Ostatní";
		case VSE:
			return "Vše";

		}

		return "";
	}

	public static TipSperku getEnumByString(String tip) {

		switch (tip) {
		case "Náušnice":
			return NAUSNICE;
		case "Náramek":
			return NARAMEK;
		case "Řetízek":
			return RETIZEK;
		case "Sada":
			return SADA;
		case "Andělíček":
			return ANDEL;
		case "Ostatní":
			return OSTATNI;
		case "Vše":
			return VSE;

		}

		return TipSperku.VSE;
	}
}
