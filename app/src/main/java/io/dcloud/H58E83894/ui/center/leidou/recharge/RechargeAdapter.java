package io.dcloud.H58E83894.ui.center.leidou.recharge;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.OnAdapterListener;
import io.dcloud.H58E83894.data.LeidouRechargeData;

/**
 * Created by fire on 2017/8/16  13:42.
 */

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.BaseViewHolder> {


    private ArrayList<ItemModel> dataList = new ArrayList<>();
//    private List<LeidouRechargeData> mDatas;
    private OnAdapterListener<LeidouRechargeData> mOnItemClickListener;
    private int lastPressIndex = -1;
    private Context context;

    public void replaceAll(ArrayList<ItemModel> list, Context context) {
        this.context = context;
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public RechargeAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.ONE:
                return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leidou_recharge, parent, false));

            case ItemModel.TWO:
                return new TWoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leidou_recharge_edit, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RechargeAdapter.BaseViewHolder holder, int position) {

        holder.setData(dataList.get(position).data);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(String data) {
        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private TextView tvMoney;
        private TextView tvSum;
        private RelativeLayout relativeLayout;

        public OneViewHolder(View view) {
            super(view);
            tvMoney = (TextView) view.findViewById(R.id.leidou_money);
            tvSum = (TextView) view.findViewById(R.id.leidou_sum);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.leidou_relative);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "OneViewHolder: ");

                    notyfy = 0;
                    int position = getAdapterPosition();
                    ItemModel model = dataList.get(position);
                    EventBus.getDefault().post(model);
                    if (lastPressIndex == position) {
                        lastPressIndex = -1;
                    } else {
                        lastPressIndex = position;
                    }
                    notifyDataSetChanged();


                }

            });
        }



        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        void setData(String data) {
            Integer wrapperi = new Integer(data);
            if (wrapperi != null) {
                tvMoney.setText(data+"元");
                String price_CNY = data; //6.2041
                int cny = Integer.parseInt(price_CNY);
//                Double cny = Double.parseDouble(price_CNY);
                tvSum.setText(cny*100+"雷豆");

                Log.i("OneViewHolder = ", getAdapterPosition()+"ddd"+lastPressIndex);
                if (getAdapterPosition() == lastPressIndex) {
                    relativeLayout.setSelected(true);
                    tvMoney.setTextColor(context.getResources().getColor(R.color.color_white));
                    tvSum.setTextColor(context.getResources().getColor(R.color.color_white));
                    relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_leidou_lan));

                } else {
//                    relativeLayout.setSelected(false);
                    tvMoney.setTextColor(context.getResources().getColor(R.color.color_text_green));
                    tvSum.setTextColor(context.getResources().getColor(R.color.color_text_green));
                    relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_leidou_yuan));

//                    tv.setSelected(false);
//                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
                }

            }


        }
    }

    int notyfy = 0;


    private class TWoViewHolder extends BaseViewHolder {
        private EditText et;
        private String chargeFunds;
        private RelativeLayout relativeLayouts;
        private TextView tvSum;


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public TWoViewHolder(View view) {
            super(view);
            final int position = getAdapterPosition();
            et = (EditText) view.findViewById(R.id.leidou_moneys);
            et.setFilters(new InputFilter[]{lengthFilter});
            relativeLayouts = (RelativeLayout) view.findViewById(R.id.leidou_relatives);
            tvSum = (TextView) view.findViewById(R.id.leidou_sums);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (lastPressIndex != position) {
                            notifyItemChanged(lastPressIndex);
                            lastPressIndex = position;


                        }
                    }
                }
            });

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence != null || !charSequence.equals("")) {
                        try {
//                            String str = Integer.parseInt(String.valueOf(charSequence));

                            Double cny = Double.parseDouble(String.valueOf(charSequence));
                            tvSum.setText(cny*100+"雷豆");
                            et.setSelection(et.getText().length());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                    }
//                        }
                        }
                    }

                @Override
                public void afterTextChanged(Editable editable) {


//                    if (editable.length() > 0) {
                        String inputText = et.getText().toString().trim();
//                        if (Double.parseDouble(inputText) < 9) {
//                            chargeFunds = "9";
//                        } else if (Double.parseDouble(inputText) > 2000) {
//                            chargeFunds = "2000";
//                        } else {
//                            chargeFunds = inputText;
//                        }
//                    }

                    String price_CNY = inputText; //6.2041
//                    Double cny = Double.parseDouble(price_CNY);
//                    int chargeFundss = Integer.parseInt(funds);
                    ItemModel model = new ItemModel(ItemModel.TWO, price_CNY);
                    EventBus.getDefault().post(model);
                }
            });
        }

//        private void restrictText() {
//            String input = et.getText().toString();
//            if (TextUtils.isEmpty(input)) {
//                return;
//            }
//            if (input.contains(".")) {
//                int pointIndex = input.indexOf(".");
//                int totalLenth = input.length();
//                int len = (totalLenth - 1) - pointIndex;
//                if (len > 2) {
//                    input = input.substring(0, totalLenth - 1);
//                    textChange = true;
//                    setText(input);
//                    setSelection(input.length());
//                }
//            }
//
//            if (input.toString().trim().substring(0).equals(".")) {
//                input = "0" + input;
//                setText(input);
//                setSelection(2);
//            }
//
//        }
//
//        /**
//         * 获取金额
//         */
//        public String getMoneyText() {
//            String money = et.getText().toString();
//            //如果最后一位是小数点
//            if (money.endsWith(".")) {
//                return money.substring(0, money.length() - 1);
//            }
//            return money;
//        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        void setData(String data) {
            super.setData(data);
            Log.i("TWoViewHolder = ", getAdapterPosition()+"ddd"+lastPressIndex);
            int position = getAdapterPosition();
            if (position == lastPressIndex){

                et.requestFocus();
//                relativeLayout.setSelected(true);
                et.setTextColor(context.getResources().getColor(R.color.color_white));
                tvSum.setTextColor(context.getResources().getColor(R.color.color_white));
                relativeLayouts.setBackground(context.getResources().getDrawable(R.drawable.ic_leidou_lan));
            }else{
                et.clearFocus();

                et.setTextColor(context.getResources().getColor(R.color.color_text_green));
                tvSum.setTextColor(context.getResources().getColor(R.color.color_text_green));
                relativeLayouts.setBackground(context.getResources().getDrawable(R.drawable.ic_leidou_yuan));
//                relativeLayout.setSelected(true);

            }

        }

    }



    private InputFilter lengthFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // source:当前输入的字符
            // start:输入字符的开始位置
            // end:输入字符的结束位置
            // dest：当前已显示的内容
            // dstart:当前光标开始位置
            // dent:当前光标结束位置
//            LogUtil.i("", "source=" + source + ",start=" + start + ",end=" + end + ",dest=" + dest.toString() + ",dstart=" + dstart + ",dend=" + dend);
            if (dest.length() == 0 && source.equals(".")) {
                return "0.";
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                if (dotValue.length() == 2) {//输入框小数的位数
                    return "";
                }
            }
            return null;
        }
    };


}
