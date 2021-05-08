package com.bcp.transactions.callbacks;

public interface WaitResponseFallback {
    void getResponseTransFallback(int status, String[] args);
}
