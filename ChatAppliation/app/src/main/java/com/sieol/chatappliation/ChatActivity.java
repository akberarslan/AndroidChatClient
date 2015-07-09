package com.sieol.chatappliation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.messaging.Message;

import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mChatText;
    private TextView mChatBody;
    private Button Sendbtn;
    private ChatClient mChatClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

    }

    private void init() {
        mChatText = (EditText) findViewById(R.id.mChatText);
        Sendbtn = (Button) findViewById(R.id.btnSend);
        mChatBody = (TextView) findViewById(R.id.mChatBody);
        Sendbtn.setOnClickListener(this);
        registerChatClientToThisActivity();
    }

    private void registerChatClientToThisActivity() {
        mChatClient = new ChatClient();
        mChatClient.init(this, Session.myID, new ChatClient.MessageReceived() {
            @Override
            public void messageReceived(Message message) {

                mChatBody.setText(mChatBody.getText()+"From : " + message.getSenderId() + "\n"
                        + "Body " + message.getTextBody() + "\n"
                        + "Time " + message.getTimestamp().toString() + "\n");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatClient.terminate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSend:

                String msgToSend = mChatText.getText().toString();
                String toID = Session.toID;

                if (!(msgToSend.equals("") && toID.equals(""))) {
                    mChatClient.sendMessage(toID, msgToSend);
                    mChatBody.setText(mChatBody.getText()+"To : " + toID + "\n"
                            + "Body " + msgToSend + "\n"
                            + "Time " + new Date().toString() + "\n");
                } else
                    Toast.makeText(ChatActivity.this, R.string.errorText, Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
