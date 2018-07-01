package hanco.itsp.android.hanco1;

import android.content.Context;
import android.content.EntityIterator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class OCRActivity extends AppCompatActivity {

    private static final String TAG=OCRActivity.class.getSimpleName();
    public static final String TESS_DATA = "/tessdata";

    private TextView textView;
    private TessBaseAPI tessBaseAPI;
    private Uri outputFileDir;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory() + "/Tess";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        textView = (TextView) findViewById(R.id.ocrText);
        textView.setText("LolxD");
        prepareTessData();
        textView.setText("Lol");
        String imageFilePath = DATA_PATH + "/image" + "ocr.jpeg";
        outputFileDir = Uri.fromFile(new File(imageFilePath));
        startOCR(outputFileDir);


    }

    private void prepareTessData() {

        try {
            File dir = new File(DATA_PATH + TESS_DATA);

            if (!dir.exists())
                dir.mkdir();

            String fileList[] = getAssets().list("");

            for(String fileName : fileList){
                String pathToDataFile = DATA_PATH + TESS_DATA + "/" + fileName;

                if(! (new File(pathToDataFile).exists() ))
                {
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);

                    byte [] buff = new byte[1024];

                    int len;

                    while((len = in.read(buff)) > 0)
                    {
                        out.write(buff, 0, len);
                    }

                    in.close();
                    out.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startOCR(Uri imageUri)
    {
        textView.setText("HELLO!");

        try{

            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
            textView.setText("In startOCR");
            String result = this.getText(bitmap);

            String path=Environment.getExternalStorageDirectory().toString() + "/Tess";
            File file=new File(path, "result.txt");

            FileOutputStream stream= new FileOutputStream(file);
            try{
                stream.write(result.getBytes());
            } finally {
                stream.close();
            }
            textView.setText(result);

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    private String getText(Bitmap bitmap) {

        try{
            textView.setText("In getText");
            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        textView.setText("In getText, try block succeeded!");
        tessBaseAPI.init((DATA_PATH), "eng", TessBaseAPI.OEM_DEFAULT);
        textView.setText("Initialised!");
        if (bitmap==null) textView.setText(DATA_PATH);
        File temp= new File(DATA_PATH + "/image/" + "ocr.jpeg");
        tessBaseAPI.setImage(temp);
        textView.setText("Image SET!");
        String retStr = "No Result";
        textView.setText("Processing...");

        try{
            retStr = tessBaseAPI.getUTF8Text();
            textView.setText("Done!");
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

        tessBaseAPI.end();
        return retStr;
    }
}
