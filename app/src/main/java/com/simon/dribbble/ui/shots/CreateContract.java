package com.simon.dribbble.ui.shots;

import com.simon.agiledevelop.mvpframe.Presenter;
import com.simon.agiledevelop.mvpframe.MvpView;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/9/6 17:45
 */

public interface CreateContract {

    interface View extends MvpView<Presenter> {
        void onSuccess();
    }


}
