package com.codeboy.ijk.f_ijk.ui;

import android.content.Context;

public class IJKAudioManager {

    public static void abandonAudioFocus(Context context) {
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
    }

    public static void requestAudioFocus(Context context) {
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, android.media.AudioManager.STREAM_MUSIC, android.media.AudioManager.AUDIOFOCUS_GAIN);
    }


}
