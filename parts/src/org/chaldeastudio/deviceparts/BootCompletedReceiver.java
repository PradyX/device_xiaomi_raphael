/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.chaldeastudio.deviceparts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.util.Log;
import androidx.preference.PreferenceManager;

import org.chaldeastudio.deviceparts.dirac.DiracUtils;
import org.chaldeastudio.deviceparts.doze.DozeUtils;
import org.chaldeastudio.deviceparts.popupcamera.PopupCameraUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "XiaomiParts";
    private static final String FOD_SCREENOFF_ENABLE_KEY = "fod_screenoff_enable";
    private static final String FOD_SCRNOFFD_PROP = "persist.sys.gfscreenoffd.run";

    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (DozeUtils.isDozeEnabled(context) && DozeUtils.sensorsEnabled(context)) {
            if (DEBUG) Log.d(TAG, "Starting Doze service");
            DozeUtils.startService(context);
        }
        new DiracUtils(context).onBootCompleted();
        PopupCameraUtils.startService(context);

        boolean fodScreenOffState = sharedPrefs.getBoolean(FOD_SCREENOFF_ENABLE_KEY, false);
        SystemProperties.set(FOD_SCRNOFFD_PROP, fodScreenOffState ? "1" : "0");
    }
}
