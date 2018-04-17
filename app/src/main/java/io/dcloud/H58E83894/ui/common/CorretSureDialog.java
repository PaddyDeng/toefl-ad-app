package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.CorretData;
import io.dcloud.H58E83894.data.ItemModelData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.BasesDialog;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class CorretSureDialog extends BasesDialog {

    private static ICallBack<String> mCallBack;
    private static final String SIMPLE_DIALOG_CONTENT = "dialog_content";
    protected RxPermissions mRxPermissions;
    private List<SpeakQuestionData> mSpeakDatas;


    public static CorretSureDialog getInstance(String content) {
        CorretSureDialog simpleDialog = new CorretSureDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SIMPLE_DIALOG_CONTENT, content);
        simpleDialog.setArguments(bundle);
        return simpleDialog;
    }

    @BindView(R.id.dialog_simple_btn_cancel)
    ImageView cancelTxt;
    @BindView(R.id.update_tvss)
    TextView updateTv;
    private String price;


    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle arguments = getArguments();
        if (arguments == null) return;
        price = arguments.getString(SIMPLE_DIALOG_CONTENT);
//        contentTv.setText(HtmlUtil.fromHtml(arguments.getString(SIMPLE_DIALOG_CONTENT)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onFail();
                    mCallBack = null;
                }
                dismiss();
            }
        });
        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                    if(TextUtils.isEmpty(price)){ return;}
                    int prices = Integer.parseInt(price);
                    if(prices == 419 ){
                        updateTv.setText("抢到作文免费点评名额");
                    }else {
                        updateTv.setText("抢到口语免费点评名额");
                    }
                    addToCompositeDis(HttpUtil
                            .getPlaces(prices).subscribe(new Consumer<CorretData>() {
                                @Override
                                public void accept(@NonNull CorretData data) throws Exception {
                                    WaitDialog.getInstance(getContext()).dismissWaitDialog();
                                    if (data.getCode() == 1) {
                                        toastShort("使用免费点评名额成功");
                                        int contentids = Integer.parseInt(data.getContentId().trim());
                                        ItemModelData model = new ItemModelData(contentids, data.getToken());
                                        EventBus.getDefault().post(model);
                                        dismiss();
                                    } else {
                                        toastShort("使用免费点评名额失败");
//                                        CorretSureDialog corretNoDialog = new CorretSureDialog();
//                                        corretNoDialog.showDialog(getFragmentManager());
                                        dismiss();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            dismiss();
                        }
                    }));
            }


        });

    }

    @Override
    protected int getRootViewId() {
        return R.layout.corret_sure;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }
}
