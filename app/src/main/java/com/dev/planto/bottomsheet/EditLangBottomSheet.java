package com.dev.planto.bottomsheet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dev.planto.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class EditLangBottomSheet extends BottomSheetDialogFragment {

    private String languageToLoad = Locale.getDefault().getLanguage();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_lang_bottom_sheet,container,false);

        TextView lang_en,lang_hi,lang_te;

        lang_en = v.findViewById(R.id.bs_english);
        lang_hi = v.findViewById(R.id.bs_hindi);
        lang_te = v.findViewById(R.id.bs_telugu);

        lang_en.setCompoundDrawables(null,null, Drawable.createFromPath(String.valueOf(Locale.US)),null);

        lang_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageToLoad = "en";
                setLanguage();
            }
        });

        lang_hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageToLoad = "hi";
                setLanguage();
            }
        });

        lang_te.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageToLoad = "te";
                setLanguage();
            }
        });

        return v;
    }

    private void setLanguage(){
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Objects.requireNonNull(getActivity()).getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Objects.requireNonNull(getActivity()).recreate();
        dismiss();

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor mEditor =  getActivity().getSharedPreferences("prefs", MODE_PRIVATE).edit();
        mEditor.putString("lang",languageToLoad);
        mEditor.apply();
    }

}