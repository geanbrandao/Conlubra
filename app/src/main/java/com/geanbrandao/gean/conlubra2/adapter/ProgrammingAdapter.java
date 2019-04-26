package com.geanbrandao.gean.conlubra2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra2.R;
import com.geanbrandao.gean.conlubra2.connection.UserInformation;
import com.geanbrandao.gean.conlubra2.model.ItemProgramacao;

import java.util.List;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.MyViewHolder> {

    private static final String TAG = "AdapterProg";

    private Context context;
    private List<ItemProgramacao> programming;

    public ProgrammingAdapter(Context context, List<ItemProgramacao> programming) {
        this.context = context;
        this.programming = programming;
    }

    @NonNull
    @Override
    public ProgrammingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_programacao, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingAdapter.MyViewHolder holder, int i) {

        ItemProgramacao p = programming.get(i);
        holder.hour.setText(p.getHora());
        holder.name.setText(p.getNome());
        holder.place.setText(p.getLocal());

    }

    private boolean isFavorito(String id) {
        if (UserInformation.user.getIdsItensFavoritos() != null) {
            for (String f : UserInformation.user.getIdsItensFavoritos()) {
                if (f.equals(id)) { // compara se esta na lista dos favoritos do usuario
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return programming.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hour, name, place;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.tv_hora_item);
            name = itemView.findViewById(R.id.tv_nome_item);
            place = itemView.findViewById(R.id.tv_local_item);

        }

    }

}
