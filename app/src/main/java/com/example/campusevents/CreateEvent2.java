package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.ChipDrawable;
import com.pchmn.materialchips.model.ChipInterface;

/* Class written by Christopher Park */

public class CreateEvent2 extends AppCompatActivity {

    EditText description, tags;
    TextView characters, changeTag, belowMsg;
    int numTags = 0, numChars = 0, previousLength;
    private int SpannedLength = 0, chipLength = 4;
    Button preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event2);

        belowMsg = (TextView) findViewById(R.id.textView29);
//        belowMsg.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        belowMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        characters = (TextView) findViewById(R.id.textView17);
        changeTag = (TextView) findViewById(R.id.textView11);

        characters.setText(numChars + "/1000");
        changeTag.setText(numTags + "/6 tags");

        description = (EditText) findViewById(R.id.eventDescription);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean backspaceClicked = previousLength > s.length();
                if(!backspaceClicked && s.length() == 1000) {
                    characters.setTextColor(Color.RED);
                } else if(backspaceClicked) {
                    characters.setTextColor(Color.BLACK);
                } else if(s.length() > previousLength) {
                    numChars++;
                }
                characters.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        description.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(keyCode == KeyEvent.KEYCODE_DEL) {
//                    numChars--;
//                    characters.setText(numChars + "/1000");
//                    return true;
//                }
//                return false;
//            }
//        });

        final ChipDrawable chip = ChipDrawable.createFromResource(this, R.xml.chip);
        tags = (EditText) findViewById(R.id.tags);
        tags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == SpannedLength - chipLength) {
                    SpannedLength = s.length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length()-1, s.length()).toString().equalsIgnoreCase("\n")) {
                    chip.setText(s.subSequence(SpannedLength,s.length()));
                    chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                    ImageSpan span = new ImageSpan(chip);
                    s.setSpan(span, SpannedLength, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannedLength = s.length();
                }
            }
        });

        description.setScroller(new Scroller(getApplicationContext()));
        description.setVerticalScrollBarEnabled(true);
        description.setMovementMethod(new ScrollingMovementMethod());

        tags.setScroller(new Scroller(getApplicationContext()));
        tags.setVerticalScrollBarEnabled(true);
        tags.setMovementMethod(new ScrollingMovementMethod());

//    } else if (newEvent.tags.isEmpty()) {
//        final AlertDialog.Builder warningMsg = new AlertDialog.Builder(CreateEvent.this);
//        warningMsg.setMessage("Adding tags helps students find your club/event easier. Would you like to add some tags?").setCancelable(false)
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(CreateEvent.this, CreateEvent2.class);
//                        startActivity(intent);
//                    }
//                })
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = warningMsg.create();
//        alert.setTitle("No tags?");
//        alert.show();

        preview = (Button) findViewById(R.id.button5);
        preview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numChars == 0) {
                    Toast.makeText(getApplicationContext(), "Please put in a description for your event!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CreateEvent2.this, Preview.class);
                    Bundle extras = getIntent().getExtras();
                    if(extras != null) {
                        String phone = extras.getString("Phone");
                        intent.putExtra("Phone", phone);
                        NewEvent.newEvent.description = description.getText().toString();
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public TextView createContactTextView(String text){
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(20);
        tv.setBackgroundResource(R.drawable.purplebtn_rounded);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,android.R.drawable.presence_offline, 0);
        return tv;
    }

    public static Object convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);

    }
}
