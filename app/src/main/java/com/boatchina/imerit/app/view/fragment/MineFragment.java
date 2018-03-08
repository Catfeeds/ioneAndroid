package com.boatchina.imerit.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boatchina.imerit.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.view_setings)
    FrameLayout viewSetings;
    @BindView(R.id.view_about)
    FrameLayout viewAbout;
    @BindView(R.id.view_map_type)
    FrameLayout viewMapType;
    @BindView(R.id.btn_quit)
    Button btnQuit;

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((TextView) viewSetings.getChildAt(0)).setText(R.string.title_config);
        ((TextView) viewAbout.getChildAt(0)).setText(R.string.title_aboutus);
        ((TextView) viewMapType.getChildAt(0)).setText(R.string.desc_map_type);
    }

    @OnClick({R.id.view_setings, R.id.view_about, R.id.view_map_type, R.id.btn_quit})
    public void onClick(View view) {
        actionByActivity(view.getId());
    }

    public void setQuitShow(boolean isShow) {
        if(isShow) {
            btnQuit.setVisibility(View.VISIBLE);
        }else {
            btnQuit.setVisibility(View.GONE);
        }
    }

}
