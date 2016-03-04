package cn.poseidon;

import com.example.poseidon.R;

import cn.poseidon.MainActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {
    private ImageView welcomeImg = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//隐藏状态栏	
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.splash);
        
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        
        //淡入淡出效果
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(5000);// 设置动画显示时间5s
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());
    }

    private class AnimationImpl implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
           // welcomeImg.setBackgroundResource(R.drawable.bac);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            skip(); // 动画结束后跳转到别的页面
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void skip() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}