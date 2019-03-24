package com.geanbrandao.gean.conlubra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.model.ItemProgramacao;

import java.util.List;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.MyViewHolder> {

    private static final String TAG = "AdapterProg";

    private Context context;
    private List<ItemProgramacao> programming;
    private ProgramacaoAdapaterListener listener;

    public ProgrammingAdapter(Context context, List<ItemProgramacao> programming, ProgramacaoAdapaterListener listener) {
        this.context = context;
        this.programming = programming;
        this.listener = listener;
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
        if (isFavorito(p.getId())) {
            holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        aplicarEventoClick(holder, i);

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



    private void aplicarEventoClick(final MyViewHolder holder, final int position) {
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "nome clicado");
                listener.onItemClicked(position);
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.favorite.getBackground().getConstantState()
                        .equals(context.getResources()
                                .getDrawable(R.drawable.ic_favorite_black_24dp)
                                .getConstantState())) {
                    holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    listener.onFavoritoCliked(position, true);
                    Log.i(TAG, "com like");
                } else {
                    holder.favorite.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    Log.i(TAG, "sem like");
                    // sem like
                    listener.onFavoritoCliked(position, false);
                }

            }
        });
    }

    public interface ProgramacaoAdapaterListener {
        void onItemClicked(int position);

        void onFavoritoCliked(int position, boolean like);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hour, name, place;
        Button favorite;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.tv_hora_item);
            name = itemView.findViewById(R.id.tv_nome_item);
            place = itemView.findViewById(R.id.tv_local_item);
            favorite = itemView.findViewById(R.id.b_favoritar_item);

        }

    }
}
