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
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.geanbrandao.gean.conlubra.modelo.ItemProgramacao;

import java.util.List;

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.MyViewHolder> {

    private static final String TAG = "AdapterProg";

    private Context context;
    private List<ItemProgramacao> programacao;
    private ProgramacaoAdapaterListener listener;

    public ProgramacaoAdapter(Context context, List<ItemProgramacao> programacao, ProgramacaoAdapaterListener listener) {
        this.context = context;
        this.programacao = programacao;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProgramacaoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_programacao, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramacaoAdapter.MyViewHolder holder, int i) {

        ItemProgramacao p = programacao.get(i);
        holder.hora.setText(p.getHora());
        holder.nome.setText(p.getNome());
        holder.local.setText(p.getLocal());
        if (isFavorito(p.getId())) {
            holder.favorito.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            holder.favorito.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        aplicarEventoClick(holder, i);

    }

    private boolean isFavorito(String id) {
        if (InformacoesUsuario.user.getIdsItensFavoritos() != null) {
            for (String f : InformacoesUsuario.user.getIdsItensFavoritos()) {
                if (f.equals(id)) { // compara se esta na lista dos favoritos do usuario
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return programacao.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hora, nome, local;
        Button favorito;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hora = itemView.findViewById(R.id.tv_hora_item);
            nome = itemView.findViewById(R.id.tv_nome_item);
            local = itemView.findViewById(R.id.tv_local_item);
            favorito = itemView.findViewById(R.id.b_favoritar_item);

        }

    }

    private void aplicarEventoClick(final MyViewHolder holder, final int position) {
        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "nome clicado");
                listener.onItemClicked(position);
            }
        });

        holder.favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.favorito.getBackground().getConstantState()
                        .equals(context.getResources()
                                .getDrawable(R.drawable.ic_favorite_black_24dp)
                                .getConstantState())) {
                    holder.favorito.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    listener.onFavoritoCliked(position, true);
                    Log.i(TAG, "com like");
                } else {
                    holder.favorito.setBackground(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
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
}
