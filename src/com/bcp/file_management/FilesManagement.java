package com.bcp.file_management;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import com.android.newpos.pay.R;
import com.bcp.inicializacion.trans_init.trans.Hash;
import com.newpos.libpay.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.bcp.definesbcp.Definesbcp.NAME_FOLDER_CTL_FILES;

public class FilesManagement {
    protected Context mContext;
    private File file;
    private Hash hash;
    private final String pathDefault = Environment.getExternalStorageDirectory()+ File.separator + "bcp";

    public FilesManagement(Context mContext) {
        this.mContext = mContext;
        hash = new Hash();
    }

    public String getPathDefault() {
        return pathDefault;
    }

    /**
     * Realiza la lectura de los archivo de configuracion del CTL
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] readFileBin(String fileName, Context context) {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(NAME_FOLDER_CTL_FILES, Context.MODE_PRIVATE);
        File f = new File(directory, fileName);

        try (InputStream ios = new FileInputStream(f)){
            byte[] data = new byte[1 << 20];
            int length = 0;
            length = ios.read(data);
            byte[] out = new byte[length];
            System.arraycopy(data,0,out,0,length);
            return out;
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), context.getString(R.string.archEliminado) + e);
            return new byte[0];
        }
    }

    public void deleteFolderContent(){
        File dir = new File(pathDefault);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < Objects.requireNonNull(children).length; i++)
            {
                File filedb = new File(dir, children[i]);
                Logger.logLine(Logger.LOG_GENERAL, "Archivo eliminado - deleteFolderContent" + filedb.delete());
            }
        }
    }

    public long calculateOffset(String fileName, boolean deleteFile)
    {
        long len=0;
        File dir= new File(pathDefault);
        file = new File(pathDefault + File.separator + fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            if (deleteFile)
                Logger.debug(mContext.getString(R.string.archEliminado) + file.delete());

            len = file.length();

        }
        else {
            try {
                if (!file.createNewFile()) {
                    Log.d("tag", mContext.getString(R.string.createFile));
                    return -1;
                }
            } catch (IOException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "createNewFile" + e.getMessage());
                return -1;
            }
        }
        return len;
    }

    public boolean writeFile (String field63, byte[] field64, int lenF64){

        String hashFld64 = hash.hashSha1(field64);
        if (hashFld64==null)
            return false;

        hashFld64 = hashFld64.toLowerCase();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)){
            if (hashFld64.equals(field63)) {
                fileOutputStream.write(field64, 0, lenF64);
            }
        }catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), mContext.getString(R.string.error)+ e);
            return false;
        }
        return true;
    }
}
