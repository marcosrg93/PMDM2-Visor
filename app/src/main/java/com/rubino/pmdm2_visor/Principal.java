package com.rubino.pmdm2_visor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Principal extends AppCompatActivity {

    private ImageView iv;
    private Bitmap bitmap;
    private FloatingActionButton fab;
    public static final int REQUEST_IMAGE_GET = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    public void init() {
        iv = (ImageView)findViewById(R.id.ivFoto);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        iv.setImageURI(uri);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFoto(view);
            }
        });




    }

    //Boton cargar Imagen
    public void cargarFoto(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }}



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("Result", "Entro");
            if(resultCode == RESULT_OK){
                Log.v("Result","Entro if");
            Uri imagenUri = data.getData();
                Log.v("URIREsult",imagenUri.toString());
            try {
                Log.v("Result","Entro try");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagenUri);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
                Log.v("Result", "Salgo");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        View view = getCurrentFocus();
        if (id == R.id.action_grey) {
            escalaGris(view);
            return true;
        }
        if (id == R.id.action_rotate) {
            rotarImg(view);
            return true;
        }
        if (id == R.id.action_shine) {
            shineImg(view);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap getBmp() {
        BitmapDrawable bmpDraw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bmpDraw.getBitmap();
        return bitmap;
    }


    public void escalaGris (View v){
        bitmap = getBmp();
        iv.setImageBitmap(Principal.toEscalaDeGris(bitmap));
    }

    public void rotarImg (View v){
        bitmap = getBmp();
        iv.setImageBitmap(Principal.rotarBitmap(bitmap, 90));

    }
    

    public static Bitmap toEscalaDeGris(Bitmap bmpOriginal) {
        Bitmap bmpGris = Bitmap.createBitmap(bmpOriginal.getWidth(),
                bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas lienzo = new Canvas(bmpGris);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(cmcf);
        lienzo.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGris;
    }
    public static Bitmap rotarBitmap(Bitmap bmpOriginal, float angulo) {
        Matrix matriz = new Matrix();
        matriz.postRotate(angulo);
        return Bitmap.createBitmap(bmpOriginal, 0, 0,
                bmpOriginal.getWidth(), bmpOriginal.getHeight(), matriz, true);
    }


    public void shineImg(View view) {
        bitmap = getBmp();

        //Dar mas brillo a la imagen
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), bitmap.getConfig());
        int pixel, red, green, blue, alpha;
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                pixel = bitmap.getPixel(i, j);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);
                alpha = Color.alpha(pixel);
                red = 100 + red;
                green = 100 + green;
                blue = 100 + blue;
                alpha = 100 + alpha;
                bmp.setPixel(i, j, Color.argb(alpha, red, green, blue));

                //bitmap.getWidth() - i - 1 dar la vuelta img
            }
        }
        iv.setImageBitmap(bmp);
    }

}
