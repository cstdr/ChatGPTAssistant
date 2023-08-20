package com.cstdr.chatgpt.controller.adapter;

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

import com.cstdr.chatgpt.R;
import com.cstdr.chatgpt.model.ChatMessage;
import com.cstdr.chatgpt.model.ChatMessageData;
import com.cstdr.chatgpt.model.Constant;
import com.cstdr.chatgpt.util.ClipboardUtil;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    public ChatListAdapter() {
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
        ChatMessage chatMessage = ChatMessageData.getInstance().getChatMessage(position);
        String msg = chatMessage.getMsg();
        String owner = chatMessage.getOwner();
        if (owner.equals(Constant.OWNER_BOT)) {
            holder.mPbThink.setVisibility(View.GONE);
            holder.mRlHuman.setVisibility(View.GONE);
            holder.mRlBot.setVisibility(View.VISIBLE);
            holder.mTvMsgBot.setText(msg);

            // TODO 复制去掉
            holder.mIvCopy.setVisibility(View.GONE);
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
        return ChatMessageData.getInstance().getSize();
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
