package br.com.everoot.tarefasescolar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import br.com.everoot.tarefasescolar.Model.Tarefa;
import br.com.everoot.tarefasescolar.Model.Turma;
import br.com.everoot.tarefasescolar.Model.Usuario;
import br.com.everoot.tarefasescolar.R;
import br.com.everoot.tarefasescolar.View.HomeClassActivity;

public class TarefasAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Tarefa> tarefas;
    private static ClickRecyclerViewListener clickRecyclerViewListener;
    private FirebaseUser user;


    public TarefasAdapter(@NonNull Context context, List<Tarefa> tarefas, ClickRecyclerViewListener clickRecyclerViewListener) {
        this.clickRecyclerViewListener = clickRecyclerViewListener;
        this.context = context;
        this.tarefas = tarefas;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_tarefa, parent, false);

        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TarefaViewHolder viewHolder = (TarefaViewHolder) holder;
        Tarefa tarefa = tarefas.get(position);

        viewHolder.descricaoTarefa.setText(tarefa.getDescricao());
        viewHolder.conteudoTarefa.setText(tarefa.getConteudo());
        viewHolder.localTarefa.setText(tarefa.getLocal());
        viewHolder.dataTarefa.setText(tarefa.getData());
        viewHolder.horaTarefa.setText(tarefa.getHora());

        viewHolder.editarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Teste", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public static class TarefaViewHolder extends RecyclerView.ViewHolder {

        private final TextView descricaoTarefa;
        private final TextView conteudoTarefa;
        private final TextView localTarefa;
        private final TextView dataTarefa;
        private final TextView horaTarefa;
        private final FloatingActionButton editarTarefa, deletarTarefa;

        public TarefaViewHolder(View itemView) {
            super(itemView);
            dataTarefa = itemView.findViewById(R.id.textViewDataTarefa);
            horaTarefa = itemView.findViewById(R.id.textViewHoraTarefa);
            descricaoTarefa = itemView.findViewById(R.id.textViewDescricaoTarefa);
            conteudoTarefa = itemView.findViewById(R.id.textViewConteudoTarefa);
            localTarefa = itemView.findViewById(R.id.textViewLocalTarefa);
            editarTarefa = itemView.findViewById(R.id.floatingActionButtonEditarTarefa);
            deletarTarefa = itemView.findViewById(R.id.floatingActionButtonDeletarTarefa);
        }
    }
}
