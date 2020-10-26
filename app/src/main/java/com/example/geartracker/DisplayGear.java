package com.example.geartracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class DisplayGear extends AppCompatActivity {
    private static final int GET_FROM_GALLERY = 1;
    Calendar datePick;
    DatePickerDialog dDialog;
    byte[] imageInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_gear);
        TextView title = findViewById(R.id.title);
        final EditText tDate = findViewById(R.id.gearDate);
        int del = Integer.parseInt(getIntent().getStringExtra("del"));
        title.setText("Item Information");


        EditText tMaker = findViewById(R.id.gearMaker);

        EditText tDes = findViewById(R.id.gearDes);

        EditText tPrice = findViewById(R.id.gearPrice);

        EditText tCom = findViewById(R.id.gearCom);
        ImageButton image = findViewById(R.id.imageButton);
        ImageView imgV = findViewById(R.id.imageView);
        //if we are editing an item, show its information (del of -1 means adding)
        if(del != -1) {
            //get gear info
            String date = getIntent().getStringExtra("date");
            String maker = getIntent().getStringExtra("maker");
            String des = getIntent().getStringExtra("des");
            String price = String.valueOf(getIntent().getStringExtra("price"));
            String comment = getIntent().getStringExtra("com");
            byte[] img = getIntent().getByteArrayExtra("img");
            //set gear info in edit screen
            tDate.setText(date);
            tMaker.setText(maker);
            tDes.setText(des);
            tPrice.setText(price);
            tCom.setText(comment);
            Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
            imgV.setImageBitmap(bmp);
        }
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
        //get datepicker when the date field is clicked
        tDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick = Calendar.getInstance();
                int day = datePick.get(Calendar.DAY_OF_MONTH);
                int month = datePick.get(Calendar.MONTH);
                int year = datePick.get(Calendar.YEAR);

                dDialog = new DatePickerDialog(DisplayGear.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int nYear, int nMonth, int nDay) {
                        //The month is 1 less for some reason, so 1 has to be added
                        nMonth = nMonth + 1;
                        String nMonths = null;
                        String nDays = null;
                        //format the date and month to show 0x if less than 10
                        if(nMonth < 10){
                            nMonths = "0" + nMonth;
                        }else{nMonths = String.valueOf(nMonth);}
                        if(nDay < 10){
                            nDays = "0" + nDay;
                        }else{nDays = String.valueOf(nDay);
                        }

                        tDate.setText(nYear + "-" + nMonths + "-" + nDays);
                    }
                },day,month,year);
                dDialog.show();
            }
        });

    }
    //sends new information from the editTexts
    public void changeInfo(View view) {
        //get all text information
        Intent returnIntent = new Intent();
        TextView tDate = findViewById(R.id.gearDate);
        TextView tMaker = findViewById(R.id.gearMaker);
        TextView tDes = findViewById(R.id.gearDes);
        TextView tPrice = findViewById(R.id.gearPrice);
        TextView tCom = findViewById(R.id.gearCom);
        //if any of the required info is empty, show Toast alert, list is not updated
        if(tDate.getText().toString(). matches("") ||
                tMaker.getText().toString(). matches("") ||
                tDes.getText().toString(). matches("") ||
                tPrice.getText().toString(). matches("")){
            //show alert
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter required gear information", Toast.LENGTH_SHORT);
            toast.show();
        }
        //else, required info is filled, update list (add or edit)
        else{
            //return information of new/edited gear
            returnIntent.putExtra("newD", tDate.getText().toString());
            returnIntent.putExtra("newM", tMaker.getText().toString());
            returnIntent.putExtra("newDs", tDes.getText().toString());
            returnIntent.putExtra("newP", tPrice.getText().toString());
            returnIntent.putExtra("newC", tCom.getText().toString());
            returnIntent.putExtra("newImg",  imageInfo);
            Log.d("setting info", "info set");
            //go back to MainActivity when information is correct
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageInfo = stream.toByteArray();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
