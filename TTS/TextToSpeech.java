package TTS;

import javax.speech.Central;
import javax.speech.EngineCreate;
import javax.speech.EngineList;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class TextToSpeech {

    public static void speak(String word) {
        try {
            // Set property as Kevin Dictionary
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            // Register Engine
            Central.registerEngineCentral
                    ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

            // Create a Synthesizer
            Synthesizer synthesizer =
                    Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

            // Allocate Synthesizer
            synthesizer.allocate();

            // Resume Synthesizer
            synthesizer.resume();

            // Speaks the given text until queue is empty.
            synthesizer.speakPlainText(word, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

            // Deallocate the Synthesizer.
            synthesizer.deallocate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Speaks the directions
     *
     * @param speak The String that the TTS is speaking
     */
    public static void doSpeak(String speak) {
        String speakText = speak;

        try {
            System.setProperty("FreeTTSSynthEngineCentral", "com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            SynthesizerModeDesc desc = new SynthesizerModeDesc(null, "general", Locale.US, null, null);
            Synthesizer synthesizer = Central.createSynthesizer(desc);
            EngineList engineList = Central.availableSynthesizers(desc);

            EngineCreate creator = (EngineCreate) engineList.get(0);

            List<Synthesizer> bagOfSynthesizers = new LinkedList<Synthesizer>();

            int numberSynthesizers = 1;
            for (int i = 0; i < numberSynthesizers; i++) {
                synthesizer = (Synthesizer) creator.createEngine();
                bagOfSynthesizers.add(synthesizer);
                synthesizer.allocate();
                synthesizer.resume();
                desc = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
            }

            Voice[] voices = desc.getVoices();
            for (int i = 0; i < voices.length; i++) {
//                System.out.println(voices[i].getName());
            }

            synthesizer.getSynthesizerProperties().setVoice(voices[1]);
            synthesizer.speakPlainText(speakText, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synthesizer.deallocate();

        } catch (Exception e) {
            String message = " missing speech.properties in " + System.getProperty("user.home") + "\n";
//            System.out.println("" + e);
//            System.out.println(message);
        }

    }

}

