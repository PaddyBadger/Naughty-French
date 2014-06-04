package com.naughtyfrench.app;

/**
 * Created by patriciaestridge on 6/2/14.
 */
public class AppBillingInfo {

    static final String MODULE2 = "module2";
    static final int BILLING_REQUEST_CODE= 1001;

    public static String getI() {
        String i = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzppNBB8SV0usovdSEhhY49LBXLYQxDFvNHV3Poi8/u/60qcKghhZwsaHobtEIJjRqPpSl5TuGlu0wh9jmij3vRxuvrOZ0f7bCSvN9cFi5YpPuRP6BKg9pv4AefrYEMNLZ5Ra8rqd5y1m0eVB5WeUs9sf98Z16aPw16HQy/wOLHxuEHxo5LblevkofhKE/qAoaWmPms+HNQxZZvn4VdmSTvOeg8FICPBuNroIZgnda1Llmij4ORUPnu7+KPGj4z8yEgopg7/7pJhDjjU1sSiD8vAzKD++5uRbD4K8YQoH3qxf7sZbnN0xUtXWV3kUSqhM3yTxuNiQNFy6Mw4NjR8A8QIDAQAB";
        return i;
    }

    public static void initializeStuff(Boolean module2) {
        MainActivity.module2 = module2;
    }
}
