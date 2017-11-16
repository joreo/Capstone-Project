package com.udanano.pocketcloset2;

import android.content.Intent;
;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        static final int CAM_REQUEST = 1;

        //SQLiteOpenHelper dbhelper;
        SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //learned that if you comment out the line below, you get real ads
                //DO NOT DO THAT LOL
                .addTestDevice("238827E5D4D7C2CCE1B4E0AAC867E1AD")
                .build();

        adView.loadAd(adRequest);

        //moved from onSubmit to here
        ClothesDBOpenHelper dbHelper = new ClothesDBOpenHelper(this);
        //like the lesson todo 3 - GET WRITABLE DB ref using getwritabledatabase and store it in database (
        mDB = dbHelper.getWritableDatabase();
        //todo 4 for fake data - currently unneeded

        //todo 7 run getallClothes and store it in a cursor
        Cursor cursor = getAllClothes();

        //do we have data?
        String count = "SELECT count(*) FROM clothes";
        Cursor mCursor = mDB.rawQuery(count, null);
        mCursor.moveToFirst();
        int icount = mCursor.getInt(0);
        if(icount > 0) { Log.d("@@@Count = ", String.valueOf(icount)); }
        else {Log.d("@@@Count, maybe 0, = ", String.valueOf(icount)); }

            //testing data in the cursor
        int cursorCount = cursor.getCount();
        Log.d("@@@Cursor count", String.valueOf(cursorCount));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                cameras: how do they work?!
                https://www.youtube.com/watch?v=je9bdkdNQqg
                https://developer.android.com/training/camera/photobasics.html
                **/

                final Button submitButton = (Button) findViewById(R.id.btnSubmit);
                submitButton.setEnabled(true);
                submitButton.setClickable(true);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAM_REQUEST);
                    }
                }

//                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (camera_intent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(camera_intent, CAM_REQUEST); }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //disable the combobox/spinner when radio button sellected
        final Spinner mySpinner = (Spinner) findViewById(R.id.category_spinner);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.pics_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                switch(checkedId){
                    case R.id.selfie_radio:
                        mySpinner.setEnabled(false);
                        break;

                    case R.id.clothing_radio:
                        mySpinner.setEnabled(true);
                        break;
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);

        int id = item.getItemId();

        if (id == R.id.nav_everything) { //was nav_camera; if that helps
            // Handle the click, etc
            vf.setDisplayedChild(0);
            Log.i("@@@", "nav_camera clicked");
        } else if (id == R.id.nav_hats) {

        } else if (id == R.id.nav_torso) {

        } else if (id == R.id.nav_legs) {

        } else if (id == R.id.nav_feet) {

        } else if (id == R.id.nav_outerwear) {

        } else if (id == R.id.nav_accessories) {

        } else if (id == R.id.nav_misc) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException
    {

        File folder = new File("PocketCloset");
        if(!folder.exists())
        {
            folder.mkdir();
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PocketCloset_" + timeStamp + "_";
        Log.i("@@@timestamp: ", timeStamp);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();


        return image;

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //now: just a toast to show where to put your "after the pic was taken" code
        //later: do something with the pic you just took
        String picLocation = mCurrentPhotoPath;
        Log.e("@@@", mCurrentPhotoPath);
        Toast.makeText(this, picLocation, Toast.LENGTH_SHORT).show();

        ImageView imgPreview = (ImageView)findViewById(R.id.img_preview);

        Picasso.with(this)
                .load(mCurrentPhotoPath)
                .into(imgPreview);

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        vf.setDisplayedChild(1);
        Log.i("@@@", "vf code ran");

        GridView gridView = (GridView) findViewById(R.id.gridview_clothes);

        galleryAddPic();
    }

//what i've been trying http://stackoverflow.com/questions/9844061/android-how-do-i-enable-disable-a-checkbox-depending-on-a-radio-button-being-s
    public void onCheckedChanged(RadioGroup group, int checkedId){

       Spinner mySpinner = (Spinner) findViewById(R.id.category_spinner);

        switch(checkedId){
            case R.id.selfie_radio:
               mySpinner.setEnabled(false);
               break;

            case R.id.clothing_radio:
                mySpinner.setEnabled(true);
                break;
        }
    }

    public void onSubmit(View v){
        //when submit is pressed: show this toast
        Toast.makeText(getApplicationContext(), "I've been clicked!", Toast.LENGTH_SHORT).show();

        //get the text from the form
        //pic
        String picture = mCurrentPhotoPath;
        //desc
        final EditText descField = (EditText) findViewById(R.id.picture_description);
        String desc = descField.getText().toString();

        //cat
        //if radio button = selfie, cat=selfie
        //else cat = combo box section

        final Spinner feedbackSpinner = (Spinner) findViewById(R.id.category_spinner);
        final RadioButton feedbackRadio = (RadioButton) findViewById(R.id.clothing_radio);

        String cat;
        if (feedbackRadio.isChecked()) {
            cat = feedbackSpinner.getSelectedItem().toString();
            Toast.makeText(getApplicationContext(), "1 The cat is " + cat , Toast.LENGTH_SHORT).show();
        } else {
            cat = "Selfie";
            Toast.makeText(getApplicationContext(), "2 The cat is " + cat , Toast.LENGTH_SHORT).show();
        }
        //date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        Toast.makeText(getApplicationContext(), currentDate , Toast.LENGTH_SHORT).show();

        //do a DB add
        ClothesDBOpenHelper DB = new ClothesDBOpenHelper(this);

        //DB, path to clothes picture, description text box, cat (selfie or clothing), current date
        DB.addEntry(DB, picture, desc, cat, currentDate);

        //get all db info, place into a cursor
        Cursor cursor = getAllClothes();

        //testing data in the cursor
        int cursorCount = cursor.getCount();
        Log.d("@@@Cursor count 2", String.valueOf(cursorCount));

        //disable Submit button (enabled on FAB touch)
        final Button submitButton = (Button) findViewById(R.id.btnSubmit);
        submitButton.setEnabled(false);
        submitButton.setClickable(false);

        //clear the description box
        descField.getText().clear();

    }

    private Cursor getAllClothes() {

        return mDB.query(
                TableData.TableInfo.TABLE_CLOTHES,
                null,
                null,
                null,
                null,
                null,
                TableData.TableInfo.COLUMN_ID
        );
    }
}
