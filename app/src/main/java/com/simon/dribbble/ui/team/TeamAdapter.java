package com.simon.dribbble.ui.team;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.simon.agiledevelop.recycler.RecycledViewHolder;
import com.simon.agiledevelop.recycler.adapter.RecycledAdapter;
import com.simon.agiledevelop.utils.ImgLoadHelper;
import com.simon.dribbble.R;
import com.simon.dribbble.data.model.TeamEntity;
import com.simon.dribbble.util.ColorPhrase;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/9/13 10:51
 */

public class TeamAdapter extends RecycledAdapter<TeamEntity,RecycledViewHolder> {
    private RecyclerView.RecycledViewPool mPool = new RecyclerView.RecycledViewPool();

    public TeamAdapter() {
        super(R.layout.item_team);
    }

    @Override
    protected void convert(RecycledViewHolder helper, TeamEntity item) {
        if (null != item) {
            String avatar_url = item.getAvatar_url();
            ImgLoadHelper.loadAvatar(avatar_url, (ImageView) helper.getView(R.id.imv_avatar));

            helper.setText(R.id.tv_team_na, item.getName());
            helper.setText(R.id.tv_location, item.getLocation());
            helper.setText(R.id.tv_team_bio, item.getBio());

            CharSequence shot_count = ColorPhrase.from(item.getShots_count() + "  <作品>")
                    .withSeparator("<>")
                    .innerColor(0xFF808080)
                    .outerColor(0xFF0000FF)
                    .format();

            CharSequence follower_count = ColorPhrase.from(item.getFollowers_count() + "  <粉丝>")
                    .withSeparator("<>")
                    .innerColor(0xFF808080)
                    .outerColor(0xFF0000FF)
                    .format();

            helper.setText(R.id.tv_shots_count, shot_count);
            helper.setText(R.id.tv_follower_count, follower_count);
        }
    }

    public RecyclerView.RecycledViewPool getPool() {
        return mPool;
    }
}
