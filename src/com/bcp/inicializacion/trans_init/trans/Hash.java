package com.bcp.inicializacion.trans_init.trans;

import android.content.Context;
import android.database.Cursor;

import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.newpos.libpay.Logger;
import com.newpos.libpay.utils.ISOUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Julian on 7/06/2018.
 * Modificate by JM
 */

public class Hash {

    /**
     * Compute the SHA-1 hash of the given byte array
     * @param hashThis
     * @return
     */
    public String hashSha1(byte[] hashThis) {
        try {
            byte[] hash;
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            hash = md.digest(hashThis);
            return ISOUtil.byte2hex(hash);
        } catch (NoSuchAlgorithmException nsae) {
            Logger.logLine(Logger.LOG_EXECPTION, nsae.getStackTrace(), "SHA-1 algorithm is not available...");
            System.exit(2);
        }
        return null;
    }

    public void saveHash(String hash,Context context)
    {
        DBHelper db = new DBHelper(context, "hash", null, 1);
        db.openDb("hash");
        db.execSql("DELETE FROM HASH;");
        db.execSql("INSERT INTO HASH VALUES ('"+hash+"');");
        db.closeDb();
    }

    public String readHash(Context context)
    {
        String sha1="";
        DBHelper db = new DBHelper(context, "hash", null, 1);
        db.openDb("hash");

        Cursor c = db.rawQuery("SELECT SHA1 FROM HASH");

        if (c.moveToFirst()) {
            do {
                sha1 = c.getString(0);
            } while(c.moveToNext());
        }
        db.closeDb();

        return sha1;
    }
}
