////
////
//////
//////import android.bluetooth.BluetoothAdapter;
//////import android.bluetooth.BluetoothDevice;
//////import android.content.Intent;
//////import android.graphics.drawable.Drawable;
//////import android.os.Bundle;
//////import android.support.annotation.Nullable;
//////import android.support.v7.app.ActionBar;
//////import android.view.Menu;
//////import android.view.MenuItem;
//////import android.view.View;
//////import android.widget.AdapterView;
//////import android.widget.ArrayAdapter;
//////import android.widget.Button;
//////import android.widget.ListView;
//////import android.widget.SpinnerAdapter;
//////import android.widget.TextView;
//////import android.widget.Toast;
//////import java.util.ArrayList;
//////import java.util.Set;
//////
//////
//////    public class BTcontrol extends ActionBar {
//////        //widgets
//////        Button btnPaired;
//////        ListView devicelist;
//////        //Bluetooth
//////        private BluetoothAdapter myBluetooth = null;
//////        private Set<BluetoothDevice> pairedDevices;
//////        public static String EXTRA_ADDRESS = "device_address";
//////
//////        protected void onCreate(Bundle savedInstanceState) {
//////            super.onCreate(savedInstanceState);
//////            setContentView(R.layout.bluetooth_menu);
//////
//////            //Calling widgets
//////            devicelist = (ListView) (R.id.deviceList);
//////
//////            //if the device has bluetooth
//////            myBluetooth = BluetoothAdapter.getDefaultAdapter();
//////
//////            if (myBluetooth == null) {
//////                //Show a mensag. that the device has no bluetooth adapter
//////                Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
//////
//////                //finish apk
//////                finish();
//////            } else if (!myBluetooth.isEnabled()) {
//////                //Ask to the user turn the bluetooth on
//////                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//////                startActivityForResult(turnBTon, 1);
//////            }
//////
//////            btnPaired.setOnClickListener(new View.OnClickListener() {
//////                @Override
//////                public void onClick(View v) {
//////                    pairedDevicesList();
//////                }
//////            });
//////
//////        }
//////
//////        private void pairedDevicesList() {
//////            pairedDevices = myBluetooth.getBondedDevices();
//////            ArrayList list = new ArrayList();
//////
//////            if (pairedDevices.size() > 0) {
//////                for (BluetoothDevice bt : pairedDevices) {
//////                    list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
//////                }
//////            } else {
//////                Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
//////            }
//////
//////            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
//////            devicelist.setAdapter(adapter);
//////            devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
//////
//////        }
//////
//////        private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
//////            public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
//////                // Get the device MAC address, the last 17 chars in the View
//////                String info = ((TextView) v).getText().toString();
//////                String address = info.substring(info.length() - 17);
//////
//////                // Make an intent to start next activity.
//////                Intent i = new Intent(DeviceList.this, ledControl.class);
//////
//////                //Change the activity.
//////                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
//////                startActivity(i);
//////            }
//////        };
//////
//////
//////        @Override
//////        public boolean onCreateOptionsMenu(Menu menu) {
//////            // Inflate the menu; this adds items to the action bar if it is present.
//////            getMenuInflater().inflate(R.menu.menu_device_list, menu);
//////            return true;
//////        }
//////
//////        @Override
//////        public boolean onOptionsItemSelected(MenuItem item) {
//////            // Handle action bar item clicks here. The action bar will
//////            // automatically handle clicks on the Home/Up button, so long
//////            // as you specify a parent activity in AndroidManifest.xml.
//////            int id = item.getItemId();
//////
//////            //noinspection SimplifiableIfStatement
//////            if (id == R.id.action_settings) {
//////                return true;
//////            }
//////
//////            return super.onOptionsItemSelected(item);
//////        }
//////
//////        @Override
//////        public void setCustomView(View view) {
//////
//////        }
//////
//////        @Override
//////        public void setCustomView(View view, LayoutParams layoutParams) {
//////
//////        }
//////
//////        @Override
//////        public void setCustomView(int i) {
//////
//////        }
//////
//////        @Override
//////        public void setIcon(int i) {
//////
//////        }
//////
//////        @Override
//////        public void setIcon(Drawable drawable) {
//////
//////        }
//////
//////        @Override
//////        public void setLogo(int i) {
//////
//////        }
//////
//////        @Override
//////        public void setLogo(Drawable drawable) {
//////
//////        }
//////
//////        @Override
//////        public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, OnNavigationListener onNavigationListener) {
//////
//////        }
//////
//////        @Override
//////        public void setSelectedNavigationItem(int i) {
//////
//////        }
//////
//////        @Override
//////        public int getSelectedNavigationIndex() {
//////            return 0;
//////        }
//////
//////        @Override
//////        public int getNavigationItemCount() {
//////            return 0;
//////        }
//////
//////        @Override
//////        public void setTitle(CharSequence charSequence) {
//////
//////        }
//////
//////        @Override
//////        public void setTitle(int i) {
//////
//////        }
//////
//////        @Override
//////        public void setSubtitle(CharSequence charSequence) {
//////
//////        }
//////
//////        @Override
//////        public void setSubtitle(int i) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayOptions(int i) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayOptions(int i, int i1) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayUseLogoEnabled(boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayShowHomeEnabled(boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayHomeAsUpEnabled(boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayShowTitleEnabled(boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void setDisplayShowCustomEnabled(boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void setBackgroundDrawable(@Nullable Drawable drawable) {
//////
//////        }
//////
//////        @Override
//////        public View getCustomView() {
//////            return null;
//////        }
//////
//////        @Nullable
//////        @Override
//////        public CharSequence getTitle() {
//////            return null;
//////        }
//////
//////        @Nullable
//////        @Override
//////        public CharSequence getSubtitle() {
//////            return null;
//////        }
//////
//////        @Override
//////        public int getNavigationMode() {
//////            return 0;
//////        }
//////
//////        @Override
//////        public void setNavigationMode(int i) {
//////
//////        }
//////
//////        @Override
//////        public int getDisplayOptions() {
//////            return 0;
//////        }
//////
//////        @Override
//////        public Tab newTab() {
//////            return null;
//////        }
//////
//////        @Override
//////        public void addTab(Tab tab) {
//////
//////        }
//////
//////        @Override
//////        public void addTab(Tab tab, boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void addTab(Tab tab, int i) {
//////
//////        }
//////
//////        @Override
//////        public void addTab(Tab tab, int i, boolean b) {
//////
//////        }
//////
//////        @Override
//////        public void removeTab(Tab tab) {
//////
//////        }
//////
//////        @Override
//////        public void removeTabAt(int i) {
//////
//////        }
//////
//////        @Override
//////        public void removeAllTabs() {
//////
//////        }
//////
//////        @Override
//////        public void selectTab(Tab tab) {
//////
//////        }
//////
//////        @Nullable
//////        @Override
//////        public Tab getSelectedTab() {
//////            return null;
//////        }
//////
//////        @Override
//////        public Tab getTabAt(int i) {
//////            return null;
//////        }
//////
//////        @Override
//////        public int getTabCount() {
//////            return 0;
//////        }
//////
//////        @Override
//////        public int getHeight() {
//////            return 0;
//////        }
//////
//////        @Override
//////        public void show() {
//////
//////        }
//////
//////        @Override
//////        public void hide() {
//////
//////        }
//////
//////        @Override
//////        public boolean isShowing() {
//////            return false;
//////        }
//////
//////        @Override
//////        public void addOnMenuVisibilityListener(OnMenuVisibilityListener onMenuVisibilityListener) {
//////
//////        }
//////
//////        @Override
//////        public void removeOnMenuVisibilityListener(OnMenuVisibilityListener onMenuVisibilityListener) {
//////
//////        }
//////    }
//////
//////
////
//////package com.stevensit.www.cpe556_interface_app;
//////
//////
//////import android.bluetooth.BluetoothAdapter;
//////import android.bluetooth.BluetoothDevice;
//////import android.content.Intent;
//////import android.os.Bundle;
//////import android.support.v7.app.AppCompatActivity;
//////import android.view.View;
//////import android.widget.AdapterView;
//////import android.widget.ArrayAdapter;
//////import android.widget.Button;
//////import android.widget.ListView;
//////import android.widget.TextView;
//////import android.widget.Toast;
//////
//////import java.util.ArrayList;
//////import java.util.Set;
//////
//////
//////public class DeviceList extends AppCompatActivity
//////{
//////    //widgets
//////    Button btnPaired;
//////    ListView devicelist;
//////    //Bluetooth
//////    private BluetoothAdapter myBluetooth ;
//////    private Set<BluetoothDevice> pairedDevices;
//////    public static String EXTRA_ADDRESS = "device_address";
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState)
//////    {
//////        super.onCreate(savedInstanceState);
//////
//////
//////        //Calling widgets
//////        btnPaired = (Button)findViewById(R.id.btnPair);
//////        devicelist = (ListView)findViewById(R.id.deviceList);
//////
//////        //if the device has bluetooth
//////        myBluetooth = BluetoothAdapter.getDefaultAdapter();
//////
//////        if(myBluetooth == null)
//////        {
//////            //Show a mensag. that the device has no bluetooth adapter
//////            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
//////
//////            //finish apk
//////            finish();
//////        }
//////        else if(!myBluetooth.isEnabled())
//////        {
//////            //Ask to the user turn the bluetooth on
//////            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//////            startActivityForResult(turnBTon,1);
//////        }
//////
//////        btnPaired.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View v)
//////            {
//////                pairedDevicesList();
//////            }
//////        });
//////
//////    }
//////
//////    private void pairedDevicesList()
//////    {
//////        pairedDevices = myBluetooth.getBondedDevices();
//////        ArrayList list = new ArrayList();
//////
//////        if (pairedDevices.size()>0)
//////        {
//////            for(BluetoothDevice bt : pairedDevices)
//////            {
//////                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
//////            }
//////        }
//////        else
//////        {
//////            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
//////        }
//////
//////        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
//////        devicelist.setAdapter(adapter);
//////        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
//////
//////    }
//////
//////    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
//////    {
//////        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
//////        {
//////            // Get the device MAC address, the last 17 chars in the View
//////            String info = ((TextView) v).getText().toString();
//////            String address = info.substring(info.length() - 17);
//////
//////            // Make an intent to start next activity.
//////            Intent i = new Intent(DeviceList.this, MainActivity.class);
//////
//////            //Change the activity.
//////            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
//////            startActivity(i);
//////        }
//////    };
//////
//////}
////
////package com.stevensit.www.cpe556_interface_app;
////
////
////import android.annotation.SuppressLint;
////import android.bluetooth.BluetoothAdapter;
////import android.bluetooth.BluetoothDevice;
////import android.bluetooth.BluetoothSocket;
////import android.content.Intent;
////import android.os.Handler;
////import android.util.Pair;
////import android.view.View;
////import android.widget.ImageButton;
////import android.widget.Toast;
////
////import java.io.IOException;
////import java.io.OutputStream;
////import java.util.Set;
////import java.util.UUID;
////
////public class BTControl extends MainActivity{
////
////
////    //private Button btnBTConnect = findViewById(R.id.btnBluetooth);
////    boolean btConnectionStatus = false;
////    private String name=null;
////    protected static BluetoothSocket btSocket = null;
////    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
////    public BTControl(){}
////     public ImageButton btImage;
////
////    @SuppressLint({"HardwareIds"})
////    public void connectBluetooth() throws IOException {  // bluetooth connection handler
////
////
////        if (btConnectionStatus) {
////            System.out.println("msg: " + "BT is already connected");
////            showToastMSG("Information Message:", "BT is already paired");
////        }else {
////
////            BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
////            System.out.println("msg: "+"bt_device_name " + myBluetooth);
////            if (myBluetooth == null)
////                Toast.makeText(getApplicationContext(), "The BT device is not detected or not compatible", Toast.LENGTH_SHORT).show();
////            else if (!myBluetooth.isEnabled()) {
////                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////                startActivityForResult(turnOn, 0);
////                Toast.makeText(getApplicationContext(), "BT is Turned on", Toast.LENGTH_LONG).show();
////            } else {
////                String address = myBluetooth.getAddress();
////                Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
////
////                if (pairedDevices.size() > 0) {
////                    for (BluetoothDevice bt : pairedDevices) {
////                        address = bt.getAddress();
////                        name = bt.getName();
////                    }
////                }
////
////                //  myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
////                BluetoothDevice btDevice = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
////                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create paired connection
////                System.out.println(BluetoothAdapter.STATE_CONNECTED);
////                new Handler().postDelayed(new Runnable() { // add a delay before connecting the bt socket to the paired device
////                    @Override
////                    public void run() {
////                        try {
////                            btSocket.connect();
////                        } catch (IOException e) {
////                            //   Toast.makeText(getApplicationContext(),"BT connection error\n" +e.getMessage(), Toast.LENGTH_SHORT).show();
////
////                            try {
////                                btSocket.close();
////                            } catch (IOException e1) {
////                                Toast.makeText(getApplicationContext(), "BT connection error\n" + e1.getMessage(), Toast.LENGTH_SHORT).show();
////                                onBackPressed();
////                            }
////                        }
////                    }
////                }, 500);
////
////                btConnectionStatus=true;
////                btConnectedMSG();
////            }
////        }
////    }
////
////    public static void sendToBtStream(String streamChr){  // send the selected output to bluetooth output socket
////        try{
////            OutputStream btOutputStream = btSocket.getOutputStream();
////            if (!streamChr.isEmpty() && btOutputStream !=null){
////                btOutputStream.write(streamChr.getBytes());
////                System.out.println("msg: "+streamChr);
////                System.out.println("msg: "+"bt socket current output: "+btSocket.toString());
////                System.out.println("msg: "+"bt stream: "+btSocket.getOutputStream());
////                System.out.println("====================================================================");
////            }
////        }
////        catch (Exception e)
////        {
////            // showToastMSG("BT stream error msg",e.getMessage());
////        }
////    }
////
////    @SuppressLint("SetTextI18n")
////    public void btConnectedMSG (){  // display bluetooth connection messages and set the indicators
////        Toast.makeText(getApplicationContext(), "Now connected to: "+name, Toast.LENGTH_SHORT).show();
////        btImage.setVisibility(View.VISIBLE);
////        robotReadyStatus.setVisibility(View.VISIBLE);
////        btImage.setActivated(true);
////        setBlinkingImage(btImage);
////        screenStatusDisplay.setText("BT Name: \n" + name );
////        btConnectionStatus=true;
////    }
////
////    // String tagAccel ="ac";
////    //String tagGyro = "gr";
////    protected static String tag ="SENS";
////    String startCharacter = "s";
////    protected static String  separateCharacter = ":";
////    String stopCharacter = "p";
////    protected static Pair[] accelValues;
////    protected static Pair[] gyroValues;
////
////    public static void encodeBTStream ( Pair x, Pair y, Pair a, Pair b){ // encapsulate the bluetooth messages sent to arduino
////
////        //sendToBtStream(startCharacter);
////
////        StringBuilder btMessage = new StringBuilder();
////
////        btMessage.append(tag)
////                .append(separateCharacter)
////                .append(x.second.toString())
////                .append(separateCharacter)
////                .append(y.second.toString())
////                .append(separateCharacter)
////                .append(a.second.toString())
////                .append(separateCharacter)
////                .append(b.second.toString());
////
////        sendToBtStream(btMessage.toString());
////    }
////
////
////
////
////}
//
////private static final String TAG = "MY_APP_DEBUG_TAG";
////private Handler mHandler; // handler that gets info from Bluetooth service
////
////// Defines several constants used when transmitting messages between the
////// service and the UI.
////private interface MessageConstants {
////    public static final int MESSAGE_READ = 0;
////    public static final int MESSAGE_WRITE = 1;
////    public static final int MESSAGE_TOAST = 2;
////
////    // ... (Add other message types here as needed.)
////}
////
////private class ConnectedThread extends Thread {
////    private final BluetoothSocket mmSocket;
////    private final InputStream mmInStream;
////    private final OutputStream mmOutStream;
////    private byte[] mmBuffer; // mmBuffer store for the stream
////
////    public ConnectedThread(BluetoothSocket socket) {
////        mmSocket = socket;
////        InputStream tmpIn = null;
////        OutputStream tmpOut = null;
////
////        // Get the input and output streams; using temp objects because
////        // member streams are final.
////        try {
////            tmpIn = socket.getInputStream();
////        } catch (IOException e) {
////            Log.e(TAG, "Error occurred when creating input stream", e);
////        }
////        try {
////            tmpOut = socket.getOutputStream();
////        } catch (IOException e) {
////            Log.e(TAG, "Error occurred when creating output stream", e);
////        }
////
////        mmInStream = tmpIn;
////        mmOutStream = tmpOut;
////    }
////
////    public void run() {
////        mmBuffer = new byte[1024];
////        int numBytes; // bytes returned from read()
////
////        // Keep listening to the InputStream until an exception occurs.
////        while (true) {
////            try {
////                // Read from the InputStream.
////                numBytes = mmInStream.read(mmBuffer);
////                // Send the obtained bytes to the UI activity.
////                Message readMsg = mHandler.obtainMessage(
////                        MessageConstants.MESSAGE_READ, numBytes, -1,
////                        mmBuffer);
////                readMsg.sendToTarget();
////            } catch (IOException e) {
////                Log.d(TAG, "Input stream was disconnected", e);
////                break;
////            }
////        }
////    }
////
////    // Call this from the main activity to send data to the remote device.
////    public void write(byte[] bytes) {
////        try {
////            mmOutStream.write(bytes);
////
////            // Share the sent message with the UI activity.
////            Message writtenMsg = mHandler.obtainMessage(
////                    MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
////            writtenMsg.sendToTarget();
////        } catch (IOException e) {
////            Log.e(TAG, "Error occurred when sending data", e);
////
////            // Send a failure message back to the activity.
////            Message writeErrorMsg =
////                    mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
////            Bundle bundle = new Bundle();
////            bundle.putString("toast",
////                    "Couldn't send data to the other device");
////            writeErrorMsg.setData(bundle);
////            mHandler.sendMessage(writeErrorMsg);
////        }
////    }
////
////    // Call this method from the main activity to shut down the connection.
////    public void cancel() {
////        try {
////            mmSocket.close();
////        } catch (IOException e) {
////            Log.e(TAG, "Could not close the connect socket", e);
////        }
////    }
////}
//
//
//
//
////
////
////package com.stevensit.www.cpe556_interface_app;
////
////import android.bluetooth.BluetoothSocket;
////import android.os.Handler;
////import android.os.Message;
////
////import java.io.IOException;
////import java.io.InputStream;
////import java.io.OutputStream;
////
////import static com.stevensit.www.cpe556_interface_app.MainActivity.showToastMSG;
////
////public class BluetoothReceiveControl extends Thread {
////
////    public OutputStream btOutputStream;
////    private BluetoothSocket inputSocket;
////    private InputStream inStream=null;
////    private byte [] btReadBuffer;
////    private Handler readBTHandler;
////
////
////    private class ReceiveBTStream extends Thread{
////
////        public void readBTStream(){
////            InputStream tempIN=null;
////
////            try{
////                tempIN = inputSocket.getInputStream();
////            }catch (IOException e){
////               showToastMSG(getApplicationContext(),"BT Error\nError occurred when receiving bt stream", Toast.LENGTH_SHORT).show();
////            }
////
////            inStream=tempIN;
////        }
////
////
////        public void readInputStream(){
////            btReadBuffer = new byte[1024];
////            int numByte;
////            while(true){
////                try{
////                    numByte = inStream.read(btReadBuffer);
////                    Message readMsg = readBTHandler.obtainMessage(
////                            0, numByte, -1,
////                            btReadBuffer);
////                    readMsg.sendToTarget();
////                }catch (IOException e){
////                //    showToastMSG("bt stream read error", "input stream is disconnected");
////                    break;
////                }
////            }
////        }
////
////        public void onCancel(){
////            try{
////                inputSocket.close();
////            }catch (IOException e){
////               // showToastMSG("bt socket error", "couldn't close teh connection");
////            }
////
////        }
////
////
////
////
////
////    }
////
////
////}
////
////package com.stevensit.www.cpe556_interface_app;
////
////
////import android.widget.TabHost;
////
////private class BTConnectionControl extends Thread {
////
//
//
//
//
//
//}