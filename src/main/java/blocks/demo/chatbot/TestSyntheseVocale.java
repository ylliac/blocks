package blocks.demo.chatbot;

import t2s.son.LecteurTexte;

public class TestSyntheseVocale {

	public static void main(String[] args) {
		final LecteurTexte lecteur = new LecteurTexte();
		lecteur.setTexte("Bonjour. Je suis un test de synthèse vocale. Quel est votre nom ?");
		lecteur.playAll();
	}

}
