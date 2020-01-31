package com.example.reto1_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ApunteBean;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.logging.Logger;

public class VisualizadorPdf extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(VisualizadorPdf.class.getName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizador_pdf);

        Intent intent = getIntent();
        ApunteBean apunte = (ApunteBean) intent.getSerializableExtra("apunte");
        try {
            PDFView pdfview = (PDFView) findViewById(R.id.pdfView);
            pdfview.fromBytes(apunte.getArchivo()).load();
        }catch(Exception e){
            LOGGER.severe("Error: "+e.getMessage());
        }
    }
}
