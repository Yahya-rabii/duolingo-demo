package com.example.dldemo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
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
                .setImage(R.drawable.aboutuslogo)
                .setDescription(description)// or Typeface
                .addGroup("Connect with us")
                .addEmail("rabiiyahya1@gmail.com")
                .addWebsite("https://yahya.rabii.me/")
                .addFacebook("https://www.facebook.com")
                .addTwitter("https://www.twitter.com")
                .addGitHub("Yahya-rabii")
                .addInstagram("https://www.instagram.com/yahya____rabii/")
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
