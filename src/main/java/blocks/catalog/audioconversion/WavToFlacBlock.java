package blocks.catalog.audioconversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javaFlacEncoder.FLACEncoder;
import javaFlacEncoder.FLACOutputStream;
import javaFlacEncoder.FLACStreamOutputStream;
import javaFlacEncoder.StreamConfiguration;

import javax.sound.sampled.AudioFormat;

import rx.Observable;
import rx.Observer;
import rx.exceptions.OnErrorFailedException;
import rx.functions.Func1;
import rx.observers.EmptyObserver;
import rx.subjects.PublishSubject;
import blocks.core.BlockSupport;

public class WavToFlacBlock extends BlockSupport {

	@Override
	protected void initialize() {

		inAudioFormat = new EmptyObserver<AudioFormat>() {
			@Override
			public void onNext(AudioFormat audioFormat) {
				WavToFlacBlock.this.audioFormat = audioFormat;
			}
		};

		in = PublishSubject.create();

		out = in.map(new Func1<byte[], byte[]>() {
			public byte[] call(byte[] input) {
				try {					
					byte[] result = convertWavToFlac(input); 
					
					//TODO ACY
					System.out.println("flac : " + result.length);
					
					return result;
				} catch (IOException e) {
					throw new OnErrorFailedException(e);
				}
			}
		});
	}

	private byte[] convertWavToFlac(byte[] input) throws IOException {

		FLACEncoder flacEncoder = new FLACEncoder();

		StreamConfiguration streamConfiguration = new StreamConfiguration();
		streamConfiguration.setSampleRate(8000);
		streamConfiguration.setBitsPerSample(16);
		streamConfiguration.setChannelCount(1);
		flacEncoder.setStreamConfiguration(streamConfiguration);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		FLACOutputStream flacOutputStream = new FLACStreamOutputStream(output);
		flacEncoder.setOutputStream(flacOutputStream);

		flacEncoder.openFLACStream();

		int frameSize = audioFormat.getFrameSize();
		byte[] samplesIn = new byte[frameSize];

		int[] sampleData = new int[(int) input.length / frameSize];

		for (int i = 0; i < sampleData.length; i++) {
			if (frameSize != 1) {
				ByteBuffer bb = ByteBuffer.wrap(samplesIn);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				short shortVal = bb.getShort();
				sampleData[i] = shortVal;
			} else {
				sampleData[i] = samplesIn[0];
			}
		}

		flacEncoder.addSamples(sampleData, sampleData.length);
		flacEncoder.encodeSamples(sampleData.length, false);
		flacEncoder.encodeSamples(flacEncoder.samplesAvailableToEncode(), true);

		output.close();

		return output.toByteArray();
	}

	public Observer<AudioFormat> getInAudioFormat() {
		return inAudioFormat;
	}

	public Observer<byte[]> getIn() {
		return in;
	}

	public Observable<byte[]> getOut() {
		return out;
	}

	private Observer<AudioFormat> inAudioFormat;
	private AudioFormat audioFormat = null;

	private PublishSubject<byte[]> in;
	private Observable<byte[]> out;

}
