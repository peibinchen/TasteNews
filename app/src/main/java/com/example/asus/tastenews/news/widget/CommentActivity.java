package com.example.asus.tastenews.news.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.asus.tastenews.R;
import com.example.asus.tastenews.beans.NewsBean;

/**
 * Created by SomeOneInTheWorld on 2016/8/23.
 */
public class CommentActivity extends AppCompatActivity implements BaseFragment.QuestionCallback{
    public static final int COMMENT_TYPE_ASK = 0;
    public static final int COMMENT_TYPE_COMMENT_AREA = 1;

    protected static final String COMMENT_TYPE = "comment_type";

    private NewsBean mNewsBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        int type = getIntent().getIntExtra(COMMENT_TYPE,COMMENT_TYPE_ASK);
        mNewsBean = (NewsBean)getIntent().getSerializableExtra("news");
        switch2Fragment(type);
    }

    private void switch2Fragment(int type){
        switch(type){
            case COMMENT_TYPE_ASK:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_comment_content,new QuestionFragment()).commit();
                break;
            case COMMENT_TYPE_COMMENT_AREA:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_comment_content,new NewsCommentMainFragment()).commit();
                break;
        }
    }

    @Override
    public NewsBean getNewsBean() {
        return mNewsBean;
    }
}
