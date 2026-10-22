
package com.example.vivitekirremote;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private ConsumerIrManager ir;
    private final String[] names = {
        "POWER", "OFF", "HDMI", "PC", "S-Video",
        "USB", "HDBaseT", "Network", "Menu", "Exit",
        "Up", "Right", "Down", "Left", "Enter",
        "MHL", "Keystone", "Source", "Auto", "VOL+",
        "Brightness", "Contrast", "MUTE", "Lamp", "Zoom",
        "3D", "Freeze", "Blank", "Status", "Skip Back",
        "Play/Pause", "Skip Forward", "Rewind", "Stop", "Forward"
    };

    private final int[] codes = {
        0x90, 0x91, 0x7A, 0x79, 0x78,
        0x77, 0x75, 0x76, 0x87, 0xCD,
        0xC1, 0xC4, 0xC2, 0xC3, 0xC5,
        0x60, 0x61, 0x83, 0x86, 0xD0,
        0x9A, 0x99, 0x89, 0x6E, 0xBD,
        0x82, 0x8E, 0x8B, 0x88, 0x40,
        0x41, 0x42, 0x43, 0x44, 0x45
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ir = (ConsumerIrManager) getSystemService(
            Context.CONSUMER_IR_SERVICE
        );

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16, 16, 16, 16);
        root.setBackgroundColor(0xFF101820);

        TextView title = new TextView(this);
        title.setText("VIVITEK 4K REMOTE");
        title.setTextSize(24);
        title.setTextColor(0xFFFFFFFF);
        title.setGravity(Gravity.CENTER);
        title.setPadding(4, 20, 4, 20);
        root.addView(title);

        TextView status = new TextView(this);
        status.setText("Infrared Projector Remote");
        status.setTextColor(0xFF80CBC4);
        status.setGravity(Gravity.CENTER);
        status.setPadding(4, 4, 4, 18);
        root.addView(status);

        ScrollView scroll = new ScrollView(this);
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(3);
        grid.setUseDefaultMargins(true);

        for (int i = 0; i < names.length; i++) {
            final int code = codes[i];
            Button b = new Button(this);
            b.setText(names[i]);
            b.setTextSize(12);
            b.setOnClickListener(v -> sendIR(code));
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.columnSpec = GridLayout.spec(
                GridLayout.UNDEFINED, 1f
            );
            grid.addView(b, lp);
        }

        scroll.addView(grid);
        root.addView(scroll, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ));
        setContentView(root);
    }

    private void sendIR(int command) {
        if (ir == null || !ir.hasIrEmitter()) {
            Toast.makeText(this,
                "This phone has no IR blaster",
                Toast.LENGTH_LONG).show();
            return;
        }

        int[] pattern = new int[67];
        int p = 0;
        pattern[p++] = 9000;
        pattern[p++] = 4500;

        int[] bytes = {
            0x31, 0xCE, command, command ^ 0xFF
        };

        for (int value : bytes) {
            for (int bit = 0; bit < 8; bit++) {
                pattern[p++] = 560;
                pattern[p++] = ((value >> bit) & 1) == 1
                    ? 1690 : 560;
            }
        }
        pattern[p++] = 560;

        int[] frame = new int[p];
        System.arraycopy(pattern, 0, frame, 0, p);
        ir.transmit(38000, frame);

        Toast.makeText(this, "Sent: " + namesFor(command),
            Toast.LENGTH_SHORT).show();
    }

    private String namesFor(int command) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i] == command) return names[i];
        }
        return "IR command";
    }
}
