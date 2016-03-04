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
        
		//���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//����״̬��	
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.splash);
        
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        
        //���뵭��Ч��
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(5000);// ���ö�����ʾʱ��5s
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
            skip(); // ������������ת�����ҳ��
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