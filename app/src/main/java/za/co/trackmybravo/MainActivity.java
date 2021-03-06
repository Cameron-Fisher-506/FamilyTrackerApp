package za.co.trackmybravo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;
import za.co.trackmybravo.dialogs.LinkDeviceCallback;
import za.co.trackmybravo.menu.ShareMyLocationFrag;
import za.co.trackmybravo.nav.DevicesFrag;
import za.co.trackmybravo.nav.FamilyFrag;
import za.co.trackmybravo.nav.MeFrag;
import za.co.trackmybravo.policies.PrivacyPolicyFrag;
import za.co.trackmybravo.services.DeviceService;
import za.co.trackmybravo.utils.ConstantUtils;
import za.co.trackmybravo.utils.DTUtils;
import za.co.trackmybravo.utils.FragmentUtils;
import za.co.trackmybravo.utils.GeneralUtils;
import za.co.trackmybravo.utils.SharedPreferencesUtils;
import za.co.trackmybravo.utils.StringUtils;
import za.co.trackmybravo.utils.WSCallsUtils;
import za.co.trackmybravo.utils.WSCallsUtilsTaskCaller;

public class MainActivity extends AppCompatActivity implements WSCallsUtilsTaskCaller
{
    private final int REQ_CODE_LINK_DEVICE = 101;
    private final int REQ_CODE_CREATE = 102;

    private final String TAG = "MainActivity";
    private ImageButton btnMe;
    private ImageButton btnFamily;
    private ImageButton btnDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUI();

        displayPrivacyPolicy();

    }

    private void displayPrivacyPolicy()
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE);
            if(jsonObject == null)
            {
                setNavIcons(false, false, false);
                this.btnFamily.setClickable(false);
                this.btnDevices.setClickable(false);
                this.btnMe.setClickable(false);

                PrivacyPolicyFrag privacyPolicyFrag = new PrivacyPolicyFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), privacyPolicyFrag, R.id.fragContainer, getSupportActionBar(), "Privacy Policy", true, false, true, null);
            }else
            {
                createDevice();

                startService(new Intent(this, DeviceService.class));

                MeFrag meFrag = new MeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), meFrag, R.id.fragContainer, getSupportActionBar(), "Me", true, false, true, null);
                setNavIcons(false, false, true);

                // ATTENTION: This was auto-generated to handle app links.
                Intent appLinkIntent = getIntent();
                String appLinkAction = appLinkIntent.getAction();
                Uri appLinkData = appLinkIntent.getData();

                if(appLinkData != null)
                {
                    final String codeToLink = appLinkData.getLastPathSegment();


                    if(codeToLink != null)
                    {
                        GeneralUtils.createAlertDialog(this, "Track Device", "Do you want to track this device (" + codeToLink +")?", true, new LinkDeviceCallback() {
                            @Override
                            public void linkDevice(String name) {

                                String myCode = SharedPreferencesUtils.getString(getApplicationContext(), SharedPreferencesUtils.MY_CODE);
                                linkDeviceToMonitor(name, myCode,codeToLink);

                            }
                        }).show();
                    }
                }
            }


        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - displayPrivacyPolicy"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.shareMyLocation:
            {

                ShareMyLocationFrag shareMyLocationFrag = new ShareMyLocationFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), shareMyLocationFrag, R.id.fragContainer, getSupportActionBar(), "Share My Location", true, false, true, null);

                break;
            }
            default:
            {
                //unknown
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void createDevice()
    {
        try
        {
            JSONObject body = new JSONObject();
            String code = SharedPreferencesUtils.getString(this, SharedPreferencesUtils.MY_CODE);
            if(code != null)
            {
                body.put("code", code);
            }

            body.put("name", "Me");

            WSCallsUtils.post(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/create", body.toString(), REQ_CODE_CREATE);
        }catch (Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - doInBackground"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    private void linkDeviceToMonitor(String name, String code, String codeToLink)
    {

        if(name != null)
        {
            if(code != null)
            {
                if(codeToLink != null)
                {
                    try
                    {
                        JSONObject body = new JSONObject();
                        body.put("name", name);
                        body.put("code", code);
                        body.put("codeToLink", codeToLink);

                        WSCallsUtils.post(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/linkDevice", body.toString(), REQ_CODE_LINK_DEVICE);


                    }catch(Exception e)
                    {
                        Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                                + "\nMethod: WSCallsUtils - doInBackground"
                                + "\ncode: " + code
                                + "codeToLink: " + codeToLink
                                + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                    }
                }else
                {
                    GeneralUtils.makeToast(this, "Partner's code is not found!");
                }

            }else
            {
                GeneralUtils.makeToast(this, "You do not have a code!");
            }
        }else
        {
            GeneralUtils.makeToast(this, "Please enter a name to start tracking member!");
        }


    }

    private void wireUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        addBtnFamilyListener();
        addBtnDevicesListener();
        addBtnMeListener();
    }

    private void addBtnDevicesListener()
    {
        this.btnDevices = (ImageButton) findViewById(R.id.btnDevices);
        this.btnDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, true, false);

                DevicesFrag devicesFrag = new DevicesFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), devicesFrag, R.id.fragContainer, getSupportActionBar(), "Devices", true, false, true, null);
            }
        });
    }

    private void addBtnFamilyListener()
    {
        this.btnFamily = (ImageButton) findViewById(R.id.btnFamily);
        this.btnFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(true, false, false);

                FamilyFrag familyFrag = new FamilyFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), familyFrag, R.id.fragContainer, getSupportActionBar(), "Family", true, false, true, null);
            }
        });
    }

    private void addBtnMeListener()
    {
        this.btnMe = (ImageButton) findViewById(R.id.btnMe);
        this.btnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, false, true);

                MeFrag meFrag = new MeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), meFrag, R.id.fragContainer, getSupportActionBar(), "Me", true, false, true, null);
            }
        });
    }

    public void setNavIcons(boolean isFamily, boolean isDevices, boolean isMe)
    {
        if(isFamily)
        {
            this.btnFamily.setBackgroundResource(R.drawable.ic_people_black_24dp);
        }else
        {
            this.btnFamily.setBackgroundResource(R.drawable.ic_people_black_white_24dp);
        }

        if(isDevices)
        {
            this.btnDevices.setBackgroundResource(R.drawable.ic_devices_black_24dp);
        }else
        {
            this.btnDevices.setBackgroundResource(R.drawable.ic_devices_black_white_24dp);
        }

        if(isMe)
        {
            this.btnMe.setBackgroundResource(R.drawable.ic_account_circle_black_24dp);
        }else
        {
            this.btnMe.setBackgroundResource(R.drawable.ic_account_circle_black_white_24dp);
        }



    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void taskCompleted(String response, int reqCode) {
        if(response != null)
        {
            if(reqCode == REQ_CODE_LINK_DEVICE)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("code") && jsonObject.has("message"))
                    {
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");
                        if(code == 0)
                        {
                            GeneralUtils.makeToast(this, message);
                        }else if(code == 1)
                        {
                            GeneralUtils.makeToast(this, message);
                        }else if(code == -1)
                        {
                            GeneralUtils.makeToast(this, message);
                        }
                    }
                }catch(Exception e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: MainActivity - taskCompleted"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_CREATE)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("code") && jsonObject.has("message") && jsonObject.has("title"))
                    {
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");
                        if(code == 0)
                        {
                            String myCode = jsonObject.getString("myCode");
                            SharedPreferencesUtils.save(this, SharedPreferencesUtils.MY_CODE, myCode);

                            GeneralUtils.makeToast(this, "Saved Code!");
                        }else if(code == -1)
                        {
                            GeneralUtils.makeToast(this, message);
                        }
                    }
                }catch(Exception e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: MainActivity - taskCompleted"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }
        }
    }
}
