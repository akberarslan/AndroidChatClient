package com.sieol.chatappliation;

import android.content.Context;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.List;

/**
 * Created by Akbar on 7/4/2015.
 */
public class ChatClient {

    //Register your application and get yours following credentials from here = https://www.sinch.com/


    private static final String APP_KEY = "0bbe49ce-19ff-410f-ab20-84cb8838156a";
    private static final String APP_SECRET = "HEIP7iBDs0m8THKux0h1iQ==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";

    private SinchClient sinchClient;
    private MessageClient mMessageClient;
    private MessageReceived mMessageReceived;

    public void init(Context context, String userId, MessageReceived messageReceived){

        sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .userId(userId)
                .build();

        sinchClient.setSupportMessaging(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.addSinchClientListener(mSinchClientListener);
        sinchClient.start();
        mMessageClient = sinchClient.getMessageClient();
        mMessageClient.addMessageClientListener(mMessageClientListener);
        mMessageReceived = messageReceived;
    }

    public void terminate(){
        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();
    }

    public void sendMessage(String to, String message){
        WritableMessage m = new WritableMessage(
                to,
                message);
        mMessageClient.send(m);

    }

    private SinchClientListener mSinchClientListener = new SinchClientListener() {
        public void onClientStarted(SinchClient client) {
        }

    public void onClientStopped(SinchClient client) {
    }

    public void onClientFailed(SinchClient client, SinchError error) {
    }

    public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
        }

    public void onLogMessage(int level, String area, String message) {
        }
    };

    public static interface  MessageReceived{
        void messageReceived(Message message);
    }

    private MessageClientListener mMessageClientListener =  new MessageClientListener() {
        @Override
        public void onIncomingMessage(MessageClient mMessageClient, Message message) {
            Log.d("MeTest", "onIncomingMessage: " + message.getTextBody());
            mMessageReceived.messageReceived(message);

        }

        @Override
        public void onMessageSent(MessageClient mMessageClient, Message message, String s) {
            Log.d("MeTest", "onMessageSent: " + message.getTextBody());

        }

        @Override
        public void onMessageFailed(MessageClient mMessageClient, Message message, MessageFailureInfo
        messageFailureInfo) {
            Log.d("MeTest", "onMessageFailed: " + message.getTextBody());

        }

        @Override
        public void onMessageDelivered(MessageClient mMessageClient, MessageDeliveryInfo
        messageDeliveryInfo) {
            Log.d("MeTest", "onMessageDelivered: " + messageDeliveryInfo.toString());

        }

        @Override
        public void onShouldSendPushData(MessageClient mMessageClient, Message message, List<PushPair> list) {

        }
    };

}
