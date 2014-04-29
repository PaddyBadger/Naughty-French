package com.naughtyfrench.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

/**
 * Created by patriciaestridge on 4/3/14.
 */
public class FrenchSlidesFragment extends Fragment implements View.OnClickListener, OnInitListener {
    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private TextToSpeech mTts;
    private String[] frenchStrings;
    private int MY_DATA_CHECK_CODE = 0;
    private Random rand = new Random();
    private Integer[] drawables = {
            R.drawable.french1, R.drawable.french2, R.drawable.french3, R.drawable.french4, R.drawable.french5
    };
    private int randomCartoon = rand.nextInt(drawables.length);

    public static FrenchSlidesFragment create(int pageNumber) {
        FrenchSlidesFragment fragment = new FrenchSlidesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FrenchSlidesFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_french_slides_page, container, false);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.title);
        titleTextView.setText("Essential Cussing " + mPageNumber+1);

        TextView englishTextView = (TextView) rootView.findViewById(R.id.english);
        String[] englishStrings = getResources().getStringArray(R.array.english);
        englishTextView.setText(englishStrings[mPageNumber]);

        TextView textView = (TextView) rootView.findViewById(R.id.french);
        frenchStrings = getResources().getStringArray(R.array.french);
        textView.setText(frenchStrings[mPageNumber]);

        ImageButton speakButton = (ImageButton) rootView.findViewById(R.id.speak);
        speakButton.setOnClickListener(this);

        ImageButton cartoon = (ImageButton) rootView.findViewById(R.id.drawing);
        cartoon.setBackgroundResource(drawables[randomCartoon]);

        Intent frenchIntent = new Intent();
        frenchIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(frenchIntent, MY_DATA_CHECK_CODE);

        return rootView;
    }

    public int getPageNumber() { return mPageNumber;}

    public void onClick(View v) {
        String words = frenchStrings[mPageNumber];
        frenchSpeak(words);
    }

    public void frenchSpeak(String speak) {
        mTts.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mTts = new TextToSpeech(getActivity(), this);
            } else {
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.FRANCE);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this.getActivity(), "Sorry, Text to speech failed", Toast.LENGTH_SHORT).show();
        }
    }
}
