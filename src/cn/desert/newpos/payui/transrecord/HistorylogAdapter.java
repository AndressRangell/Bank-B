package cn.desert.newpos.payui.transrecord;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.android.newpos.pay.R;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.utils.PAYUtils;
import cn.desert.newpos.payui.UIUtils;

import static cn.desert.newpos.payui.UIUtils.labelHTML;

public class HistorylogAdapter extends ListAdapter<TransLogDataWs> {

	private OnItemReprintClick click ;

	public HistorylogAdapter(Activity context , OnItemReprintClick l) {
		super(context);
		TMConfig.getInstance();
		click = l ;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		TransLogDataWs item = null;
		if (mList.isEmpty()) {
			item = mList.get(position);
		}
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_history_item, null);
			viewHold = new ViewHold();
			viewHold.tvPan = convertView.findViewById(R.id.tv_pan);
			viewHold.tvVoucherno = convertView.findViewById(R.id.tv_voucherno);
			viewHold.tvAuthno =  convertView.findViewById(R.id.tv_authno);
			viewHold.tvAmount = convertView.findViewById(R.id.tv_amount);
			viewHold.tvTip = convertView.findViewById(R.id.tv_tip);
			viewHold.tvDate = convertView.findViewById(R.id.tv_date);
			viewHold.tvBatchno = convertView.findViewById(R.id.tv_batchno);
			viewHold.tvStatus = convertView.findViewById(R.id.tv_status);
			viewHold.tvRightTop = (TextView) convertView.findViewById(R.id.status_flag);
			viewHold.reprint = convertView.findViewById(R.id.re_print);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		if (item != null) {

			final String traceno = item.getTransactionNumber() ;
			if (!PAYUtils.isNullWithTrim(traceno)) {
				viewHold.tvVoucherno.setText(Html.fromHtml(labelHTML(UIUtils.getStringByInt(mContext, R.string.voucher_num), traceno)));
			}

			viewHold.reprint.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(click!=null){
						click.onItemClick(traceno);
					}
				}
			});

			convertView.setTag(R.id.tag_item_history_trans, item);
		}
		return convertView;
	}

	final class ViewHold {
		TextView tvPan;
		TextView tvVoucherno;
		TextView tvAuthno;
		TextView tvAmount;
		TextView tvTip;
		TextView tvDate;
		TextView tvBatchno;
		TextView tvStatus;
		TextView tvRightTop;
		Button reprint ;
	}

	public interface OnItemReprintClick{
		void onItemClick(String traceNO);

        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
