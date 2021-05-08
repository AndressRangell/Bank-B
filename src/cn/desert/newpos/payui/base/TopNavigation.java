package cn.desert.newpos.payui.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.newpos.pay.R;


/**
 * Activity的导航条
 */
public class TopNavigation extends LinearLayout {


	private LinearLayout mNavLeft;
	private ImageView mLeftIcon;
	private ImageView mRightIcon;
	private TextView navLeftTx;
	private TextView mTitle;
	private TextView mRightContent;
	private RelativeLayout topLayout;
	private TextView mTimeView;

	public TopNavigation(Context context) {
		super(context);
		init(context);
	}

	public TopNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {

		LinearLayout mNavigationView;

		mNavigationView = (LinearLayout) inflate(context, R.layout.base_top_navigation, this);
		topLayout = findViewById(R.id.top_layout);
		mNavLeft = findViewById(R.id.nav_left);
		mLeftIcon = mNavigationView.findViewById(R.id.nav_left_img);
		mTitle = mNavigationView.findViewById(R.id.nav_title_tv);
		mRightContent = findViewById(R.id.nav_right_text);
		mTimeView = findViewById(R.id.nav_right_text);
	}

	/**
	 * 设置导航条左边图标
	 * @param resId
	 */
	public void setLeftIcon(int resId) {
		mLeftIcon.setBackgroundResource(resId);
	}

	public void setLeftIcon(Drawable drawable) {
		mLeftIcon.setBackgroundDrawable(drawable);
	}

	/**
	 * 设置导航条右边图标
	 * @param resId
	 */
	public void setRightIcon(int resId) {
		mRightIcon.setImageResource(resId);
	}

	public void setRightIcon(Drawable drawable) {
		mRightIcon.setImageDrawable(drawable);
	}

	/**
	 * 设置导航条文字内容
	 * @param resId
	 */
	public void setTitle(int resId) {
		mTitle.setText(resId);
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

	/**
	 * 设置导航条时间
	 * @param str
     */
	public void setTime(String str){
		mTimeView.setText(str);
	}

	public void setTimeColor(int id){
		mTimeView.setTextColor(id);
	}

	public void setTimeVisible(int flag){
		mTimeView.setVisibility(flag);
	}

	/**
	 * 设置导航条文字内容
	 * @param resId
	 */
	public void setRightContent(int resId) {
		mRightContent.setText(resId);
	}

	public void setRightContentVisiblity(int v){
		mRightContent.setVisibility(v);
	}

	public void setRightContent(String title) {
		mRightContent.setText(title);
	}

	public void setOnLeftIconClickListener(OnClickListener clickListener) {
		mLeftIcon.setOnClickListener(clickListener);
	}

	public RelativeLayout getTopLayout() {
		return topLayout;
	}

	public void setTopLayout(RelativeLayout topLayout) {
		this.topLayout = topLayout;
	}

	public TextView getNavLeftTx() {
		return navLeftTx;
	}

	public void setNavLeftTx(TextView navLeftTx) {
		this.navLeftTx = navLeftTx;
	}

	public void setOnLeftTxClickListener(OnClickListener clickListener) {
		navLeftTx.setOnClickListener(clickListener);
	}

	public ImageView getLeftIcon() {
		return mLeftIcon;
	}

	public ImageView getRightIcon() {
		return mRightIcon;
	}

	public LinearLayout getLeftLayout() {
		return mNavLeft;
	}

	public TextView getRightTxt() {
		return mRightContent;
	}

	public void setLeftIconVisible(int visibility) {
		mLeftIcon.setVisibility(visibility);
		if(visibility == View.VISIBLE){
			mLeftIcon.setEnabled(true);
			mLeftIcon.setClickable(true);
		}else {
			mLeftIcon.setClickable(false);
			mLeftIcon.setEnabled(false);

            mNavLeft.setEnabled(false);//para deshabilitar el click
		}
	}

	public void setRightIconVisible(int visibility) {
		mRightIcon.setVisibility(visibility);
	}

	public void setLeftPadding(int leftPadding) {
		mRightIcon.setPadding(leftPadding, mLeftIcon.getPaddingTop(),
				mLeftIcon.getPaddingRight(), mLeftIcon.getPaddingBottom());
	}

	public void setRightPadding(int rightPadding) {
		mRightIcon.setPadding(mRightIcon.getPaddingLeft(),
				mRightIcon.getPaddingTop(), rightPadding,
				mRightIcon.getPaddingBottom());
	}
}
