package com.keju.baby.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.baby.BabyMainActivity;
import com.keju.baby.activity.doctor.DoctorMainActivity;
import com.keju.baby.activity.login.LoginActivity;
import com.keju.baby.util.NetUtil;
import com.keju.baby.util.SharedPrefUtil;

public class LogoActivity extends Activity {
	private ImageView ivHeart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        
        ivHeart = (ImageView) findViewById(R.id.ivHeart);
        animation();
    }
    
    /**
	 * 跳转；
	 */
	private void animation() {
		AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
		aa.setDuration(2000);
		ivHeart.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation arg0) {
				if(SharedPrefUtil.getIsRemember(LogoActivity.this)== 1){
					if(SharedPrefUtil.getIsRemember(LogoActivity.this)== 1){
						if(SharedPrefUtil.getUserType(LogoActivity.this) == Constants.USER_DOCTOR){
							startActivity(new Intent(LogoActivity.this, DoctorMainActivity.class));
						}else{
							startActivity(new Intent(LogoActivity.this, BabyMainActivity.class));
						}	
					}else{
						if(NetUtil.checkNet(LogoActivity.this)){
							if(SharedPrefUtil.getUserType(LogoActivity.this) == Constants.USER_DOCTOR){
								startActivity(new Intent(LogoActivity.this, DoctorMainActivity.class));
							}else{
								startActivity(new Intent(LogoActivity.this, BabyMainActivity.class));
							}	
						}else{
							startActivity(new Intent(LogoActivity.this, LoginActivity.class));
						}
					}
				}
					else{
					startActivity(new Intent(LogoActivity.this, LoginActivity.class));
				}
				finish();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
	}
}