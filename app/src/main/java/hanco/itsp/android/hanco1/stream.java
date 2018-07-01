package hanco.itsp.android.hanco1;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class stream extends AppCompatActivity {
    Button sendButton;
    VideoView preview;
    EditText ipAddress;
    MediaController mediaController;
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    private TCPClient mTcpClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        sendButton=findViewById(R.id.connectbutton);
        //preview=findViewById(R.id.preview);
        ipAddress=findViewById(R.id.ipaddress);
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);
        new connectTask().execute("");


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ipAddress.getText().toString();

                //add the text in the arrayList
                arrayList.add("c: " + message);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                ipAddress.setText("");
                //playstream(address);
            }
        });
    }
    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }


   /** private void playstream(String address) {
        Uri src=Uri.parse(address);
        if(src==null){
            Toast.makeText(stream.this,"src==NULL",Toast.LENGTH_SHORT).show();
        }
        else{
            preview.setVideoURI(src);
            mediaController=new MediaController(this);
            preview.setMediaController(mediaController);
            preview.start();
            Toast.makeText(stream.this,"preview started from"+src,Toast.LENGTH_SHORT);

        }
    }**/
}
