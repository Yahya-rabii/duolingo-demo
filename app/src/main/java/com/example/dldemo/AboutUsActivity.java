package com.example.dldemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        String description = getString(R.string.aboutus_description);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.duolingo)
                .setDescription(description)// or Typeface
                .addGroup("Connect with us")
                .addEmail("rabiiyahya1@gmail.com")
                .addWebsite("https://yahya.rabii.me/")
                .addFacebook("https://www.facebook.com")
                .addTwitter("https://www.twitter.com")
                .addYoutube("https://www.youtube.com")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("https://github.com/Yahya-rabii")
                .addInstagram("https://www.instagram.com/yahya_rabii")
                .create();
        setContentView(aboutPage);
    }
    private Element createCopyright(){
        Element copyright = new Element();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        final String currentYear = dateFormat.format(new Date());
        final String copyrightString = String.format("Copyright by Noxidious and Matrixcsyounes %s", currentYear);
        copyright.setTitle(copyrightString);
        copyright.setGravity(Gravity.CENTER);
        return copyright;
    }
}
