package com.getflourish.stt;

public interface TranscribeListener {

	void transcribe(String utterance, float confidence, int status);
	
}
