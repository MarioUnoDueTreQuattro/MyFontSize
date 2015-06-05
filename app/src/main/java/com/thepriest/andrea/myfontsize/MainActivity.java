package com.thepriest.andrea.myfontsize;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    RelativeLayout main_layout;
    SeekBar seekBarFontSize;
    TextView textViewFontSize;
    Button buttonApply, buttonReset, buttonClose;

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
            }
        });
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
}
