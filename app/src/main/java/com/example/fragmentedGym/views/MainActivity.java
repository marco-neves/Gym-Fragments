package com.example.fragmentedGym.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.fragmentedGym.R;
import com.example.fragmentedGym.adapters.RecyclerViewAdapterInventory;
import com.example.fragmentedGym.adapters.RecyclerViewAdapterMerchandise;
import com.example.fragmentedGym.database.GymDB;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapterMerchandise.ShopDelegate, FragmentedPurchase.FragmentedShop {

    ArrayList<String> shopItems = new ArrayList<>();
    ArrayList<String> myItems = new ArrayList<>();
    @BindView(R.id.rvShop)
    RecyclerView recyclerViewMerchandise;
    @BindView(R.id.rvInvetory)
    RecyclerView recyclerViewInventory;
    GymDB db;
    FragmentedPurchase fragmentedPurchase = new FragmentedPurchase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        db = new GymDB(this, null);

        readDatabase();
        setUpRecyclerViews();
    }

    @Override
    public void redrawCall() {
        readDatabase();
        setUpRecyclerViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private void setUpRecyclerViews(){
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter1 = new RecyclerViewAdapterMerchandise(shopItems,this);
        recyclerViewMerchandise.setLayoutManager(layoutManager1);
        recyclerViewMerchandise.setAdapter(adapter1);
        recyclerViewMerchandise.addItemDecoration(decoration);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter2 = new RecyclerViewAdapterInventory(myItems);
        recyclerViewInventory.setLayoutManager(layoutManager2);
        recyclerViewInventory.setAdapter(adapter2);
        recyclerViewInventory.addItemDecoration(decoration);
    }

    //get the gym materials from the database and update the arraylists accordingly
    private void readDatabase(){
        shopItems = new ArrayList<>();
        myItems = new ArrayList<>();
        Cursor cursor = db.getInventory();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            int id = cursor.getColumnIndex(GymDB.COLUMN_ID);
            String material = cursor.getString(cursor.getColumnIndex(GymDB.COLUMN_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndex(GymDB.COLUMN_AMOUNT));
            Log.d("TAG_X", "    : "+id+" "+material+" "+quantity);
            if(quantity==0){
                shopItems.add(material);
            }else{
                shopItems.add(material);
                myItems.add(material+","+quantity);
            }
        }
        if(shopItems.size()==0){
            db.insert("");
            db.insert("Weights");
            db.insert("Medicine Balls");
            db.insert("Treadmills");
            db.insert("Dip Stations");
            db.insert("Yoga Mats");
            db.insert("Pull-up Bars");
            readDatabase();
        }
    }

    @Override
    public void onClickShop(String myCart) {
        //start a fragment
        Bundle bundle = new Bundle();
        bundle.putString("my_string",myCart);

        fragmentedPurchase.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_framelayout, fragmentedPurchase)
                .addToBackStack(fragmentedPurchase.getTag())
                .commit();
    }
}
