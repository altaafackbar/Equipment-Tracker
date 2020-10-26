package com.example.geartracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    //initializing variables
    protected static final long DELAY = 5;
    private static final int LAUNCH_GEAR = 1;
    ListView gearList;
    GearListAdapter gearAdapter;
    ArrayList<Gear> dataList;
    //del keeps track of currently selected item
    int del = -1;
    TextView totalPrice;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up gear list and gear list adapter
        gearList = findViewById(R.id.gear_list);
        dataList = new ArrayList<>();
        gearAdapter = new GearListAdapter(this,R.layout.adapter_view, dataList);
        gearList.setAdapter(gearAdapter);
        totalPrice = findViewById(R.id.totalPrice);
        //set up runnable to keep updating the total price
        handler.post(TextRunnable);
        //keep track of list item currently selected
        gearList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                del = position;
            }
        });

    }
    //keeps track of the total price by calculating it every 5ms
    //there are definitely better ways of achieving the same effect
    //but this solution came to me first
    Runnable TextRunnable = new Runnable(){
        public void run() {
            double total = 0;
            for(Gear gear: dataList){
                total = total + gear.getPrice();
            }
            totalPrice.setText(String.format("Total Gear Price: $%s", String.valueOf(total)));
            handler.postDelayed(this, DELAY);
        }
    };
    //add new gear to the list, del is set to -1 to notify that something is getting added
    public void newGear(View view) {
        //adding a new element, no gear information yet, so only del is put in
        del = -1;
        Intent intent = new Intent(this, DisplayGear.class);
        intent.putExtra("del", String.valueOf(del));
        startActivityForResult(intent, LAUNCH_GEAR);
        gearAdapter.notifyDataSetChanged();
    }
    //edit gear at currently selected index
    public void editGear(View view) {
        if(del != -1) {
            Log.d("CHECK", "editGear: del is " + String.valueOf(del));
            //create new intent and put in existing information
            Intent intent = new Intent(this, DisplayGear.class);
            intent.putExtra("date", dataList.get(del).getDate());
            intent.putExtra("maker", dataList.get(del).getMaker());
            intent.putExtra("des", dataList.get(del).getDescription());
            intent.putExtra("price", String.valueOf(dataList.get(del).getPrice()));
            intent.putExtra("com", dataList.get(del).getComment());
            intent.putExtra("del", String.valueOf(del));
            intent.putExtra("img", dataList.get(del).getImage());
            startActivityForResult(intent, LAUNCH_GEAR);
        }
        gearAdapter.notifyDataSetChanged();
    }
    //remove selected list item
    public void listRem(View view) {
        if(del != -1) {
            dataList.remove(del);
            gearAdapter.notifyDataSetChanged();
        }
        del = -1;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_GEAR) {
            if (resultCode == Activity.RESULT_OK) {
                //make new gear object based on received information
                Log.d("MyTag", "getting info");
                Gear newGear = new Gear(
                        data.getStringExtra("newD")
                        , data.getStringExtra("newM")
                        , data.getStringExtra("newDs")
                        , Double.valueOf(data.getStringExtra("newP"))
                        , data.getStringExtra("newC")
                        , data.getByteArrayExtra("newImg"));
                Log.d("MyTag", "made new gear");
                if (del != -1) {
                    //if edited, set edited object to newGear
                    dataList.set(del, newGear);
                    gearAdapter.notifyDataSetChanged();
                } else {
                    //else new gear is being added
                    Log.d("MyTag", "adding new gear");
                    dataList.add(newGear);
                    gearAdapter.notifyDataSetChanged();
                    Log.d("MyTag", "adding new gears");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("MyTag", "No result");
            }
        }
    }
}


