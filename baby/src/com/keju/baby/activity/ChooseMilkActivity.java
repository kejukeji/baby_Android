package com.keju.baby.activity;

import android.os.Bundle;

import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 选择配方奶（妈妈进入的时候，不可以新增配方奶）
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:34:41
 */
public class ChooseMilkActivity extends BaseActivity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_milk_activity);
	}
}
