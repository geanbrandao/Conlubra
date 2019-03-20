package com.geanbrandao.gean.conlubra.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geanbrandao.gean.conlubra.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicacaoAdapter extends RecyclerView.Adapter<PublicacaoAdapter.MyViewHolder> {
    @NonNull
    @Override
    public PublicacaoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacaoAdapter.MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome, publicacao, data, contadorLikes, contadorComentarios;
        private TextView likes, comentarios;
        private ImageView fotoPublicacao;
        private CircleImageView fotoPerfil;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.tvPublicacaoNome);
            publicacao = itemView.findViewById(R.id.tvPublicacao);
            data = itemView.findViewById(R.id.tvPublicacaoData);
            contadorLikes = itemView.findViewById(R.id.tvPublicacaoContadorLikes);
            contadorComentarios = itemView.findViewById(R.id.tvPublicacaoContadorComentarios);
            likes = itemView.findViewById(R.id.tvPublicacaoLikes);
            comentarios = itemView.findViewById(R.id.tvPublicacaoComentarios);
            fotoPerfil = itemView.findViewById(R.id.civFotoPerfilPublicacao);
            fotoPublicacao = itemView.findViewById(R.id.ivFotoPublicacao);


        }
    }
}
