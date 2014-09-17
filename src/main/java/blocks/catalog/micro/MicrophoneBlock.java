package blocks.catalog.micro;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;

import org.apache.log4j.Logger;

import rx.Observer;

import blocks.catalog.micro.Microphone.CaptureState;
import blocks.core.Block;

public class MicrophoneBlock extends Block {

	private static final int TIME_AFTER_SPEECH = 2000;

	private static final Logger LOGGER = Logger
			.getLogger(MicrophoneBlock.class);

	@Override
	protected void initialize() {

	}

	public Observer<Object> getInTick() {
		return inTick;
	}

	public Observer<Integer> getInThreshold() {
		return inThreshold;
	}

	private Observer<Object> inTick;
	private Observer<Integer> inThreshold;

	private MicrophoneAnalyzer micro = new MicrophoneAnalyzer(
			AudioFileFormat.Type.WAVE);

}
