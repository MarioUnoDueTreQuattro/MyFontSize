package com.thepriest.andrea.myfontsize;

import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexFile;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout main_layout;
    SeekBar seekBarFontSize;
    TextView textViewFontSize;
    Button buttonApply, buttonReset, buttonClose;
    public int mFontSize;
    public float mFloatFontSize;
    private static Method getConfigurationMethod;
    private static Method updateConfigurationMethod;
    private static Object am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // main_layout= (RelativeLayout) findViewById(R.id.rel_layout);
        // main_layout.setBackgroundColor(R.color.bright_foreground_material_light);
        seekBarFontSize = (SeekBar) findViewById(R.id.seekBarFontSize);
        textViewFontSize = (TextView) findViewById(R.id.textViewFontSize);
        buttonApply = (Button) findViewById(R.id.buttonApply);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonClose = (Button) findViewById(R.id.buttonClose);
        seekBarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Notification that the progress level has changed. Clients can use the fromUser parameter
             * to distinguish user-initiated changes from those that occurred programmatically.
             *
             * @param seekBar  The SeekBar whose progress has changed
             * @param progress The current progress level. This will be in the range 0..max where max
             *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
             * @param fromUser True if the progress change was initiated by the user.
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewFontSize.setText("Selected font size: " + seekBarFontSize.getProgress() * 5 + "%");
                mFontSize = seekBarFontSize.getProgress() * 5;
                mFloatFontSize = mFontSize;
                Log.d(TAG, "mFloatFontSize= " + mFloatFontSize);


            }

            /**
             * Notification that the user has started a touch gesture. Clients may want to use this
             * to disable advancing the seekbar.
             *
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * Notification that the user has finished a touch gesture. Clients may want to use this
             * to re-enable advancing the seekbar.
             *
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewFontSize.setText("Selected font size: " + 100 + "%");
                seekBarFontSize.setProgress(20);
                mFontSize = 100;
                mFloatFontSize = 1.0f;
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyFont();
            }
        });
        mFloatFontSize = getResources().getConfiguration().fontScale;
        Log.d(TAG, "mFloatFontSize= " + mFloatFontSize);
        seekBarFontSize.setProgress(Math.round(mFloatFontSize * 20));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ApplyFont() {
        am = null;
        updateConfigurationMethod = null;

        try {
            DexFile df = new DexFile(new File("/system/app/Settings.apk"));
            Class ActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
            am = Class.forName("android.app.IActivityManager").cast(ActivityManagerNative.getMethod("getDefault", null).invoke(ActivityManagerNative, null));

            Class IActivityManager = Class.forName("android.app.IActivityManager");
            Configuration config = new Configuration();
            float fSize = mFloatFontSize / 100;
            config.fontScale = fSize;
            Log.d(TAG, "fSize= " + fSize);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = config.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(config, metrics);
            //getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
            //ActivityManagerNative.getDefault().updatePersistentConfiguration(config);
            //Settings.System.putFloat(this.getContentResolver(), Settings.System.FONT_SCALE, mFloatFontSize);
            Settings.System.putConfiguration(getContentResolver(), config);
            updateConfigurationMethod = IActivityManager.getClass().getMethod("updatePersistentConfiguration", new Class[]{Configuration.class});
            MainActivity.updateConfigurationMethod.invoke(MainActivity.am, new Object[]{config});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {

            Configuration c = new Configuration();
            c.updateFrom((Configuration) MainActivity.getConfigurationMethod.invoke(MainActivity.am, null));
            c.fontScale = MainActivity.this.mFontSize;
            MainActivity.updateConfigurationMethod.invoke(MainActivity.am, new Object[]{c});
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "An error occured while trying to set the font size", e);
        }

    }

    static {
        am = null;
        updateConfigurationMethod = null;
        try {
            Class ActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
            am = Class.forName("android.app.IActivityManager").cast(ActivityManagerNative.getMethod("getDefault", null).invoke(ActivityManagerNative, null));
            getConfigurationMethod = am.getClass().getMethod("getConfiguration", null);
            updateConfigurationMethod = am.getClass().getMethod("updatePersistentConfiguration", new Class[]{Configuration.class});
        } catch (Exception e) {
            Log.e(TAG, "Unable to access private API via Reflection", e);
        }
    }
}