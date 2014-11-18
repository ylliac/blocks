package blocks.demo.chatbot;

import rx.Observable;
import blocks.catalog.chatbot.ChatBotBlock;
import blocks.catalog.console.ConsoleBlock;
import blocks.catalog.speechrecognition.SpeechRecognitionBlock;
import blocks.catalog.translate.TranslateOnlineBlock;
import blocks.catalog.tts.TextToSpeechOfflineBlock;
import blocks.catalog.tts.TextToSpeechOnlineBlock;
import blocks.util.Language;

public class DemoChatbot {

	public static void main(String[] args) {
		DemoChatbot demo = new DemoChatbot();
		// demo.textToSpeechOnline();
		// demo.textToSpeechOffline();
		// demo.textToTranslateOnlineToBotToSpeechOnline();
		demo.speechRecognition();
	}

	// TEXT -> SPEECH ONLINE
	public void textToSpeechOnline() {
		TextToSpeechOnlineBlock tts = new TextToSpeechOnlineBlock();

		tts.getInLanguage().onNext(Language.FRENCH);
		Observable.just("Qui es tu ?").subscribe(tts.getIn());
	}

	// TEXT -> SPEECH OFFLINE
	public void textToSpeechOffline() {
		TextToSpeechOfflineBlock tts = new TextToSpeechOfflineBlock();

		Observable.just("Qui es tu ?").subscribe(tts.getIn());
	}

	// TEXT -> TRANSLATE ONLINE -> BOT -> SPEECH ONLINE
	public void textToTranslateOnlineToBotToSpeechOnline() {
		ConsoleBlock console = new ConsoleBlock();
		TranslateOnlineBlock translate = new TranslateOnlineBlock();
		TextToSpeechOnlineBlock tts = new TextToSpeechOnlineBlock();
		ChatBotBlock chatbot = new ChatBotBlock();

		translate.getOut().subscribe(chatbot.getIn());
		Observable.just("conf/chatbot/brain.rs").subscribe(
				chatbot.getInConfigurationFile());
		chatbot.getOut().subscribe(console.getInMessage());
		chatbot.getOut().subscribe(tts.getIn());
		Observable.just("Qui es tu ?").subscribe(translate.getIn());
	}

	// TODO TEST SPEECH
	public void speechRecognition() {
		// TODO
		SpeechRecognitionBlock speech = new SpeechRecognitionBlock();
	}
}
