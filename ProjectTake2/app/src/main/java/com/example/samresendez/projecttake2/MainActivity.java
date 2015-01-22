package com.example.samresendez.projecttake2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.sync.android.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {
    private String appKey = "j0vplqbkw8j6pxm";
    private String appSecret = "pzha657wwo27mi6";
    private static final int LOAD_IMAGE_REQUEST_CODE = 1;
    private DbxFileSystem fileSystem;
    private static final int REQUEST_CODE = 1337;
    private DbxAccount dbxAccount;
    private DbxPath path;
    private DbxFile dbxPicture;
    private ArrayList<String> StringList = new ArrayList<>();
    public final static String MESSAGE = "com.example.samresendez.MESSAGE";
    private DbxPath imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbxAccountManager manager = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
        manager.startLink(this, REQUEST_CODE);
        dbxAccount = manager.getLinkedAccount();
    }

    //Sets up DropBox Connection
    private void DbxSetup() {
        try {
            fileSystem = DbxFileSystem.forAccount(dbxAccount);
        } catch (Exception e) {
            Log.e("RIP Dozer", "");
            Context context = getApplicationContext();
            CharSequence text = "Could not open fileSystem :(";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void sendFile(View view) {
        Intent addPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(addPhotoIntent, LOAD_IMAGE_REQUEST_CODE);
    }

    public void getFileList(View view) {
        //TODO:Get the entire method finished :P

    }

    public DbxPath getNewImagePath() {
        int pathCounter = 0;
        try {
            while (true) {
                path = new DbxPath("/image/Picture" + pathCounter + ".jpg");
                if (!fileSystem.isFile(path)) {
                    break;
                }
                pathCounter++;
            }

            //TODO: decide what to do with this code
           /* List<DbxFileInfo> dbxFileInfos = fileSystem.listFolder(new DbxPath("/"));
            int currentIndex = dbxFileInfos.size();
            DbxFileInfo currentDbxFile = dbxFileInfos.get(currentIndex);
            DbxPath currentPath = currentDbxFile.path;
            for(DbxFileInfo info : dbxFileInfos){ */

        } catch (Exception e) {
            path = new DbxPath("/images" + pathCounter + ".jpg");
            Context context = getApplicationContext();
            CharSequence text = "Did not do the thing";
            int length = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, length);
            toast.show();
        }
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                DbxSetup();
                if (resultCode == Activity.RESULT_OK) {
                    Context context = getApplicationContext();
                    CharSequence text = "Syncing";
                    int length = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, length);
                    toast.show();

                    try {
                        Uri theImage = data.getData();
                        Log.e("test", "Started the try");
                        Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), theImage);
                        Log.e("test", photo.toString());
                        dbxPicture = fileSystem.create(getNewImagePath());
                        Log.e("test", "Log");
                        FileOutputStream imageOutput = dbxPicture.getWriteStream();
                        Log.e("Log", dbxPicture.toString());

                        photo.compress(Bitmap.CompressFormat.JPEG, 100, imageOutput);
                        Log.e("fun", "times");

                        imageOutput.close();
                        dbxPicture.close();

                    } catch (DbxException e) {
                        context = getApplicationContext();
                        text = "Could not do the dropbox thing";
                        length = Toast.LENGTH_LONG;
                        toast = Toast.makeText(context, text, length);
                        toast.show();
                    } catch (IOException e) {
                        context = getApplicationContext();
                        text = "Could not do the java thing";
                        length = Toast.LENGTH_LONG;
                        toast = Toast.makeText(context, text, length);
                        toast.show();
                    }
                }
            case ListViewActivity.LIST_REQUEST_CODE:
                String pathString = data.getStringExtra("String Name");
                Log.e("String Log", pathString);
                try {
                   DbxPath dlPath = new DbxPath(pathString);
                    DbxFile file = fileSystem.open(dlPath);
                    FileInputStream input = file.getReadStream();
                    
                } catch (Exception e) {
                    Log.e("test", "Halp, I can't Read");
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.downloadButton:
                List<DbxFileInfo> itemList;
                path = getNewImagePath();
                try {
                    DbxPath path2 = path.getParent();
                    Intent intent = new Intent(this, ListViewActivity.class);
                    itemList = fileSystem.listFolder(path2);
                    for (DbxFileInfo file : itemList) {
                        String string = file.path.toString();
                        StringList.add(string);
                    }
                    intent.putExtra(MESSAGE, StringList);
                    startActivity(intent);
                    Log.e("String List Log", StringList.toString());
                } catch (Exception e) {
                    Log.e("adsf", "adsf");
                }

        }
        return super.onOptionsItemSelected(item);
    }
    //TODO: Code Disposal
    /*public String getOutDbxFileInfo() {
        try {
            FileInputStream infoInput = dbxPicture.getReadStream();
            Log.e("test", infoInput.toString());
            infoInput.close();
            return infoInput.toString();
        } catch (DbxException e) {
            Log.e("DBXEXCEPT", "hmm?");
            return null;
        } catch (IOException e) {
            Log.e("IOExcept", "hm?");
            return null;
        } catch (DbxFile.StreamExclusionException e) {
            Log.e("Funny one", "hmmm?");
            return null;
        }


    public String getDbxFileName(DbxPath dbxPath){
        String path3 = dbxPath.toString();
        int lastSlash = path3.lastIndexOf("/");
        int lastDot = path3.lastIndexOf(".");
        String fileName = path3.substring(lastSlash + 1, lastDot);
        return fileName;
    }*/

}
