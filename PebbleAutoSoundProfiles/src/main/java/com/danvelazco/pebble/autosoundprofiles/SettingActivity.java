package com.danvelazco.pebble.autosoundprofiles;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.danvelazco.pebble.autosoundprofiles.receiver.PebbleConnectionReceiver;

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
            /**
             * {@inheritDoc}
             */
            @Override
            public void onClick(View v) {
                mButtonState.setEnabled(false);
                // Togglet the state and update text
                mButtonState.setText(setReceiverEnabled(!isReceiverEnabled())
                        ? R.string.btn_disable : R.string.btn_enable);
                mButtonState.setEnabled(true);
            }
        });
    }

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

    private boolean setReceiverEnabled(boolean enabled) {
        // Component to change the state of
        ComponentName component = new ComponentName(SettingActivity.this, PebbleConnectionReceiver.class);
        PackageManager pm = getPackageManager();
        if (pm != null) {
            pm.setComponentEnabledSetting(component,
                    (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED),
                    PackageManager.DONT_KILL_APP);
        }
        return enabled;
    }
    
}
