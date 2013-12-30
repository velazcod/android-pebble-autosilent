package com.danvelazco.pebble.autosoundprofiles.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * {@link android.content.BroadcastReceiver} used to get intent actions
 * related to a Pebble watch connecting or disconnecting.
 *
 * Whenever a watch is connected, the phone's ring mode will be silent,
 * otherwise it will go back to normal.
 */
public class PebbleConnectionReceiver extends BroadcastReceiver {

    // Constants
    private static final String ACTION_CONNECTED = "com.getpebble.action.PEBBLE_CONNECTED";
    private static final String ACTION_DISCONNECTED = "com.getpebble.action.PEBBLE_DISCONNECTED";
    private static final String ACTION_ALREADY_CONNECTED = "com.danvelazco.pebble.autosoundprofiles.pebble_already_connected";

    // Store original ring setting (e.g. RINGER_MODE_VIBRATE or RINGER_MODE_NORMAL)
    private static int originalRinger = AudioManager.RINGER_MODE_NORMAL;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: add ability to only allow one specific watch to change ring mode?
        //final String pebbleAddress = intent.getStringExtra("address");

        if (ACTION_CONNECTED.equals(intent.getAction()) || ACTION_ALREADY_CONNECTED.equals(intent.getAction())) {
            setSilentMode(context, true);
        } else if (ACTION_DISCONNECTED.equals(intent.getAction())) {
            setSilentMode(context, false);
        }
    }

    /**
     * Set the audio ringer mode to silent mode, or not
     *
     * @param context {@link android.content.Context}
     * @param silent {@link boolean} whether to set to silent or unset
     */
    public void setSilentMode(Context context, boolean silent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (silent) {
            originalRinger = audioManager.getRingerMode();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(originalRinger);
        }
    }

}
