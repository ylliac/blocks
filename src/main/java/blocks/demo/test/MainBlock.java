package blocks.demo.test;

import rx.Observable;
import blocks.catalog.chatbot.ChatBotBlock;
import blocks.catalog.console.ConsoleBlock;
import blocks.catalog.translate.TranslateBlock;
import blocks.catalog.tts.TextToSpeechBlock;

public class MainBlock {

	public static void main(String[] args) {
		ConsoleBlock console = new ConsoleBlock();
		TranslateBlock translate = new TranslateBlock();
		TextToSpeechBlock tts = new TextToSpeechBlock();
		ChatBotBlock chatbot = new ChatBotBlock();

		translate.getOut().subscribe(chatbot.getIn());
		Observable.just("conf/chatbot/brain.rs").subscribe(
				chatbot.getInConfigurationFile());
		chatbot.getOut().subscribe(console.getInMessage());
		chatbot.getOut().subscribe(tts.getIn());
		Observable.just("Qui es tu ?").subscribe(translate.getIn());
	}
}
