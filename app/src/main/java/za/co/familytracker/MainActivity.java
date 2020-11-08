package za.co.familytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import za.co.familytracker.nav.DevicesFrag;
import za.co.familytracker.nav.FamilyFrag;
import za.co.familytracker.nav.MeFrag;
import za.co.familytracker.services.DeviceService;
import za.co.familytracker.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private ImageButton btnMe;
    private ImageButton btnFamily;
    private ImageButton btnDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUI();

        startService(new Intent(this, DeviceService.class));

        addBtnFamilyListener();
        addBtnDevicesListener();
        addBtnMeListener();

        FamilyFrag familyFrag = new FamilyFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), familyFrag, R.id.fragContainer, getSupportActionBar(), "Family", true, false, true, null);
        setNavIcons(true, false, false);

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
}
