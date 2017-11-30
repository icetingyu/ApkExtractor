package hsu.icesimon.apkextractor.UI;

import android.graphics.drawable.Drawable;

/**
 * Created by simon on 2017/11/29.
 */

public class AppInfo {
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public boolean isSystemApp = false;

    public boolean getSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}