package com.example.clientguirapidprototype;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.clientguirapidprototype.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import client.Client;
import usercommandhandler.UserCommandHandler;

public class MainActivity extends AppCompatActivity implements clientinterface.ClientInterface{
    TextView myMessageWindow;
    Client myClient;
    UserCommandHandler userCommandHandler;
    boolean connected;
    @Override
    public void update(String message) {
        Message msg = Message.obtain();
        msg.obj = message;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            String message = msg.obj.toString();
            myMessageWindow.append(message+"\n");
            return true;
        }
    });

    @Override
    public void setCommandHandler(UserCommandHandler commandHandler) {
        this.userCommandHandler=commandHandler;
    }

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myClient = new Client(this);
        userCommandHandler=new UserCommandHandler(myClient,this);
        myClient.setUserCommandHandler(userCommandHandler);
        connected = false;
        Thread myClientThread = new Thread(myClient);
        myClientThread.start();
        Thread myUserCommandHandlerThread = new Thread(userCommandHandler);
        myUserCommandHandlerThread.start();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        //button click listeners
        findViewById(R.id.button_update_port).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePortButtonHandler(v);
            }
        });

        findViewById(R.id.button_update_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIPButtonHandler(v);
            }
        });

        findViewById(R.id.toggleButton_connect_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectServerButtonHandler(v);
            }
        });

        findViewById(R.id.toggleButton_LED1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                led1ButtonHandler(v);
            }
        });
        findViewById(R.id.toggleButton_LED2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                led2ButtonHandler(v);
            }
        });
        findViewById(R.id.toggleButton_LED3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                led3ButtonHandler(v);
            }
        });
        findViewById(R.id.toggleButton_LED4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                led4ButtonHandler(v);
            }
        });
        myMessageWindow =findViewById(R.id.editTextTextMultiLine_serverMessageWindow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updatePortButtonHandler(View view){
        EditText myEditText = (EditText) findViewById(R.id.editText_portNumber);
        String myText = myEditText.getText().toString();
        myEditText.setText(myText);

        myClient.setPort(Integer.parseInt(myText));

        EditText myServerWindow = findViewById(R.id.editTextTextMultiLine_serverMessageWindow);
        myServerWindow.append("Port updated to:"+myText+"\n");
    }

    public void updateIPButtonHandler(View view){
        EditText myEditText = (EditText) findViewById(R.id.editText_ip);
        String myText = myEditText.getText().toString();
        myEditText.setText(myText);

        EditText myServerWindow = findViewById(R.id.editTextTextMultiLine_serverMessageWindow);
        myServerWindow.append("IP updated to:"+myText+"\n");
    }

    public void connectServerButtonHandler(View view){
        if(!connected){
            userCommandHandler.handleUserCommand("2");
            connected=true;
        }else{
            this.update("Server already connected");
        }
    }

    public void led1ButtonHandler(View view){
        if(connected){
            userCommandHandler.toggleLed(1);
        }
    }

    public void led2ButtonHandler(View view){
        if(connected){
            userCommandHandler.toggleLed(2);
        }
    }

    public void led3ButtonHandler(View view){
        if(connected){
            userCommandHandler.toggleLed(3);
        }
    }
    public void led4ButtonHandler(View view){
        if(connected){
            userCommandHandler.toggleLed(4);
        }
    }
}