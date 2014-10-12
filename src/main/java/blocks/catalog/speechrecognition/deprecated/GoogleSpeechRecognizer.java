package blocks.catalog.speechrecognition.deprecated;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import blocks.util.Language;
import blocks.util.Variant;

//From https://github.com/The-Shadow/java-speech-api

public class GoogleSpeechRecognizer {

	//TODO ACY : Marche plus
	private static final String GOOGLE_RECOGNIZER_URL = "https://www.google.com/speech-api/v1/recognize?xjerr=1&client=chromium";

	public GoogleSpeechRecognizer() {
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public boolean isProfanityFilter() {
		return profanityFilter;
	}

	public void setProfanityFilter(boolean profanityFilter) {
		this.profanityFilter = profanityFilter;
	}

	public SpeechToTextResult speechToText(File input) {
		return speechToText(Variant.toByteBuffer(input), 1);
	}
	
	public SpeechToTextResult speechToText(byte[] input) {
		return speechToText(Variant.toByteBuffer(input), 1);
	}

	public SpeechToTextResult speechToText(ByteBuffer input) {
		return speechToText(input, 1);
	}

	public SpeechToTextResult speechToText(ByteBuffer input, int maxResultCount) {

		SpeechToTextResult result = null;

		try {
			// Send request to Google
			String requestResponse = rawRequest(input, maxResultCount);

			// Parse the response
			result = parseResponse(requestResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private String rawRequest(ByteBuffer input, int maxResults)
			throws IOException {
		URL url;
		URLConnection urlConn;
		OutputStream outputStream;
		BufferedReader br;

		StringBuilder sb = new StringBuilder(GOOGLE_RECOGNIZER_URL);
		if (language != null) {
			sb.append("&lang=");
			sb.append(language);
		}
		if (!profanityFilter) {
			sb.append("&pfilter=0");
		}
		sb.append("&maxresults=");
		sb.append(maxResults);

		// URL of Remote Script.
		url = new URL(sb.toString());

		// Open New URL connection channel.
		urlConn = url.openConnection();

		// we want to do output.
		urlConn.setDoOutput(true);

		// No caching
		urlConn.setUseCaches(false);

		// Specify the header content type.
		urlConn.setRequestProperty("Content-Type", "audio/x-flac; rate=8000");

		// Send POST output.
		outputStream = urlConn.getOutputStream();
		outputStream.write(input.array());
		outputStream.close();

		// Get response data.
		br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

		String response = br.readLine();

		br.close();

		return response;
	}

	private SpeechToTextResult parseResponse(String rawResponse) {

		SpeechToTextResult result = new SpeechToTextResult();

		if (rawResponse.contains("utterance")) {

			String array = substringBetween(rawResponse, "[", "]");
			String[] parts = array.split("}");

			boolean first = true;
			for (String s : parts) {
				if (first) {
					first = false;
					String utterancePart = s.split(",")[0];
					String confidencePart = s.split(",")[1];

					String utterance = utterancePart.split(":")[1];
					String confidence = confidencePart.split(":")[1];

					utterance = stripQuotes(utterance);
					confidence = stripQuotes(confidence);

					if (utterance.equals("null")) {
						utterance = null;
					}
					if (confidence.equals("null")) {
						confidence = null;
					}

					result.setResult(utterance);
					result.setConfidence(confidence);
				} else {
					String utterance = s.split(":")[1];
					utterance = stripQuotes(utterance);
					if (utterance.equals("null")) {
						utterance = null;
					}
					result.getOtherResults().add(utterance);
				}
			}
		}

		return result;
	}

	private String substringBetween(String s, String part1, String part2) {
		String sub = null;

		int i = s.indexOf(part1);
		int j = s.indexOf(part2, i + part1.length());

		if (i != -1 && j != -1) {
			int nStart = i + part1.length();
			sub = s.substring(nStart, j);
		}

		return sub;
	}

	private String stripQuotes(String s) {
		int start = 0;
		if (s.startsWith("\"")) {
			start = 1;
		}
		int end = s.length();
		if (s.endsWith("\"")) {
			end = s.length() - 1;
		}
		return s.substring(start, end);
	}

	private Language language = Language.AUTO_DETECT;

	private boolean profanityFilter = false;

}
