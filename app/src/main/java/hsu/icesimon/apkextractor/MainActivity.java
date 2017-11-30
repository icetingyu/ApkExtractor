package hsu.icesimon.apkextractor;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hsu.icesimon.apkextractor.UI.AppInfo;
import hsu.icesimon.apkextractor.UI.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnListFragmentInteractionListener {

    ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter.OnListFragmentInteractionListener mListener;
    private Toolbar toolbar;
    public static boolean showAlsoSystemApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        mListener = this;
        getInstalledAPPList();

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(appInfoList, mListener);
        recyclerView.setAdapter(recyclerViewAdapter);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                layoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_system) {
            if (showAlsoSystemApp) {
                showAlsoSystemApp = false;
                item.setTitle(getString(R.string.show_system_app));
            } else {
                showAlsoSystemApp = true;
                item.setTitle(getString(R.string.show_install_only));
            }
            recyclerViewAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    private void grabAPKpathAndCopy(AppInfo appinfo) {
        BufferedReader reader = null;
        String content = "";
        String installPath = "";
        String command_grab_apk_path = "pm path "+ appinfo.getPname();
        Log.d("command_grab_apk_path : " + command_grab_apk_path);

        try {
            Process process = Runtime.getRuntime().exec(command_grab_apk_path);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer output = new StringBuffer();
            int read;
            char[] buffer = new char[4096];
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            content = output.toString();
            content = content.replace("\n","").replace("\t","").replace("\r", "");
            Log.d("Result : " + content);
            // Result will be "package:" + apk path;
            installPath = content.replace("package:", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("installPath : " + installPath+"<<");

        adbshellCopyApk(installPath, appinfo);
    }

    private void adbshellCopyApk(String input, AppInfo appinfo) {
        BufferedReader reader = null;
        String content;
        String outputFilePath = "/sdcard/"+appinfo.getAppname()+"_"+appinfo.versionName+".apk";
        String command_copy_apk = "cp "+ input.trim()+" "+outputFilePath;
        Log.d("Copy Command : "+command_copy_apk);
        try {

            Process process = Runtime.getRuntime().exec(new String[]{"cp",input.trim(),outputFilePath});
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer output = new StringBuffer();
            int read;
            char[] buffer = new char[4096];
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            content = output.toString();
            Log.d("oo o o o : " + content);
        } catch (IOException e) {
            Log.d("IO error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d("IO error2: " + e.getMessage());

                    e.printStackTrace();
                }
            }
        }
        Log.d("APK be extracted: "+ new File(outputFilePath).exists());
    }

    private void getInstalledAPPList() {
        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < apps.size(); i++) {
            PackageInfo p = apps.get(i);

            AppInfo newInfo = new AppInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());

            if((p.applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                newInfo.setSystemApp(true);
            } else {
                newInfo.setSystemApp(false);
            }
            appInfoList.add(newInfo);
        }
    }

    public void onListFragmentInteraction(AppInfo item) {
        Log.d("AppInfo : " + item.getAppname() + ", " + item.getVersionName());
        grabAPKpathAndCopy(item);
    }
}
