package cn.poseidon;

import com.example.poseidon.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
@SuppressWarnings("deprecation")
public class AboutUsActivity extends Activity
{
	int[] imageIds = new int[]
	{
		R.drawable.person1, R.drawable.person2,
		R.drawable.person3, R.drawable.person4,
		R.drawable.person5
		};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		@SuppressWarnings("deprecation")
		final Gallery gallery = (Gallery) findViewById(R.id.gallery);
		
		// 获取显示图片的ImageSwitcher对象
		final ImageSwitcher switcher = (ImageSwitcher) findViewById(R.id.switcher);
		// 为ImageSwitcher对象设置ViewFactory对象
		switcher.setFactory(new ViewFactory()
		{
			@SuppressWarnings("deprecation")
			@Override
			public View makeView()
			{
				ImageView imageView = new ImageView(AboutUsActivity.this);
				imageView.setBackgroundColor(0xff0000);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
				return imageView;
			}
		});
		// 设置图片更换的动画效果
		switcher.setInAnimation(AnimationUtils.loadAnimation(this,
			android.R.anim.fade_in));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(this,
			android.R.anim.fade_out));
		// 创建一个BaseAdapter对象，该对象负责提供Gallery所显示的图片
		BaseAdapter adapter = new BaseAdapter()
		{
			@Override
			public int getCount()
			{
				return imageIds.length;
			}
			@Override
			public Object getItem(int position)
			{
				return position;
			}
			@Override
			public long getItemId(int position)
			{
				return position;
			}
			
			// 该方法的返回的View就是代表了每个列表项
			@SuppressWarnings("deprecation")
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				// 创建一个ImageView
				ImageView imageView = new ImageView(AboutUsActivity.this);
				imageView.setImageResource(imageIds[position % imageIds.length]);
				// 设置ImageView的缩放类型
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
				return imageView;
			}
		};
		
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			// 当Gallery选中项发生改变时触发该方法
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id)
			{
				switcher.setImageResource(imageIds[position % imageIds.length]);
				
//				if(!bitmapDrawable.getBitmap().isRecycled())
//				{
//					bitmapDrawable.getBitmap().recycle();
//				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}
}