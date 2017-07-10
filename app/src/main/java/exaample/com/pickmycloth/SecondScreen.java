package exaample.com.pickmycloth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static exaample.com.pickmycloth.Pants.GridView_ImagePath_Pants;
import static exaample.com.pickmycloth.Shirts.GridView_ImagePath;


/**
 * Created by Wonder 5 on 29-06-2017.
 */

public class SecondScreen extends AppCompatActivity {
    Button pants, shirts;
    ImageView myshirts, mypants, dislike, bookmark;
    TextView notice;
    File[] listFile;
    File[] listFileShirts;
    private Bitmap currentBitmap = null;
    private Bitmap currentBitmapPants = null;
    public static final String GridView_ImagePath_Bookmarks = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GridViewBookmarks/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondscreen);

        myshirts = (ImageView) findViewById(R.id.myshirt);
        mypants = (ImageView) findViewById(R.id.mypants);
        dislike = (ImageView) findViewById(R.id.dislike);
        bookmark = (ImageView) findViewById(R.id.star);
        notice = (TextView) findViewById(R.id.notice);

        pants = (Button) findViewById(R.id.pants);
        shirts = (Button) findViewById(R.id.shirts);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean permOk = checkPermissions();

            buttonMethod();
            getImage();
            getPantImage();
        }
        if (listFile != null && listFileShirts != null) {
            if (listFile.length != 0 && listFileShirts.length != 0) {

                dislike.setVisibility(View.VISIBLE);
                bookmark.setVisibility(View.VISIBLE);
            } else {
                if (listFile != null && listFile.length < 1 && listFileShirts != null || listFileShirts.length < 1) {
                    notice.setText("Your  drawer is empty please fill it  up with some cloths.");
                } else if (listFileShirts != null && listFileShirts.length < 1) {
                    notice.setText("Your Shirt drawer is empty please fill it up with some shirts.");
                } else if (listFile != null && listFile.length < 1) {
                    notice.setText("Your Pants drawer is empty please fill it up with some pants.");
                }
            }
        } else {
            notice.setText("Your  drawer is empty please fill it  up with some Pants/Shirts");
            dislike.setVisibility(View.GONE);
            bookmark.setVisibility(View.GONE);
        }


        if (listFile != null && listFileShirts != null)

        {

            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getImage();
                    getPantImage();
                    // Toast.makeText(SecondScreen.this, "clicked", Toast.LENGTH_SHORT).show();
                }
            });

            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // View linearLayout = (View) findViewById(R.id.bothimage);
                    myshirts.buildDrawingCache();
                    Bitmap bmapshirt = myshirts.getDrawingCache();
                    mypants.buildDrawingCache();
                    Bitmap bmappants = mypants.getDrawingCache();

                    // Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    createSingleImageFromMultipleImages(bmapshirt, bmappants);

                }
            });
        } else

        {
            Toast.makeText(this, "Please fill up your drawer", Toast.LENGTH_LONG).show();
            dislike.setEnabled(false);
            bookmark.setEnabled(false);
        }

    }


    Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight() * 2, firstImage.getConfig());
        Canvas canvas = new Canvas(result);

        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 0f, firstImage.getHeight(), null);
        // createImageFromBitmap(result);
        storeImage(result);
        Intent intent = new Intent(this, Bookmrk.class);
        startActivity(intent);
      /*  // mypants.setImageBitmap(result);
         Intent intent = new Intent(this,Bookmrk.class);
         Bundle extras = new Bundle();
       //  extras.putParcelable("imagebitmap", result);
         intent.putExtras(extras);
         startActivity(intent);*/


        return result;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {

            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private File getOutputMediaFile() {

        File mediaStorageDir = new File(GridView_ImagePath_Bookmarks);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }


    private void buttonMethod() {
        pants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondScreen.this, Pants.class);
                startActivity(intent);
                finish();

            }
        });
        shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondScreen.this, Shirts.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void getPantImage() {
        ArrayList<String> f = new ArrayList<String>();// list of file paths
        listFile = new File[0];


        File path = new File(GridView_ImagePath_Pants);
        if (path.length() == 0) {
            // empty or doesn't exist
            return;
        } else {
            // exists and is not empty


            if (path.exists()) {
                if (path.isDirectory()) {
                    listFile = path.listFiles();
                    if (listFile != null) {

                        for (int i = 0; i < listFile.length; i++) {

                            f.add(listFile[i].getAbsolutePath());

                        }


                        Random generator = new Random();
                        int randomIndex = generator.nextInt(listFile.length);

                        if (currentBitmapPants != null)
                            currentBitmapPants.recycle();
                        currentBitmapPants = BitmapFactory.decodeFile(f.get(randomIndex));
                        mypants.setImageBitmap(currentBitmapPants);
                        //return currentBitmap;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void getImage() {
        ArrayList<String> f = new ArrayList<String>();// list of file paths
        // listFileShirts = new File[0];

        File path = new File(GridView_ImagePath);
        if (path.exists()) {
            if (path.equals(null) || path.length() == 0) {

                return;
            } else {

                if (path.exists()) {
                    if (path.isDirectory()) {
                        listFileShirts = path.listFiles();
                        if (listFileShirts != null) {

                            for (int i = 0; i < listFileShirts.length; i++) {
                                f.add(listFileShirts[i].getAbsolutePath());
                            }


                            Random generator = new Random();
                            int randomIndex = generator.nextInt(listFileShirts.length);

                            if (currentBitmap != null)
                                currentBitmap.recycle();
                            currentBitmap = BitmapFactory.decodeFile(f.get(randomIndex));
                            myshirts.setImageBitmap(currentBitmap);
                            //return currentBitmap;
                        } else {
                            return;
                        }
                    }
                    //  return currentBitmap;
                }
            }
        }
    }

    public Boolean checkPermissions() {
        Boolean retVal = true;
        // check and request storage access permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            retVal = false;
        }
        return retVal;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show();
                    buttonMethod();
                    getImage();
                    getPantImage();

                } else {
                    Toast.makeText(this, "Permission denied ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}



