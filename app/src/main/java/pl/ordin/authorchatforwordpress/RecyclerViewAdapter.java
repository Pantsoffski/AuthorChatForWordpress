package pl.ordin.authorchatforwordpress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<CustomArrayList> chatting = new ArrayList<>();

    //constructor
    RecyclerViewAdapter(ArrayList<CustomArrayList> chatting) {
        this.chatting = chatting;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {

        CustomArrayList singleChat = chatting.get(i);
        ((MyViewHolder) viewHolder).nickView.setText(singleChat.nick);
        ((MyViewHolder) viewHolder).dateView.setText(singleChat.date);
        ((MyViewHolder) viewHolder).chatView.setText(singleChat.chat.replace("&#34;", "\"")); //plus replace quotation mark &#34; to "
    }

    @Override
    public int getItemCount() {
        return chatting.size();
    }

    public void setItems(ArrayList<CustomArrayList> refreshedChat) {
        this.chatting = refreshedChat;
    }

    //holder class
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickView, dateView, chatView;

        public MyViewHolder(View item) {
            super(item);
            nickView = (TextView) item.findViewById(R.id.nickView);
            dateView = (TextView) item.findViewById(R.id.dateView);
            chatView = (TextView) item.findViewById(R.id.chatView);
        }
    }
}
