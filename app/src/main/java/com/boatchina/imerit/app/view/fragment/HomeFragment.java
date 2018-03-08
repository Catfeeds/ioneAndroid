package com.boatchina.imerit.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.presenter.HomePresenter;
import com.boatchina.imerit.app.view.HomeView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements HomeView, BGABanner.Adapter {


    @BindView(R.id.banner_main_alpha)
    BGABanner bannerMainAlpha;
    @Inject
    HomePresenter homePresenter;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();



    }

    @Override
    public void onResume() {
        super.onResume();
//        if(homePresenter!=null) {
//            homePresenter.loadData();//扫描到这里要刷新
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BondsComponent bondsComponent = AndroidApplication.getBaseApplication().getApplicationComponent().bondsComponent(new ActivityModule(getActivity()));
        bondsComponent.inject(this);
        homePresenter.setView(this);
        bannerMainAlpha.setAdapter(this);
        List<Integer> list = new ArrayList();
        list.add(R.mipmap.ic_ad1);
        list.add(R.mipmap.ic_ad2);
        list.add(R.mipmap.ic_ad3);
        list.add(R.mipmap.ic_ad4);
        List<String> list2 = new ArrayList();
        list2.add("");
        list2.add("");
        list2.add("");
        list2.add("");
        bannerMainAlpha.setData(R.layout.view_banner, list, list2);

    }
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            onVisible();//我的解绑到这里要刷新
//
//        }
//    }

    private void onVisible() {
        if(homePresenter!=null) {
            homePresenter.loadData();
        }

    }

    //点击事件由view开始，调用presenter，再由presenter调用view，fragment在回调给activity，进行跳转
    @OnClick({R.id.iv_addimei, R.id.ll_loc, R.id.ll_history,R.id.ll_phone,R.id.ll_monitor,R.id.ll_fence,R.id.ll_talkback})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.ll_loc:
            case R.id.ll_fence:
            case R.id.ll_history:
//                final String map = PreferencesUtils.getString(getActivity(), "map");
//
//                if(map==null||map.equals("")) {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //先得到构造器
//                    builder.setTitle(R.string.title_select_map);                                     //设置标题
//                    final String[] array = new String[]{"高德地图","Google Map"};
//                    builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            PreferencesUtils.putString(getActivity(), "map", array[which]);
//
//
//
//                        }
//                    });
//                    builder.create().show();
//                }else if(map.equals("Google Map")) {
//                    switch (view.getId()) {
//                        case R.id.ll_loc:
//                            startActivity(new Intent(getActivity(), GLocationActivity.class));
//                            break;
//                        case R.id.ll_fence:
//                            startActivity(new Intent(getActivity(), FenceListActivity.class));
//                            break;
//                        case R.id.ll_history:
//                            startActivity(new Intent(getActivity(), GHistoryActivity.class));
//                            break;
//                    }
//                }else if(map.equals("高德地图")){
//                    switch (view.getId()) {
//                        case R.id.ll_loc:
//                            startActivity(new Intent(getActivity(), LocationActivity.class));
//                            break;
//                        case R.id.ll_fence:
//                            startActivity(new Intent(getActivity(), FenceListActivity.class));
//                            break;
//                        case R.id.ll_history:
//                            startActivity(new Intent(getActivity(), HistoryActivity.class));
//                            break;
//                    }
//                }
//                break;
                homePresenter.navigateTo(view.getId());
                break;
            default:
                homePresenter.navigateTo(view.getId());
                break;
        }


    }

    @Override
    public void navigateTo(int id) {
        actionByActivity(id);
    }




    @Override
    public Context context() {
        return getActivity();
    }


    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        ((SimpleDraweeView) view).setImageResource((Integer) model);

    }


}
