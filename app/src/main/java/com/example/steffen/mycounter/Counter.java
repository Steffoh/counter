package com.example.steffen.mycounter;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Created by Steffen on 06.08.2014.
 */
public class Counter implements View.OnClickListener, Serializable {
    private TextView TVzaehler, TVcountername;
    private Button Baddone, Bsubone;
    private int zaehler;
    private ViewSwitcher VSbuttonedittext;
    private EditText ETeditcountername;
    private Animation slideleft;
    private InputMethodManager imm;
    private Context context;
    private TableLayout tblomni;

    public TextView getTVzaehler() {
        return TVzaehler;
    }

    public TextView getTVcountername() {
        return TVcountername;
    }

    public Button getBaddone() {
        return Baddone;
    }

    public Button getBsubone() {
        return Bsubone;
    }

    public int getZaehler() {
        return zaehler;
    }

    public ViewSwitcher getVSbuttonedittext() {
        return VSbuttonedittext;
    }

    public EditText getETeditcountername() {
        return ETeditcountername;
    }

    public Animation getSlideleft() {
        return slideleft;
    }

    public Counter(final TextView TVcountername, TextView TVzaehler, Button Baddone, Button Bsubone, final ViewSwitcher VSbuttonedittext, final EditText ETeditcountername, Animation slideleft, Context context, TableLayout tblomni) {
        this.context = context;
        this.TVzaehler = TVzaehler;
        this.Baddone = Baddone;
        this.Bsubone = Bsubone;
        this.VSbuttonedittext = VSbuttonedittext;
        this.ETeditcountername = ETeditcountername;
        this.TVcountername = TVcountername;
        this.slideleft = slideleft;
        this.tblomni = tblomni;
        Baddone.setOnClickListener(this);
        Bsubone.setOnClickListener(this);
        VSbuttonedittext.addView(TVcountername);
        VSbuttonedittext.addView(ETeditcountername);
        VSbuttonedittext.setInAnimation(slideleft);
        VSbuttonedittext.setOutAnimation(slideleft);
        TVcountername.setOnClickListener(this);
        ETeditcountername.setOnClickListener(this);
        ETeditcountername.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keycode) {
                        case KeyEvent.KEYCODE_ENTER:
                            changename();
                            return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == Baddone) {
            zaehler++;
            TVzaehler.setText("" + zaehler);
        }

        if (view == Bsubone) {
            zaehler--;
            TVzaehler.setText("" + zaehler);
        }

        if (view == TVcountername) {
            TVcountername.setVisibility(View.GONE);
            VSbuttonedittext.showNext();
            ETeditcountername.setVisibility(View.VISIBLE);
            showSoftKeyboard(ETeditcountername);
            TVzaehler.setText("" + zaehler);
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideSoftKeyboard(View view) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void changename() {
        if (ETeditcountername.getText().length() >= 1) {
            TVcountername.setText(ETeditcountername.getText());
            ETeditcountername.setVisibility(View.GONE);
            VSbuttonedittext.showNext();
            TVcountername.setVisibility(View.VISIBLE);
            hideSoftKeyboard(TVcountername);
            hideSoftKeyboard(ETeditcountername);

        } else {
            ETeditcountername.setVisibility(View.GONE);
            VSbuttonedittext.showNext();
            TVcountername.setVisibility(View.VISIBLE);
        }
    }


}
