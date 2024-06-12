package com.easing.commons.android.ui_component.favorite_button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.easing.commons.android.R;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.preference.Preference;
import com.easing.commons.android.struct.StringList;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.view.Views;

@SuppressWarnings("all")
public class FavoritesSwitch extends AppCompatImageView {

    public FavoritesSwitch(Context context) {
        this(context, null);
    }

    public FavoritesSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        Views.onClick(this, () -> {
            boolean isAdd = isAddToFavorites();
            if (isAdd)
                removeFromFavorites();
            else
                addToFavorites();
            EventBus.core.emit("onActivityFavoritesChange");
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        boolean isAdd = isAddToFavorites();
        setImageResource(isAdd ? R.drawable.icon_favorited : R.drawable.icon_not_favorited);
    }

    //判断是否已加入收藏夹
    public boolean isAddToFavorites() {
        StringList favoritesClasses = Preference.get("activityFavorites", StringList.class, StringList.getDefault());
        return favoritesClasses.contains(getContext().getClass().getName());
    }

    //加入收藏夹
    public void addToFavorites() {
        StringList favoritesClasses = Preference.get("activityFavorites", StringList.class, StringList.getDefault());
        favoritesClasses.add(getContext().getClass().getName());
        Preference.set("activityFavorites", favoritesClasses);
        setImageResource(R.drawable.icon_favorited);
        TipBox.tipInCenter("已添加至快捷功能入口");
    }

    //移除收藏夹
    public void removeFromFavorites() {
        StringList favoritesClasses = Preference.get("activityFavorites", StringList.class, StringList.getDefault());
        favoritesClasses.remove(getContext().getClass().getName());
        Preference.set("activityFavorites", favoritesClasses);
        setImageResource(R.drawable.icon_not_favorited);
        TipBox.tipInCenter("已从快捷功能入口移除");
    }
}
