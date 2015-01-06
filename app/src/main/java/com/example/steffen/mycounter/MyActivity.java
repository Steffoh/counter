package com.example.steffen.mycounter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;


public class MyActivity extends Activity {
    private List<Counter> counterlist;
    private TableLayout TBLomni;
    private int counternummerierung = 1;
    private File f;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        TBLomni = (TableLayout) findViewById(R.id.TBLomni);
        counterlist = new ArrayList<Counter>();

    }


    //Hinzufügen der Menüs aus menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        getMenuInflater().inflate(R.menu.addcountermenu, menu);
        return true;
    }

    //Auswahl von Menüs
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast counteradded = new Toast(this);
        String[] a = null;

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.addcountermenu:
                //counteradded.makeText(this, "Counter added.", Toast.LENGTH_SHORT).show();
                addcounter(a);
                return true;
            case R.id.savetemplate:
                savetemplate(counterlist);
                return true;
            case R.id.loadtemplate:
                loadtemplate();
                return true;
            case R.id.newtemplate:
                deleteallcounter();
                counternummerierung = 1;
                return true;
            case R.id.deletefile:
                deletesavefile();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public static String toJSon(List<Counter> counterlist) {
        JSONArray jscounterliste = new JSONArray();

        for (Counter c : counterlist) {

            JSONObject jsoncounter = new JSONObject();
            try {

                jsoncounter.put("countername", c.getTVcountername().getText());
                jsoncounter.put("zaehler", c.getTVzaehler().getText());
                jscounterliste.put(jsoncounter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jscounterliste.toString();
    }

    public void savetemplate(List counterlist) {
        try {

            deletesavefile();
            FileOutputStream fos = openFileOutput("template.json", MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(toJSon(counterlist));
            osw.flush();
            osw.close();
            Toast.makeText(this, "Template saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadtemplate() {

        try {
            InputStream instream = openFileInput("template.json");
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);

            deleteallcounter();

            String line;
            line = buffreader.readLine();
            try {
                JSONArray jsoncounterlist = new JSONArray(line);
                String[] a = new String[2];
                for (int i = 0; i < jsoncounterlist.length(); i++) {
                    JSONObject counter = jsoncounterlist.getJSONObject(i);
                    String jsoncountername = counter.getString("countername");
                    String jsonzaehler = counter.getString("zaehler");

                    a[0] = jsoncountername;
                    a[1] = jsonzaehler;
                    addcounter(a);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Erstellung und Hinzufügen des Counters
    public void addcounter(String... a) {


        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowlayoutparams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(rowlayoutparams);
        Animation slideleft = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        InputMethodManager keyboard = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        Button Bplusone = new Button(this);
        Button Bsubone = new Button(this);
        TextView TVzaehler = new TextView(this);
        if (a == null) {
            TVzaehler.setText("0");
        } else {
            TVzaehler.setText(a[1]);
        }

        TextView TVcountername = new TextView(this);

        if (a == null) {
            TVcountername.setText("Counter " + counternummerierung++);
        } else {
            TVcountername.setText(a[0]);
        }

        TVcountername.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        Bplusone.setText("+");
        Bsubone.setText("-");
        ViewSwitcher VSbuttonedittext = new ViewSwitcher(this);
        EditText ETeditcountername = new EditText(this);
        Counter counter = new Counter(TVcountername, TVzaehler, Bplusone, Bsubone, VSbuttonedittext, ETeditcountername, slideleft, this, TBLomni);
        counterlist.add(counter);
        if (counterlist == null) {
            Toast error = new Toast(this);
            error.makeText(this, "Die Liste ist leer, digga", Toast.LENGTH_LONG).show();
        }


        row.addView(VSbuttonedittext);
        row.addView(Bplusone);
        row.addView(Bsubone);
        row.addView(TVzaehler);

        TBLomni.addView(row);

    }

    public void deleteallcounter() {

        TBLomni.removeAllViews();
        counternummerierung = 1;

    }

    public void deletesavefile() {

        f = new File("/data/data/com.example.steffen.mycounter/files/template.json");
        boolean deleted = f.delete();
        if (deleted) {
            System.out.println("Deleted");
        } else {
            System.out.println("Not Deleted");
        }

    }

}
