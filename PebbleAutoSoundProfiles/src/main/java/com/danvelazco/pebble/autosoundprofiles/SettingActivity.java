package com.danvelazco.pebble.autosoundprofiles;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.danvelazco.pebble.autosoundprofiles.receiver.PebbleConnectionReceiver;

/**
 * Activity that allows us to see the current state of the
 * {@link com.danvelazco.pebble.autosoundprofiles.receiver.PebbleConnectionReceiver}
 * component so that we can enable or disable it accordingly.
 */
public class SettingActivity extends Activity {

    // Members
    private Button mButtonState = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mButtonState = (Button) findViewById(R.id.btn_enable);
        mButtonState.setText(isReceiverEnabled() ? R.string.btn_disable : R.string.btn_enable);
        mButtonState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleComponentState();
            }
        });
    }

    /**
     * Toggle the state of the
     * {@link com.danvelazco.pebble.autosoundprofiles.receiver.PebbleConnectionReceiver}
     * component. If enabled, disable it, and vice-versa.
     */
    private void toggleComponentState() {
        mButtonState.setEnabled(false);
        // Toggle the state and update text
        mButtonState.setText(setReceiverEnabled(!isReceiverEnabled())
                ? R.string.btn_disable : R.string.btn_enable);
        mButtonState.setEnabled(true);
    }

    /**
     * Check whether the {@link com.danvelazco.pebble.autosoundprofiles.receiver.PebbleConnectionReceiver}
     * component is currently enabled or not.
     *
     * @return {@link boolean}
     */
    private boolean isReceiverEnabled() {
        ComponentName component = new ComponentName(SettingActivity.this, PebbleConnectionReceiver.class);
        PackageManager pm = getPackageManager();
        if (pm != null) {
            int setting = pm.getComponentEnabledSetting(component);
            if (setting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                return true;
            }
        }
        return false;
    }

    /**
     * Change the state of the {@link com.danvelazco.pebble.autosoundprofiles.receiver.PebbleConnectionReceiver}
     * component.
     *
     * @param enabled {@link boolean}
     * @return {@link boolean} if the component's state was changed successfully
     */
    private boolean setReceiverEnabled(boolean enabled) {
        // Component to change the state of
        ComponentName component = new ComponentName(SettingActivity.this, PebbleConnectionReceiver.class);
        PackageManager pm = getPackageManager();
        if (pm != null) {
            pm.setComponentEnabledSetting(component,
                    (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED),
                    PackageManager.DONT_KILL_APP);
            if (enabled) {
                // If the component is enabled, make it check the connection status of the watch
                Intent intent = new Intent(PebbleConnectionReceiver.ACTION_CHECK_STATUS);
                sendBroadcast(intent);
            }
        }
        return enabled;
    }
    
}
