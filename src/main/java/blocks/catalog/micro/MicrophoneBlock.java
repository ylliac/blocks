package blocks.catalog.micro;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.EmptyObserver;
import rx.schedulers.Schedulers;
import blocks.core.Block;

public class MicrophoneBlock extends Block {

	private static final int TIME_AFTER_SPEECH = 2000;

	private static final Logger LOGGER = Logger
			.getLogger(MicrophoneBlock.class);

	/**
	 * Calculates the volume of AudioData which may be buffered data from a
	 * data-line.
	 * 
	 * @param audioData
	 *            The byte[] you want to determine the volume of
	 * @return the calculated volume of audioData
	 */
	public static int calculateRMSLevel(byte[] audioData) {

		// This only works for sampleSizeInBits = 8 in the AudioFormat.
		// Extract from
		// http://stackoverflow.com/questions/11540094/sound-level-rms :
		// "The sample rate doesn't matter, but the bit depth, endianness, and,
		// in a different way, number of channels, do matter.
		// To see why, you must simply notice that the function in question
		// takes a byte array as an argument
		// and processes each value from that array individually. The byte
		// datatype is an 8-bit value.
		// If you want something that works with 16-bit values, you need to use
		// a different datatype (short) or convert to that from bytes.
		// Once you do that, you will still get different values for 16 bits vs
		// 8 bit because the range is different:
		// 8 bit goes from -128 to +127 and 16 bit goes from -32768 to +32767,
		// but they are both measuring the same thing,
		// meaning they scaling the same real-word values to different
		// represented values.
		// As for sound-levels and their relationship to time.... well it
		// depends on your sample rate and the size
		// of the arrays going into this function.
		// For example, if your samplerate is 8kHz and you have 2048 samples per
		// buffer,
		// then your function is going to be called 8000/2048 or about 3.9 times
		// per second,
		// meaning your results are coming in at that rate (every 256
		// milliseconds).

		// 8 bits version
		// long lSum = 0;
		// for (int i = 0; i < audioData.length; i++){
		// lSum = lSum + audioData[i];
		// }
		//
		// double dAvg = lSum / audioData.length;
		//
		// double sumMeanSquare = 0d;
		// for (int j = 0; j < audioData.length; j++){
		// sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);
		// }
		//
		// double averageMeanSquare = sumMeanSquare / audioData.length;
		// return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);

		// TODO ACY Choisir l'une ou l'autre des versions selon le format

		// 16 bits version
		short[] shorts = new short[audioData.length / 2];
		ByteBuffer.wrap(audioData).order(ByteOrder.LITTLE_ENDIAN)
				.asShortBuffer().get(shorts);

		double[] values = new double[shorts.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = shorts[i] * 100. / (double) Short.MAX_VALUE;
		}

		double sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum = sum + values[i];
		}

		double dAvg = sum / values.length;

		double sumMeanSquare = 0d;
		for (int j = 0; j < values.length; j++) {
			sumMeanSquare = sumMeanSquare + Math.pow(values[j] - dAvg, 2d);
		}

		double averageMeanSquare = sumMeanSquare / values.length;
		return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
	}

	@Override
	protected void initialize() {

		inThreshold = new EmptyObserver<Integer>() {
			@Override
			public void onNext(Integer threshold) {
				MicrophoneBlock.this.threshold = threshold;
			}
		};

		// TODO ACY utiliser inTick pour faire un throttle sur le flux ?

		final AudioFormat audioFormat = getAudioFormat();
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class,
				audioFormat);
		TargetDataLine line = null;
		try {
			line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			line.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		}

		line.start();

		final AudioInputStream stream = new AudioInputStream(line);

		// Listen the audio flow
		final Observable<byte[]> audioFlow = Observable
				.create(new OnSubscribe<byte[]>() {
					public void call(Subscriber<? super byte[]> subscriber) {
						byte[] buffer = new byte[10 * audioFormat
								.getFrameSize()];

						try {
							while (stream.read(buffer) != -1) {
								subscriber.onNext(buffer);
							}
						} catch (IOException e) {
							subscriber.onError(e);
						}

						subscriber.onCompleted();
					}
				});

		// Filter when volume is too low
		final Observable<byte[]> filteredAudioFlow = audioFlow
				.filter(new Func1<byte[], Boolean>() {
					public Boolean call(byte[] audioData) {
						int volume = calculateRMSLevel(audioData);
						return volume > threshold;
					}
				});

		// Buffer
		out = filteredAudioFlow.buffer(new Func0<Observable<Integer>>() {
			public Observable<Integer> call() {
				return audioFlow.subscribeOn(Schedulers.computation())
						.map(new Func1<byte[], Integer>() {
							public Integer call(byte[] audioData) {
								return calculateRMSLevel(audioData);
							}
						}).buffer(1, TimeUnit.SECONDS)
						.map(new Func1<List<Integer>, Integer>() {

							public Integer call(List<Integer> volumes) {
								int avgVolume = 0;

								for (Integer volume : volumes) {
									avgVolume += volume;
								}
								avgVolume /= (double) volumes.size();

								return avgVolume;
							}

						}).filter(new Func1<Integer, Boolean>() {
							public Boolean call(Integer avgVolume) {
								return avgVolume < threshold;
							}
						});
			}
		}).filter(new Func1<List<byte[]>, Boolean>() {
			public Boolean call(List<byte[]> list) {
				return !list.isEmpty();
			}
		}).map(new Func1<List<byte[]>, byte[]>() {
			public byte[] call(List<byte[]> list) {

				byte[] result = new byte[0];

				for (byte[] data : list) {
					byte[] newResult = new byte[result.length + data.length];
					System.arraycopy(result, 0, newResult, 0, result.length);
					System.arraycopy(data, 0, newResult, result.length,
							data.length);
					result = newResult;
				}

				return result;
			}
		}).doOnEach(new EmptyObserver<byte[]>() {
			@Override
			public void onNext(byte[] audioData) {
				System.out.println("Recorded size: " + audioData.length);
			}
		}).subscribeOn(Schedulers.from(Executors.newFixedThreadPool(4)));

		// TODO ACY a mettre a la fin
		// getTargetDataLine().stop();
		// getTargetDataLine().close();
	}

	public Observer<Object> getInTick() {
		return inTick;
	}

	public Observer<Integer> getInThreshold() {
		return inThreshold;
	}

	public Observable<byte[]> getOut() {
		return out;
	}

	public AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	private Observer<Object> inTick;
	private Observer<Integer> inThreshold;

	private Observable<byte[]> out;

	private int threshold = 1;

}
