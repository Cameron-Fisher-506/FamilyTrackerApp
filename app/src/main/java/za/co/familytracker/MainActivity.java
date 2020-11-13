package za.co.familytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

import za.co.familytracker.nav.DevicesFrag;
import za.co.familytracker.nav.FamilyFrag;
import za.co.familytracker.nav.MeFrag;
import za.co.familytracker.services.DeviceService;
import za.co.familytracker.utils.ConstantUtils;
import za.co.familytracker.utils.DTUtils;
import za.co.familytracker.utils.DeviceUtils;
import za.co.familytracker.utils.FragmentUtils;
import za.co.familytracker.utils.GeneralUtils;
import za.co.familytracker.utils.StringUtils;
import za.co.familytracker.utils.WSCallsUtils;
import za.co.familytracker.utils.WSCallsUtilsTaskCaller;

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

        createDevice();

        startService(new Intent(this, DeviceService.class));

        addBtnFamilyListener();
        addBtnDevicesListener();
        addBtnMeListener();

        FamilyFrag familyFrag = new FamilyFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), familyFrag, R.id.fragContainer, getSupportActionBar(), "Family", true, false, true, null);
        setNavIcons(true, false, false);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if(appLinkData != null)
        {
            String imeiToLink = appLinkData.getLastPathSegment();
            String imei = DeviceUtils.getIMEI(this);
            linkDevice(imei, imeiToLink);
        }
    }

    private void createDevice()
    {
        String imei = DeviceUtils.getIMEI(this);
        if(imei != null)
        {
            try
            {
                JSONObject body = new JSONObject();
                body.put("imei", imei);
                body.put("name", "Me");

                WSCallsUtils.post(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/create", body.toString(), REQ_CODE_CREATE);
            }catch (Exception e)
            {
                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                        + "\nMethod: WSCallsUtils - doInBackground"
                        + "\nIMEI: " + imei
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }
        }else
        {
            GeneralUtils.makeToast(this, "IMEI is null");
        }
    }

    private void linkDevice(String imei, String imeiToLink)
    {
        if(imei != null)
        {
            if(imeiToLink != null)
            {
                try
                {
                    JSONObject body = new JSONObject();
                    body.put("imei", imei);
                    body.put("imeiToLink", imeiToLink);

                    WSCallsUtils.post(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/linkDevice", body.toString(), REQ_CODE_LINK_DEVICE);


                }catch(Exception e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: WSCallsUtils - doInBackground"
                            + "\nIMEI: " + imei
                            + "imeiToLink: " + imeiToLink
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }else
            {
                GeneralUtils.makeToast(this, "imeiToLink is null");
            }

        }else
        {
            GeneralUtils.makeToast(this, "IMEI is null");
        }
    }

    private void wireUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
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
        }
    }
}
