package za.co.familytracker.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.familytracker.MainActivity;
import za.co.familytracker.R;
import za.co.familytracker.dialogs.PermissionCallback;
import za.co.familytracker.nav.MeFrag;
import za.co.familytracker.utils.DeviceUtils;
import za.co.familytracker.utils.DialogUtils;
import za.co.familytracker.utils.FragmentUtils;
import za.co.familytracker.utils.GeneralUtils;

public class ShareMyLocationFrag extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_share_my_location, container, false);

        String imei = DeviceUtils.getIMEI(getContext());

        if(imei != null)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://cameronfisher.co.za/familytracker/" + DeviceUtils.getIMEI(getContext()));
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }else
        {
            DialogUtils.createAlertPermission(getContext(), "Phone Permission", "Please enable phone and location permissions for Family Tracker.", true, new PermissionCallback() {
                @Override
                public void checkPermission(boolean ischeckPermission) {
                    if(ischeckPermission)
                    {
                        GeneralUtils.openAppSettingsScreen(getContext());
                    }
                }
            }).show();
        }

        MeFrag meFrag = new MeFrag();
        FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), meFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Me", true, false, true, null);

        return view;
    }
}
