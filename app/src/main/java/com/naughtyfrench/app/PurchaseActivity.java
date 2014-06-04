package com.naughtyfrench.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import billing.IabHelper;
import billing.IabResult;
import billing.Purchase;

/**
 * Created by patriciaestridge on 6/3/14.
 */
public class PurchaseActivity extends Activity {

    // Billing helper object
    private IabHelper mHelper;
    private boolean mBillingServiceReady;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_purchase);

        // Initialise buy buttons
        Button buyBtn = (Button) findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonUpgradeClicked();
            }
        });
        initialiseBilling();
    }

    private void initialiseBilling() {
        if (mHelper != null) {
            return;
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        mHelper = new IabHelper(this, AppBillingInfo.getI());

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) {
                    return;
                }

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.i("Problem setting up in-app billing: ","" + result.getMessage());
                    return;
                }

                // IAB is fully set up.
                mBillingServiceReady = true;
            }
        });
    }


    // User clicked the "Upgrade to Premium" button.
    public void onButtonUpgradeClicked() {
        if (!mBillingServiceReady) {
            Toast.makeText(PurchaseActivity.this, "Purchase requires Google Play Store (billing) on your Android.", Toast.LENGTH_LONG).show();
            return;
        }

        String payload = "purchase"+(AppBillingInfo.MODULE2); // This is based off your own implementation.
        mHelper.launchPurchaseFlow(PurchaseActivity.this, AppBillingInfo.MODULE2, AppBillingInfo.BILLING_REQUEST_CODE, mPurchaseFinishedListener, payload);
    }



    /**
     * When In-App billing is done, it'll return information via onActivityResult().
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHelper == null) {
            return;
        }

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    /**
     * Very important
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }



    // Callback for when a purchase is finished
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                return;
            }

            // Don't complain if cancelling
            if (result.getResponse() == IabHelper.IABHELPER_USER_CANCELLED) {
                return;
            }

            if (!result.isSuccess()) {
                Log.e("Error purchasing: ","" + result.getMessage());
                return;
            }

            // Purchase was success! Update accordingly
            if (purchase.getSku().equals(AppBillingInfo.MODULE2)) {
                Toast.makeText(PurchaseActivity.this, "Thank you for purchasing!", Toast.LENGTH_LONG).show();

                Boolean purchaseModuleTwo = true;
                AppBillingInfo.initializeStuff(purchaseModuleTwo);
            }
        }
    };
}
