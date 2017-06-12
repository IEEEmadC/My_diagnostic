package org.dev4u.hv.my_diagnostic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import utils.SaveImage;

public class UserDataActivity extends AppCompatActivity {

    private Animatable cursiveAvd;
    private ImageView hearth;
    private CircleImageView circleImageView;
    private int PICK_PHOTO_FOR_AVATAR=3;
    private ImageView imageViewDialog;
    private Bitmap activePicture;
    private Button btnDate, btnCountry,btnBlood;
    private LinearLayout container;
    private AnimationDrawable anim;
    //private int Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Profile");
        setContentView(R.layout.activity_user_data);
        //cursiveAvd = ((Animatable) ((ImageView) findViewById(R.id.pulse)).getDrawable());
        //hearth = (ImageView)findViewById(R.id.pulse);
        //default picture
        activePicture = ((BitmapDrawable) getBaseContext().getDrawable(R.drawable.ic_profile)).getBitmap();
        circleImageView = (CircleImageView)findViewById(R.id.profile_image);
        //buttons
        btnDate = (Button)findViewById(R.id.btnDate);
        btnCountry = (Button)findViewById(R.id.btnCountry);
        btnBlood = (Button) findViewById(R.id.btnBlood);

        Country country = Country.getCountryByLocale(getResources().getConfiguration().locale);
        btnCountry.setCompoundDrawables(getDrawable(country.getFlag()),null,null,null);
        //btnCountry.setCompoundDrawables();
        //background

        //container = (LinearLayout) findViewById(R.id.rootview);

        //anim = (AnimationDrawable) container.getBackground();
        //anim.setEnterFadeDuration(14000);
        //anim.setExitFadeDuration(14000);

        /*restartCursiveAnimation();
        ((ImageView)findViewById(R.id.pulse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartCursiveAnimation();
            }
        });
        */

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPicture();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCountry();
            }
        });
        btnBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBlood();
            }
        });

        //loading from storage
        Bitmap b = loadImageFromStorage("Profile","profile.png");
        if(b!=null) {
            activePicture = b;
            circleImageView.setImageBitmap(Bitmap.createScaledBitmap (b,(int) (b.getWidth() * .4), (int) (b.getHeight() * .4),true));
        }
    }
    /*@Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }*/

    /*private void restartCursiveAnimation() {

        cursiveAvd.stop();
        cursiveAvd.start();
    }*/
    private void showDialogPicture(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //dialogBuilder.setTitle("Profile Picture");
        dialogBuilder.setPositiveButton("Change picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pickImage();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_picture, null);
        dialogBuilder.setView(dialogView);
        imageViewDialog = (ImageView) dialogView.findViewById(R.id.imageViewDialog);
        imageViewDialog.setImageBitmap(activePicture);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation_Window;
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }


    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    public void pickDate(){
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDate.setText(new StringBuilder()
                                // Month is 0 based so add 1
                                .append(dayOfMonth).append("/").append(month+1).append("/")
                                .append(year).append(" "));
                    }
                }, 1990, 0, 1);//yyyy , mm+1 , dd
        dialog.show();
        dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void pickCountry(){
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                btnCountry.setText(name);
                picker.dismiss();
            }
        });
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    public void pickBlood(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select your blood type : ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        arrayAdapter.add("O+");
        arrayAdapter.add("O-");
        arrayAdapter.add("A+");
        arrayAdapter.add("A-");
        arrayAdapter.add("B+");
        arrayAdapter.add("B-");
        arrayAdapter.add("AB+");
        arrayAdapter.add("AB-");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                btnBlood.setText(strName);
                /*AlertDialog.Builder builderInner = new AlertDialog.Builder(UserDataActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
                */
            }
        });
        AlertDialog alertDialog = builderSingle.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation_Window;
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                Toast.makeText(this,"Error loading image",Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
            if(bmp!=null){
                activePicture = bmp;
                circleImageView.setImageBitmap(Bitmap.createScaledBitmap (bmp,(int) (bmp.getWidth() * .4), (int) (bmp.getHeight() * .4),true));
                new SaveImage(this,"Profile","profile.png").execute(bmp);
            }

        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage,String Path,String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Path, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path,String name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(this);
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, name);
            Log.d("Se busca en: ",f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}
