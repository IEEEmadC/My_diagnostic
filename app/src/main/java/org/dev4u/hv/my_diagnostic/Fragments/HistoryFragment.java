package org.dev4u.hv.my_diagnostic.Fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import db.User;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.DiseaseUtilitesSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment {

    private View view;
    private ImageView hearth;
    private AnimatedVectorDrawable hearthAnim;
    private Bitmap activePicture;
    private ImageView imageViewDialog;
    private CircleImageView circleImageView;
    private User user;
    private TextView lblUsername;
    private TextView lblBirtday;
    private TextView lblBloodtype;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);
        //animation
        hearth = (ImageView)view.findViewById(R.id.pulse);
        hearthAnim = ((AnimatedVectorDrawable) ((ImageView) view.findViewById(R.id.pulse)).getDrawable());
        //controls

        activePicture = ((BitmapDrawable) getActivity().getBaseContext().getDrawable(R.drawable.ic_profile)).getBitmap();
        circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
        lblUsername = (TextView) view.findViewById(R.id.lblFullname);
        lblBirtday = (TextView) view.findViewById(R.id.lblBirthday);
        lblBloodtype = (TextView) view.findViewById(R.id.lblBloodtype);



        //setting the profile image
        Bitmap b =null;//= loadImageFromStorage("Profile","profile.png");
        if(b!=null) {
            activePicture = b;
            circleImageView.setImageBitmap(Bitmap.createScaledBitmap (b,(int) (b.getWidth() * .4), (int) (b.getHeight() * .4),true));
        }

        setHasOptionsMenu(true);
        //restartCursiveAnimation();

        AnimatedVectorDrawable d = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.hearth_pulse_animation); // Insert your AnimatedVectorDrawable resource identifier
        hearth.setImageDrawable(d);
        d.start();


        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartCursiveAnimation();
            }
        });


        //update values
        preferences = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences = preferences.edit();

        String status = preferences.getString("USERNAME","null");
        if(!status.equals("null")){
            user = DiseaseUtilitesSingleton.getInstance().getUser(status);
            lblUsername.setText(user.getFullname());

            String parts[] = user.getBirthday().split("-");
            String bday = parts[2]+"/"+parts[1]+"/"+parts[0];
            lblBloodtype.setText("BloodType :"+user.getName_bloodtype());
            lblBirtday.setText("BirthDay :"+bday);
        }


        return view;
    }

    private void restartCursiveAnimation() {

        Log.d("Estado anim$$$$$$$$$$$","Estado "+hearthAnim.isRunning());
        hearthAnim.stop();
        hearthAnim.start();
        Log.d("Estado anim$$$$$$$$$$$","Estado "+hearthAnim.isRunning());
    }


    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_find, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        SearchView sv = new SearchView(((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                //handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap loadImageFromStorage(String path,String name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, name);
            Log.d("Se busca en: ",f.getAbsolutePath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }
}
