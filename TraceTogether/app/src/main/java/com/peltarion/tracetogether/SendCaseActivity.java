package com.peltarion.tracetogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

import android.content.Intent;
import android.os.Bundle;

import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SendCaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_case);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        String casePassword = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //Just for demo
        send(casePassword);
    }

    public static void send(String password){
        //long caseId = RpcClient.RegisteredId.INSTANCE.id.getId(); //Long.parseLong(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        // Do only once, and save id in local db
        //Id myId = stub.register(Empty.getDefaultInstance());

        // TODO(each app): Do some bluetooth stuff here, catch some other id:s
        //long valueFromBluetoothScanner = caseId;
        Id potentialCase = Id.newBuilder().setId(RpcClient.RegisteredId.INSTANCE.id.getId() + 1).build();

        // TODO(each app): Get confirmation in the UI (have the user enter a password)

        // Send (TODO retry if it fails??)
        Id myId = Id.newBuilder().setId(RpcClient.RegisteredId.INSTANCE.id.getId()).build();

        final PotentialCases potentialCases = PotentialCases.newBuilder() //
                .setPassword(CasePassword.newBuilder().setPassword(password).build()) //
                .setConfirmedId(myId).addPotentialCases(potentialCase).build();
        RpcClient.call(new Function<CaseNotifierServiceGrpc.CaseNotifierServiceBlockingStub, Void>() {
            @Override
            public Void apply(CaseNotifierServiceGrpc.CaseNotifierServiceBlockingStub caseNotifierServiceBlockingStub) {
                caseNotifierServiceBlockingStub.confirmedCase(potentialCases);
                return null;
            }
        });
    }
}
