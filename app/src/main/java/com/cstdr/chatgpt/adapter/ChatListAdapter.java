package com.cstdr.chatgpt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cstdr.chatgpt.MyApplication;
import com.cstdr.chatgpt.R;
import com.cstdr.chatgpt.bean.ChatMessage;
import com.cstdr.chatgpt.constant.Constant;
import com.cstdr.chatgpt.util.ClipboardUtil;

import java.util.List;
import java.util.zip.Inflater;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {


    private List<ChatMessage> mChatMessageList;

    public ChatListAdapter(List<ChatMessage> chatMessageList) {
        mChatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatMessage chatMessage = mChatMessageList.get(position);
        String msg = chatMessage.getMsg();
        String owner = chatMessage.getOwner();
        if (owner.equals(Constant.OWNER_BOT)) {
            holder.mPbThink.setVisibility(View.GONE);
            holder.mRlHuman.setVisibility(View.GONE);
            holder.mRlBot.setVisibility(View.VISIBLE);
            holder.mTvMsgBot.setText(msg);
            holder.mIvCopy.setVisibility(View.VISIBLE);
            holder.mIvCopy.setOnClickListener(v -> {
                // 复制到剪贴板
                if (ClipboardUtil.copy(msg)) {
                    Toast.makeText(v.getContext(), "已复制", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "复制出错", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (owner.equals(Constant.OWNER_HUMAN)) {
            holder.mPbThink.setVisibility(View.GONE);
            holder.mRlBot.setVisibility(View.GONE);
            holder.mRlHuman.setVisibility(View.VISIBLE);
            holder.mTvMsgHuman.setText(msg);
            holder.mIvCopy.setVisibility(View.GONE);
        } else if (owner.equals(Constant.OWNER_BOT_THINK)) {
            holder.mPbThink.setVisibility(View.VISIBLE);
            holder.mRlHuman.setVisibility(View.GONE);
            holder.mRlBot.setVisibility(View.VISIBLE);
            holder.mTvMsgBot.setText(msg);
            holder.mIvCopy.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mChatMessageList != null) {
            int size = mChatMessageList.size();
            return size;
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout mRlBot;
        private final RelativeLayout mRlHuman;
        private TextView mTvMsgBot;
        private TextView mTvMsgHuman;
        private ProgressBar mPbThink;
        private ImageView mIvCopy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mRlBot = itemView.findViewById(R.id.rl_bot);
            mRlHuman = itemView.findViewById(R.id.rl_human);

            mTvMsgBot = itemView.findViewById(R.id.tv_msg_bot);
            mTvMsgHuman = itemView.findViewById(R.id.tv_msg_human);

            mPbThink = itemView.findViewById(R.id.pb_think);
            mIvCopy = itemView.findViewById(R.id.iv_icon_copy);
        }
    }

}
