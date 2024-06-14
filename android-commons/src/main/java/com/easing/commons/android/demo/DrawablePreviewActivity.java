package com.easing.commons.android.demo;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.view.Views;

public class DrawablePreviewActivity extends CommonActivity {

    @Override
    protected void create() {
        setContentView(R.layout.activity_drawable_preview);
        Views.viewBinding(this, ctx);

        findViewById(R.id.v1).setSelected(true);
        findViewById(R.id.v3).setSelected(true);
        findViewById(R.id.v5).setSelected(true);
    }
}
