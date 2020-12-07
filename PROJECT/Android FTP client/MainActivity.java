import java.io.IOException;

import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener; import org.apache.commons.net.ftp.FTP;

import org.apache.commons.net.ftp.FTPConnectionClosedException; import org.apache.commons.net.ftp.FTPReply; import org.apache.commons.net.ftp.FTPSClient;

import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;

import android.os.StrictMode;

import android.support.v7.app.ActionBarActivity; import android.view.Menu; import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements OnClickListener {

private EditText server, username, password; private Button theButton;

boolean error = false;
String protocol = "SSL";

FTPSClient ftps;

public static final String MY_APP_PREFS = "MyAppPrefs";

@Override

protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);

StrictMode.ThreadPolicy policy = new
StrictMode.ThreadPolicy.Builder().permitAll().build();

StrictMode.setThreadPolicy(policy);

ftps = new FTPSClient(protocol); ftps.addProtocolCommandListener(new PrintCommandListener(new
PrintWriter(System.out)));
 



 

server = (EditText) findViewById(R.id.server); username = (EditText) findViewById(R.id.username); password = (EditText) findViewById(R.id.password);

theButton = (Button)findViewById(R.id.myButton); theButton.setOnClickListener(this);
}

public void onClick(View v) {
//respond to click

if (v.getId() == theButton.getId()) {

//	init connection
try
{

int reply;

ftps.connect(server.getText().toString());

System.out.println("Connected to " + server.getText().toString() + ".");

reply = ftps.getReplyCode();

if (!FTPReply.isPositiveCompletion(reply))
{

ftps.disconnect();

System.err.println("FTP server refused connection."); System.exit(1);

}
}

catch (IOException e)
{

if (ftps.isConnected())
{

try

{
ftps.disconnect();

}
catch (IOException f)

{
// do nothing
}

}
System.err.println("Could not connect to server.");

e.printStackTrace();
System.exit(1);

}

//	log in

try
{

ftps.setBufferSize(1000);

if (!ftps.login(username.getText().toString(), password.getText().toString()))
{
 


 
ftps.logout();

error = true;
System.exit(1);

}

ftps.setFileType(FTP.BINARY_FILE_TYPE);
ftps.enterLocalPassiveMode();

ftps.sendCommand("OPTS UTF8 ON");

Globals.global_ftps = ftps;

Intent i = new Intent(getApplicationContext(), Files.class); startActivity(i);

}

catch (FTPConnectionClosedException e)

{
error = true;

System.err.println("Server closed connection."); e.printStackTrace();

}
catch (IOException e)

{

error = true;
e.printStackTrace();

}

System.out.println(Environment.getDataDirectory()); System.out.println(Environment.getExternalStorageDirectory());
}

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {

//	Inflate the menu; this adds items to the action bar if it is present. getMenuInflater().inflate(R.menu.main, menu);

return true;

}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
//	Handle action bar item clicks here. The action bar will

//	automatically handle clicks on the Home/Up button, so long

//	as you specify a parent activity in AndroidManifest.xml. int id = item.getItemId();

if (id == R.id.action_settings) { return true;

}

return super.onOptionsItemSelected(item);

}
}
 








 

