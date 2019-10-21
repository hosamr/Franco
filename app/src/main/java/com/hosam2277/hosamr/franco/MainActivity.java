package com.hosam2277.hosamr.franco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.view.View;
import android.widget.Toast;
import android.content.ClipboardManager;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    PopupMenu popup ;
    private HashMap<Character, String> francoToArabicArray = new HashMap<>();
    private HashMap<Character, String> arabicToFrancoArray = new HashMap<>();
    EditText editText;
    EditText editText2;
    DatabaseHelper myDb ;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private int counter=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4089508720843264/4068116646");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        editText=(EditText)findViewById(R.id.textView);
        editText2=(EditText)findViewById(R.id.textView2);
        setFrancoToArabicArray();
        setArabicToFrancoArray();
        myDb  = new DatabaseHelper(this );
        //AddData();
        //viewAll();
    }
    public  void AddData() {

    }
    public void viewAll() {
        Cursor res = myDb .getAllData();
        Toast.makeText(this, String.valueOf(res.getCount()), Toast.LENGTH_LONG).show();

        if(res.getCount() == 0) {
            Toast.makeText(this, "ErrorNothing found", Toast.LENGTH_LONG).show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Franco :"+ res.getString(1)+"\n");
            buffer.append("Arabic :"+ res.getString(2)+"\n");
        }

        // Show all data
       // Toast.makeText(this, "Data"+buffer.toString(), Toast.LENGTH_LONG).show();
    }

    public void francoToArabic(View v) {
        counter++;
        if (mInterstitialAd.isLoaded()&&counter%5==0) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        String francoText=editText.getText().toString();
        String words[]=francoText.split(" ");
        String arabicText="";
        for(int i=0;i<words.length;i++){
            Cursor res = myDb .getArabic(words[i]);
            if (res.getCount() != 0) {
                while (res.moveToNext()) {
                    arabicText += res.getString(res.getColumnIndex(myDb .arabicCol));
                    break;
                }
            }
            else {
                for (int j = 0; j < words[i].length(); j++) {
                    if (j == 0) {
                        if (words[i].charAt(j) == 'e')
                            arabicText += "إ";
                        else if (words[i].charAt(j) == 'o')
                            arabicText += "أُ";
                        else
                            arabicText += getFrancoToArabicArray(words[i].charAt(j));
                    } else
                        arabicText += getFrancoToArabicArray(words[i].charAt(j));
                }
            }
            arabicText+=" ";
        }
        editText2.setText(arabicText);
    }
    public void arabicToFranco(View v) {
        counter++;
        if (mInterstitialAd.isLoaded()&&counter%5==0) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
         HashMap<Integer, String> arabicToFrancoSpecialArray = new HashMap<>();
        String arabicText=editText.getText().toString();
        String words[]=arabicText.split(" ");
        String francoText="";
        for(int i=0;i<words.length-2;i++){
            Cursor res = myDb .getFranco(words[i]+" "+words[i+1]+" "+words[i+2]);
            if (res.getCount() != 0) {
                while (res.moveToNext()) {
                    arabicToFrancoSpecialArray.put(i, res.getString(res.getColumnIndex(myDb .francoCol)));
                    break;
                }
            }
        }
        for(int i=0;i<words.length;i++) {
            if (arabicToFrancoSpecialArray.containsKey(i)) {
                francoText += arabicToFrancoSpecialArray.get(i);
                i += 2;
            } else {
                Cursor res = myDb.getFranco(words[i]);
                if (res.getCount() != 0) {
                    while (res.moveToNext()) {
                        francoText += res.getString(res.getColumnIndex(myDb.francoCol));
                        break;
                    }
                } else {
                    for (int j = 0; j < words[i].length(); j++) {
                        if (j == 0) {
                            if (words[i].charAt(j) == 'ي')
                                francoText += "y";
                            else {
                                francoText += getArabicToFrancoArray(words[i].charAt(j));
                            }
                        } else
                            francoText += getArabicToFrancoArray(words[i].charAt(j));
                    }
                }
            }
            francoText += " ";
        }
        editText2.setText(francoText);
    }
    public void copy(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", editText2.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getResources().getString(R.string.copy), Toast.LENGTH_SHORT).show();
    }
    public void send(View v) {
        Intent Send=new Intent(Intent.ACTION_SEND);
        Send.setType("text/plain");
        Send.putExtra(Intent.EXTRA_TEXT,editText2.getText().toString());
        Intent.createChooser(Send,"By Franco App");
        this.startActivity(Intent.createChooser(Send,getResources().getString(R.string.sendBy)));
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actions);
        popup.show();

    }

   @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.share){
            Intent Share=new Intent(Intent.ACTION_SEND);
            Share.setType("text/plain");
            Share.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+ this.getPackageName());
            Intent.createChooser(Share,"shareapp");
            this.startActivity(Intent.createChooser(Share,"Share using "));
        }
       else if(id == R.id.moreApps){
           Intent moreapps=new Intent(Intent.ACTION_VIEW);
           moreapps.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Hosam Eldeen Reda"));
           this.startActivity(moreapps);
       }
       else if(id == R.id.rateUs){
           Intent rateapps = new Intent(Intent.ACTION_VIEW);
           rateapps.setData(Uri.parse("market://details?id=" + this.getPackageName()));
           this.startActivity(rateapps);
       }

       return true;
    }

    public void setFrancoToArabicArray() {
        francoToArabicArray.put('a', "ا");
        francoToArabicArray.put('b', "ب");
        francoToArabicArray.put('c', "ك");
        francoToArabicArray.put('d', "د");
        francoToArabicArray.put('e', "ي");
        francoToArabicArray.put('f', "ف");
        francoToArabicArray.put('g', "ج");
        francoToArabicArray.put('h', "ه");
        francoToArabicArray.put('i', "ي");
        francoToArabicArray.put('j', "ج");
        francoToArabicArray.put('k', "ك");
        francoToArabicArray.put('l', "ل");
        francoToArabicArray.put('m', "م");
        francoToArabicArray.put('n', "ن");
        francoToArabicArray.put('o', "و");
        francoToArabicArray.put('p', "ب");
        francoToArabicArray.put('q', "ك");
        francoToArabicArray.put('r', "ر");
        francoToArabicArray.put('s', "س");
        francoToArabicArray.put('t', "ت");
        francoToArabicArray.put('u', "و");
        francoToArabicArray.put('v', "ڤ");
        francoToArabicArray.put('w', "و");
        francoToArabicArray.put('x', "اكس");
        francoToArabicArray.put('y', "ي");
        francoToArabicArray.put('z', "ز");
        francoToArabicArray.put('2', "أ");
        francoToArabicArray.put('3', "ع");
        francoToArabicArray.put('4', "ش");
        francoToArabicArray.put('5', "خ");
        francoToArabicArray.put('6', "ط");
        francoToArabicArray.put('7', "ح");
        francoToArabicArray.put('8', "غ");
        francoToArabicArray.put('9', "ص");
        francoToArabicArray.put('A', "ا");
        francoToArabicArray.put('B', "ب");
        francoToArabicArray.put('C', "ك");
        francoToArabicArray.put('D', "د");
        francoToArabicArray.put('E', "ي");
        francoToArabicArray.put('F', "ف");
        francoToArabicArray.put('G', "ج");
        francoToArabicArray.put('H', "ه");
        francoToArabicArray.put('I', "ي");
        francoToArabicArray.put('J', "ج");
        francoToArabicArray.put('K', "ك");
        francoToArabicArray.put('L', "ل");
        francoToArabicArray.put('M', "م");
        francoToArabicArray.put('N', "ن");
        francoToArabicArray.put('O', "و");
        francoToArabicArray.put('P', "ب");
        francoToArabicArray.put('Q', "ك");
        francoToArabicArray.put('R', "ر");
        francoToArabicArray.put('S', "س");
        francoToArabicArray.put('T', "ت");
        francoToArabicArray.put('U', "و");
        francoToArabicArray.put('V', "ڤ");
        francoToArabicArray.put('W', "و");
        francoToArabicArray.put('X', "اكس");
        francoToArabicArray.put('Y', "ي");
        francoToArabicArray.put('Z', "ز");

    }

    public String getFrancoToArabicArray(char c) {
        if(francoToArabicArray.containsKey(c)) {
            return (francoToArabicArray.get(c));
        }
        return Character.toString(c);
    }

    public void setArabicToFrancoArray() {
        arabicToFrancoArray.put('ا', "a");
        arabicToFrancoArray.put('أ', "2");
        arabicToFrancoArray.put('ب', "b");
        arabicToFrancoArray.put('ت', "t");
        arabicToFrancoArray.put('ث', "th");
        arabicToFrancoArray.put('ج', "g");
        arabicToFrancoArray.put('ح', "7");
        arabicToFrancoArray.put('خ', "5");
        arabicToFrancoArray.put('د', "d");
        arabicToFrancoArray.put('ذ', "z");
        arabicToFrancoArray.put('ر', "r");
        arabicToFrancoArray.put('ز', "z");
        arabicToFrancoArray.put('س', "s");
        arabicToFrancoArray.put('ش', "4");
        arabicToFrancoArray.put('ص', "s");
        arabicToFrancoArray.put('ض', "d");
        arabicToFrancoArray.put('ط', "t");
        arabicToFrancoArray.put('ظ', "z");
        arabicToFrancoArray.put('ع', "3");
        arabicToFrancoArray.put('غ', "8");
        arabicToFrancoArray.put('ف', "f");
        arabicToFrancoArray.put('ق', "k");
        arabicToFrancoArray.put('ك', "k");
        arabicToFrancoArray.put('ل', "l");
        arabicToFrancoArray.put('م', "m");
        arabicToFrancoArray.put('ن', "n");
        arabicToFrancoArray.put('ه', "h");
        arabicToFrancoArray.put('و', "o");
        arabicToFrancoArray.put('ي', "i");
        arabicToFrancoArray.put('ڤ', "v");
        arabicToFrancoArray.put('ؤ', "o2");
        arabicToFrancoArray.put('ئ', "2");
        arabicToFrancoArray.put('ء', "2");
        arabicToFrancoArray.put('آ', "a");
        arabicToFrancoArray.put('ة', "a");
        arabicToFrancoArray.put('إ', "e");
        arabicToFrancoArray.put('ى', "a");
        arabicToFrancoArray.put('ً', "n");



    }

    public String getArabicToFrancoArray(char c) {
        if(arabicToFrancoArray.containsKey(c)) {
            return (arabicToFrancoArray.get(c));
        }
        return Character.toString(c);
    }

}
