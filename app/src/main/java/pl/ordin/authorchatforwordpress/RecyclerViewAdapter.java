package pl.ordin.authorchatforwordpress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<CustomArrayList> chatting = new ArrayList<>();
    private RecyclerView recyclerView;

    //constructor
    public RecyclerViewAdapter(ArrayList<CustomArrayList> chatting, RecyclerView recyclerView) {
        this.chatting = chatting;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        //onClickListener that removes element after touch
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int positionToDelete = recyclerView.getChildAdapterPosition(v);

                chatting.remove(positionToDelete);

                notifyItemRemoved(positionToDelete);
            }
        });

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
