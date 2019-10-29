package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.pchmn.materialchips.model.ChipInterface;

public class CreateEvent2 extends AppCompatActivity {

    EditText description, tags;
    TextView characters, changeTag;
    int numTags = 0, numChars = 0;
    boolean backspaceClicked;
    private int SpannedLength = 0, chipLength = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event2);

        characters = (TextView) findViewById(R.id.textView17);
        changeTag = (TextView) findViewById(R.id.textView11);

        characters.setText(numChars + "/1000");
        changeTag.setText(numTags + "/6 tags");

        description = (EditText) findViewById(R.id.eventDescription);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after < count) {
                    backspaceClicked = true;
                } else {
                    backspaceClicked = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numChars++;
                characters.setText(numChars + "/1000");
                if(numChars == 1000) {
                    description.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(backspaceClicked) {
                    numChars--;
                    characters.setText(numChars + "/1000");
                }
            }
        });

        tags = (EditText) findViewById(R.id.tags);
        tags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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
