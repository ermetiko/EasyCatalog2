package com.elfosoftware.easycatalog;

import android.app.Application;
import java.io.File;

/**
 * Created by canfra00 on 11/03/2016.
 */

public class MyApplication extends Application {

    private File immaginiPath;

    public File getImmaginiPath() {
        return immaginiPath;
    }

    public void setImmaginiPath(File v) {
        this.immaginiPath = v;
    }
}