package blocks.demo.properties;

public class DemoProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DemoProperties demo = new DemoProperties();
		demo.demo();
	}

	//ACY 31/12/2014 Faire une d�mo pr�sentant :
	// - un champ de recherche avec bouton de validation
	// - une liste de r�sultat
	// - une liste de valeurs sources
	// Un traitement prend en entr�e le champ de recherche et les valeurs sources et renvoie
	// les �l�ments des valeurs sources contenant la recherche
	// Si on modifie le champ de recherche --> les r�sultats sont mis � jour
	// Si on modifie les valeurs sources --> les r�sultats sont mis � jour
	// TODO : ajouter cette liste de valeurs sources et la rendre modifiable
	
	public void demo() {
		DemoFrame frame = new DemoFrame();
		SearchBlock searchBlock = new SearchBlock();

		frame.searchProperty().sendTo(searchBlock.getIn());
		frame.resultsProperty().listen(searchBlock.getOut());

		frame.setVisible(true);
	}

}
