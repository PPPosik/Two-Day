package com.example.khj_pc.twoday;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.scilab.forge.jlatexmath.core.AjLatexMath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.github.kbiakov.codeview.classifier.CodeProcessor;
import pub.devrel.easypermissions.EasyPermissions;

public class TexActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PICKFILE_RESULT_CODE = 1000;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private CustomViewPager mViewPager;

    private boolean fabImgFlag = true;

    PlaceholderFragment placeholderFragment = new PlaceholderFragment();
    ViewerPlaceholderFragment viewerPlaceholderFragment = new ViewerPlaceholderFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tex);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.icon_overflow));
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        AjLatexMath.init(this); // init library: load fonts, create paint, etc.
        CodeProcessor.init(this);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = placeholderFragment.getText();
                viewerPlaceholderFragment.setText(s);
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() == 0 ? 1 : 0, true);

                if(fabImgFlag){
                    fab.setImageResource(R.mipmap.left_arrow);
                }
                else{
                    fab.setImageResource(R.mipmap.right_arrow);
                }
                fabImgFlag = !fabImgFlag;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tex, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.get_tex) {
            if (EasyPermissions.hasPermissions(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestFile();
            } else {
                EasyPermissions.requestPermissions(this, "tex파일을 가져오기 위해서 권한이 필요합니다", 300, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            return true;
        }

        if(id == R.id.save_tex) {
            if (EasyPermissions.hasPermissions(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if(placeholderFragment.getText().length() > 0) {
                    saveFile();
                } else {
                    Toast.makeText(getApplicationContext(), "값을 입력하세요!", Toast.LENGTH_SHORT).show();
                }
            } else {
                EasyPermissions.requestPermissions(this, "tex파일을 저장하기 위해서 권한이 필요합니다", 400, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent,
                "tex 파일을 선택해주세요!"), PICKFILE_RESULT_CODE);
    }

    private void saveFile() {
        try {
            File file = new File( Environment.getExternalStorageDirectory() + "/result.tex");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(placeholderFragment.getText().getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), "성공적으로 저장했습니다!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String result;
            cursor = getContentResolver().query(contentUri,
                    null, null, null, null);

            if (cursor == null) {
                result = contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            return result;
        } catch (Exception e) {
            Log.e("fk", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == 300)
            requestFile();
        if(requestCode == 400)
            saveFile();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(requestCode == 300)
            EasyPermissions.requestPermissions(this, "tex파일을 가져오기 위해서 권한이 필요합니다", 300, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(requestCode == 400)
            EasyPermissions.requestPermissions(this, "tex파일을 저장하기 위해서 권한이 필요합니다", 400, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (EasyPermissions.hasPermissions(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                try {
                    Uri uri = data.getData();
                    if (uri == null)
                        throw new NullPointerException("I hate this");
                    String path = getPath(this, uri);
                    File file = new File(path);
                    String s = FileUtils.readFileToString(file, "UTF-8");
                    placeholderFragment.setText(s);
                    mViewPager.setCurrentItem(0, true);
                    Log.d("hi", uri.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "파일을 불러오는데 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                EasyPermissions.requestPermissions(this,
                        "ex파일을 가져오기 위해서 권한이 필요합니다", 300, android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return placeholderFragment;
            return viewerPlaceholderFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    public static class CustomViewPager extends ViewPager {
        private Boolean disable = true;

        public CustomViewPager(Context context) {
            super(context);
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return disable ? false : super.onInterceptTouchEvent(event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return disable ? false : super.onTouchEvent(event);
        }

        public void disableScroll(Boolean disable) {
            //When disable = true not work the scroll and when disble = false work the scroll
            this.disable = disable;
        }
    }
}
