package com.naughtyfrench.app;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import billing.IabHelper;
import billing.IabResult;
import billing.Inventory;

/**
 * Created by patriciaestridge on 6/2/14.
 */
public class BillingInventoryFragment extends Fragment {

    private IabHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initialiseBilling();
    }


    private void initialiseBilling() {
        if (mHelper != null) {
            return;
        }

        mHelper = new IabHelper(getActivity(), AppBillingInfo.getI());

        // remove
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {

                if (mHelper == null) {
                    return;
                }

                if (!result.isSuccess()) {
                    Log.e(getActivity().getApplicationInfo().name, "Problem setting up in-app billing: " + result.getMessage());
                    return;
                }

                mHelper.queryInventoryAsync(iabInventoryListener());
            }
        });
    }

    private IabHelper.QueryInventoryFinishedListener iabInventoryListener() {
        return new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                if (mHelper == null) {
                    return;
                }

                if (!result.isSuccess()) {
                    return;
                }

                Boolean purchaseModuleTwo = inventory.hasPurchase(AppBillingInfo.MODULE2); // Where G.SKU_PRO is your product ID (eg. permanent.ad_removal)

                AppBillingInfo.initializeStuff(purchaseModuleTwo);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }


}
