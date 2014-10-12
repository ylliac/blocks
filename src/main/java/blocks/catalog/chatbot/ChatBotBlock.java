package blocks.catalog.chatbot;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.catalog.chatbot.rivescript.RiveScript;
import blocks.core.BlockSupport;

public class ChatBotBlock extends BlockSupport {

	@Override
	protected void initialize() {
		inConfigurationFile = new EmptyObserver<String>() {
			@Override
			public void onNext(String fileName) {
				bot.loadFile(fileName);
				bot.sortReplies();
			}
		};

		in = PublishSubject.create();

		out = in.map(new Func1<String, String>() {
			public String call(String input) {
				
				// Give it to the bot and get the answer
				String reply = bot.reply("localuser", input.toLowerCase());
				return reply;
			}
		});
	}

	public Observer<String> getInConfigurationFile() {
		return inConfigurationFile;
	}

	public PublishSubject<String> getIn() {
		return in;
	}

	public Observable<String> getOut() {
		return out;
	}

	private Observer<String> inConfigurationFile;
	private PublishSubject<String> in;
	private Observable<String> out;

	private RiveScript bot = new RiveScript(false);

}
