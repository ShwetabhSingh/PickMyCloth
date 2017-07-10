package exaample.com.pickmycloth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static exaample.com.pickmycloth.SecondScreen.GridView_ImagePath_Bookmarks;

/**
 * Created by Wonder 5 on 02-07-2017.
 */

public class Bookmrk extends AppCompatActivity {
    private Bitmap currentBitmapPants = null;
    ImageView imageView;
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);
       /* imageView = (ImageView) findViewById(R.id.imageView);
        getPantImage();*/
        getFromSdcard();
        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);

    }

    private void getPantImage() {
        ArrayList<String> f = new ArrayList<String>();// list of file paths
        File[] listFile;
        File path = new File(GridView_ImagePath_Bookmarks);
        if (path.exists()) {
            if (path.isDirectory()) {
                listFile = path.listFiles();
                for (int i = 0; i < listFile.length; i++) {
                    f.add(listFile[i].getAbsolutePath());
                }
            }
            if (currentBitmapPants != null)
                currentBitmapPants.recycle();
            currentBitmapPants = BitmapFactory.decodeFile(f.get(0));
            imageView.setImageBitmap(currentBitmapPants);
            //return currentBitmap;
        }
    }

    public void getFromSdcard() {
        File file = new File(GridView_ImagePath_Bookmarks);

        if (file.length() == 0) {
            // empty or doesn't exist
            Toast.makeText(this,"Nothing present,please add some cloths",Toast.LENGTH_LONG).show();
            return;
        } else {

            if (file.isDirectory()) {
                listFile = file.listFiles();


                for (int i = 0; i < listFile.length; i++) {

                    f.add(listFile[i].getAbsolutePath());

                }
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
               holder.checkBox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
        CheckBox checkBox;


    }

}
