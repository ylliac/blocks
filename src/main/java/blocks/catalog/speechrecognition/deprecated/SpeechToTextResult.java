package blocks.catalog.speechrecognition.deprecated;

import java.util.ArrayList;
import java.util.List;

public class SpeechToTextResult {

	public SpeechToTextResult() {
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public List<String> getOtherResults() {
		return otherResults;
	}

	public void setOtherResults(List<String> otherResults) {
		this.otherResults = otherResults;
	}

	public List<String> getAllResult() {
		List<String> allResults = otherResults;
		allResults.add(0, result);
		return allResults;
	}

	private String result;

	private String confidence;

	private List<String> otherResults = new ArrayList<String>();

}
