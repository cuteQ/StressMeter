package com.example.yuzhong.stressmeter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StressMeterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StressMeterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StressMeterFragment extends Fragment {
    private Button mMorePicButton;
    private int[] stressLevel;
    private int counter = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static StressMeterFragment newInstance(String param1, String param2) {
        StressMeterFragment fragment = new StressMeterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stress_meter, container, false);
        final GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        final ImageAdapter imageAdapter = new ImageAdapter(this.getContext());
        gridview.setAdapter(imageAdapter);

        //according to the lecture to mark the stress level
        stressLevel = new int[]{6,8,14,16,5,7,13,15,2,4,10,12,1,3,9,11};

        //add activity when you click the picture in the gridview
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int[] pictures = imageAdapter.getmThumbIds();
                Intent intent = new Intent(getContext(), ShowPictureActivity.class);
                // pass the data in intent
                intent.putExtra("picture", pictures[position]);
                intent.putExtra("stressLevel", stressLevel[position]);
                // start Intent
                startActivity(intent);
//                Toast.makeText(getContext(), "" + position,
//                        Toast.LENGTH_SHORT).show();
            }
        });

        //change the pictures in the gridview
        mMorePicButton = (Button) rootView.findViewById(R.id.changepictures);

        mMorePicButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                counter = counter % 3 + 1;
                imageAdapter.changePicture(counter);
                imageAdapter.notifyDataSetChanged();
                gridview.setAdapter(imageAdapter);
            }
        });
        return rootView;
    }

}
