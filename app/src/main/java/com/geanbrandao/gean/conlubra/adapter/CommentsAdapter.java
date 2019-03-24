package com.geanbrandao.gean.conlubra.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geanbrandao.gean.conlubra.DownloadImageTask;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.model.Comentario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private List<Comentario> comments;
    private Context context;
    private ComentariosAdapaterListener listener;

    public CommentsAdapter(List<Comentario> comments, Context context, ComentariosAdapaterListener listener) {
        this.comments = comments;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_comentario, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Comentario c = comments.get(i);
        holder.name.setText(c.getNomeAutorComentario());
        holder.date.setText(formataData(c.getDataComentario()));
        holder.comment.setText(c.getConteudoComentario());
        int count = c.getContadorLikesComentario();
        holder.countlikes.setText(count);
        baixarImagem(c.getFotoAutorPostagem(), holder.civProfilePicture);
        aplicaEventoClick(holder, i);
        if (isLike(c.getIdComentario())){
            holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

    }

    private void aplicaEventoClick(final MyViewHolder holder, final int position) {
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Botao like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isLike(String id) {
        if (UserInformation.user.getIdsComentarioCurtidos() != null ) {
            for (String l : UserInformation.user.getIdsComentarioCurtidos()) {
                if (id.equals(l)) { // compara se esta na lista dos comentarios curtidas pelo usuario
                    return true;
                }
            }
        }
        return false;
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
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, date, comment, countlikes;
        private Button like;
        private CircleImageView civProfilePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_comentario_nome);
            date = itemView.findViewById(R.id.tv_comentario_data);
            comment = itemView.findViewById(R.id.tv_comentario);
            countlikes = itemView.findViewById(R.id.tvContadorLikesComentario);
            like = itemView.findViewById(R.id.bLikecomentario);
            civProfilePicture = itemView.findViewById(R.id.civ_foto_perfil_comentario);
        }
    }
    public interface ComentariosAdapaterListener {
        void onItemClicked(int position);

        void onLikeCliked(int position, boolean like);
    }

}