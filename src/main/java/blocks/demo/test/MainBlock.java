package blocks.demo.test;

import rx.Observable;
import blocks.catalog.audioconversion.WavToFlacBlock;
import blocks.catalog.chatbot.ChatBotBlock;
import blocks.catalog.console.ConsoleBlock;
import blocks.catalog.micro.MicrophoneBlock;
import blocks.catalog.speechrecognition.SpeechRecognitionBlock;
import blocks.catalog.translate.TranslateBlock;
import blocks.catalog.tts.TextToSpeechBlock;

public class MainBlock {

	public static void main(String[] args) {
		ConsoleBlock console = new ConsoleBlock();
		TranslateBlock translate = new TranslateBlock();
		TextToSpeechBlock tts = new TextToSpeechBlock();
		ChatBotBlock chatbot = new ChatBotBlock();
//		MicrophoneBlock micro = new MicrophoneBlock();
//		WavToFlacBlock wavToFlac = new WavToFlacBlock();
//		blocks.catalog.speechrecognition.deprecated.SpeechRecognitionBlock deprecatedSpeechRecognition = new blocks.catalog.speechrecognition.deprecated.SpeechRecognitionBlock();
		
//		SpeechRecognitionBlock speechRecognition = new SpeechRecognitionBlock();
		
//		micro.getOutAudioFormat().subscribe(wavToFlac.getInAudioFormat());
//		micro.getOut().subscribe(wavToFlac.getIn());
//		wavToFlac.getOut().subscribe(deprecatedSpeechRecognition.getIn());
//		deprecatedSpeechRecognition.getOut().subscribe(translate.getIn());
//		deprecatedSpeechRecognition.getOut().subscribe(tts.getIn());
		
		translate.getOut().subscribe(chatbot.getIn());
		Observable.just("conf/chatbot/brain.rs").subscribe(
				chatbot.getInConfigurationFile());
		chatbot.getOut().subscribe(console.getInMessage());
		chatbot.getOut().subscribe(tts.getIn());
		Observable.just("Qui es tu ?").subscribe(translate.getIn());
	}
}
