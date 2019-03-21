package com.geanbrandao.gean.conlubra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geanbrandao.gean.conlubra.DownloadImageTask;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.conexao.InformacoesUsuario;
import com.geanbrandao.gean.conlubra.modelo.Postagem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicacaoAdapter extends RecyclerView.Adapter<PublicacaoAdapter.MyViewHolder> {

    private List<Postagem> postagens;
    private Context context;
    private PublicacaoAdapterListener listener;

    public PublicacaoAdapter(List<Postagem> postagens, Context context, PublicacaoAdapterListener listener) {
        this.postagens = postagens;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PublicacaoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_publicacao, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacaoAdapter.MyViewHolder holder, int i) {
        Postagem p = postagens.get(i);
        if (p.getFotoAutorPostagem() != null) {
            baixarImagem(p.getFotoAutorPostagem(), holder.fotoPerfil);
        }
        if (p.getImagemPostagem() != null) {
            baixarImagem(p.getImagemPostagem(), holder.fotoPublicacao);
        }
        holder.nome.setText(p.getNomeAutorPostagem());
        holder.data.setText(formataData(p.getDataPostagem()));
        holder.publicacao.setText(p.getConteudoPostagem());
        holder.contadorLikes.setText(p.getContadorLikesPostagem() + "");
        holder.contadorComentarios.setText(p.getContadorComentariosPostagem() + "");
        aplicaEventoClick(holder, i);
        if (isLike(p.getIdPostagem())) {
            holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

    }

    private boolean isLike(String id) {
        if (InformacoesUsuario.user.getIdsPostagemCurtidas() != null ) {
            for (String l : InformacoesUsuario.user.getIdsPostagemCurtidas()) {
                if (id.equals(l)) { // compara se esta na lista das publicacoes curtidas pelo usuario
                    return true;
                }
            }
        }
        return false;
    }

    private void aplicaEventoClick(final MyViewHolder holder, final int position) {
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Botao like", Toast.LENGTH_SHORT).show();
                int countLikes = postagens.get(position).getContadorLikesPostagem();
                if (holder.like.getBackground().getConstantState()
                        .equals(context.getDrawable(R.drawable.ic_favorite_black_24dp)
                                .getConstantState())) {
                    holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    countLikes--;
                    holder.contadorLikes.setText(""+countLikes);
                    listener.onLikeCliked(position, false);
                    Log.i("Like", "Tinha like agora não tem");
                } else {
                    holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_black_24dp));
                    countLikes++;
                    holder.contadorLikes.setText(""+countLikes);
                    listener.onLikeCliked(position, true);
                    Log.i("Like", "Não tinha like agora tem");
                }
            }
        });

        holder.comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Botao comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void baixarImagem(String url, ImageView iv) {
        new DownloadImageTask(iv).execute(url);
    }

    private String formataData(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm '-' dd/MM/yyyy", Locale.US);
        return dateFormatter.format(date);
    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome, publicacao, data, contadorLikes, contadorComentarios;
        private ImageView like, comentario;
        private ImageView fotoPublicacao;
        private CircleImageView fotoPerfil;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.tvPublicacaoNome);
            publicacao = itemView.findViewById(R.id.tvPublicacao);
            data = itemView.findViewById(R.id.tvPublicacaoData);
            contadorLikes = itemView.findViewById(R.id.tvPublicacaoContadorLikes);
            contadorComentarios = itemView.findViewById(R.id.tvPublicacaoContadorComentarios);
            like = itemView.findViewById(R.id.bPublicacaoLikes);
            comentario = itemView.findViewById(R.id.bPublicacaoComentarios);
            fotoPerfil = itemView.findViewById(R.id.civFotoPerfilPublicacao);
            fotoPublicacao = itemView.findViewById(R.id.ivFotoPublicacao);


        }
    }

    public interface PublicacaoAdapterListener {
        void onItemClicked(int position);

        void onLikeCliked(int position, boolean like);

        void onComentarioCliked(int position, boolean like);
    }
}
