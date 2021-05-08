package com.newpos.libpay.device.card;


public interface HandleCardManager {
    int SUCCESS = 0;
    int USER_CANCEL = 1;
    int TIMEOUT_ERROR = 2;
    int UNKNOWN_ERROR = -1;

    void onSearchResult(int var1, CardInfo cardInfo);
}
