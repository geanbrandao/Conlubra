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

import com.geanbrandao.gean.conlubra.utils.DownloadImageTask;
import com.geanbrandao.gean.conlubra.R;
import com.geanbrandao.gean.conlubra.connection.UserInformation;
import com.geanbrandao.gean.conlubra.model.Postagem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.MyViewHolder> {

    private List<Postagem> posts;
    private Context context;
    private PublicacaoAdapterListener listener;

    public PublicationAdapter(List<Postagem> posts, Context context, PublicacaoAdapterListener listener) {
        this.posts = posts;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PublicationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_publicacao, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationAdapter.MyViewHolder holder, int i) {
        Postagem p = posts.get(i);
        if (p.getFotoAutorPostagem() != null) {
            baixarImagem(p.getFotoAutorPostagem(), holder.profilePicture);
        }
        if (p.getImagemPostagem() != null) {
            baixarImagem(p.getImagemPostagem(), holder.picturePost);
        }
        holder.name.setText(p.getNomeAutorPostagem());
        holder.date.setText(formataData(p.getDataPostagem()));
        holder.post.setText(p.getConteudoPostagem());
        holder.countLikes.setText(String.valueOf(p.getContadorLikesPostagem()));
        holder.countComments.setText(String.valueOf(p.getContadorComentariosPostagem()));
        aplicaEventoClick(holder, i);
        if (isLike(p.getIdPostagem())) {
            holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

    }

    private boolean isLike(String id) {
        if (UserInformation.user.getIdsPostagemCurtidas() != null ) {
            for (String l : UserInformation.user.getIdsPostagemCurtidas()) {
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
                int countLikes = posts.get(position).getContadorLikesPostagem();
                if (holder.like.getBackground().getConstantState()
                        .equals(context.getDrawable(R.drawable.ic_favorite_black_24dp)
                                .getConstantState())) {
                    holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    countLikes--;
                    holder.countLikes.setText(String.valueOf(countLikes));
                    listener.onLikeCliked(position, false);
                    Log.i("Like", "Tinha like agora não tem");
                } else {
                    holder.like.setBackground(context.getDrawable(R.drawable.ic_favorite_black_24dp));
                    countLikes++;
                    holder.countLikes.setText(String.valueOf(countLikes));
                    listener.onLikeCliked(position, true);
                    Log.i("Like", "Não tinha like agora tem");
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Botao comentario", Toast.LENGTH_SHORT).show();
                listener.onComentarioCliked(position);
            }
        });

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Publicacao", Toast.LENGTH_SHORT).show();
                listener.onItemClicked(position);
            }
        });

        holder.picturePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onImageCliked(position);
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
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, post, date, countLikes, countComments;
        private Button like, comment;
        private ImageView picturePost;
        private CircleImageView profilePicture;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name_publication);
            post = itemView.findViewById(R.id.tv_publication_publication);
            date = itemView.findViewById(R.id.tv_date_publication);
            countLikes = itemView.findViewById(R.id.tv_likes_publication);
            countComments = itemView.findViewById(R.id.tv_comment_publication);
            like = itemView.findViewById(R.id.b_like_publication);
            comment = itemView.findViewById(R.id.b_comment_publication);
            profilePicture = itemView.findViewById(R.id.civ_profile_publication);
            picturePost = itemView.findViewById(R.id.iv_picture_publication);


        }
    }

    public interface PublicacaoAdapterListener {
        void onItemClicked(int position);

        void onLikeCliked(int position, boolean like);

        void onComentarioCliked(int position);

        void onImageCliked(int position);
    }
}
