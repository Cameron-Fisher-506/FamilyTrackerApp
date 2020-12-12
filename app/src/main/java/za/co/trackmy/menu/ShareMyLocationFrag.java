package za.co.trackmy.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.trackmy.MainActivity;
import za.co.trackmy.R;
import za.co.trackmy.dialogs.PermissionCallback;
import za.co.trackmy.nav.MeFrag;
import za.co.trackmy.utils.DeviceUtils;
import za.co.trackmy.utils.DialogUtils;
import za.co.trackmy.utils.FragmentUtils;
import za.co.trackmy.utils.GeneralUtils;
import za.co.trackmy.utils.SharedPreferencesUtils;

public class ShareMyLocationFrag extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_share_my_location, container, false);

        String code = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.MY_CODE);
        if(code != null)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://cameronfisher.co.za/trackmy/" + code);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }else
        {
            GeneralUtils.makeToast(getContext(), "You do not have a code!");
        }

        MeFrag meFrag = new MeFrag();
        FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), meFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Me", true, false, true, null);

        return view;
    }
}
