package com.naughtyfrench.app;

/**
 * Created by patriciaestridge on 4/3/14.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private class Sample {
        private CharSequence title;
        private Class<? extends Activity> activityClass;
        private String price = "";
        private boolean enabled;

        private Sample(int titleResId, Class<? extends Activity> activityClass, String price, boolean enabled) {
            this.activityClass = activityClass;
            this.title = getResources().getString(titleResId);
            this.enabled = enabled;
            this.price = price;
        }

        @Override
        public String toString() {
            return title.toString();
        }
    }

    public static Sample[] mSamples;
    public static boolean module2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BillingInventoryFragment inventory = new BillingInventoryFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(inventory, "checkingPurchases");

        mSamples = new Sample[]{
                new Sample(R.string.french_cards_one, FrenchSlidesActivity.class, "Free!", true),
                new Sample(R.string.french_cards_two, FrenchSlidesActivity.class, "0.99", module2),
        };

        setListAdapter(new ArrayAdapter<Sample>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mSamples));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        if (mSamples[position].enabled) {
            Intent i = new Intent(MainActivity.this, mSamples[position].activityClass);
            i.putExtra("array_number", position);
            startActivity(i);
        } else {
            Intent i = new Intent(MainActivity.this, PurchaseActivity.class);
            startActivity(i);
        }
    }
}