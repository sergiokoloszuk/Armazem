package br.com.sergiokoloszuk.armazem.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import br.com.sergiokoloszuk.armazem.MainActivity;
import br.com.sergiokoloszuk.armazem.R;
import br.com.sergiokoloszuk.armazem.interfaces.RecyclerViewItem;
import br.com.sergiokoloszuk.armazem.model.Cadastro;

public class RecyclerItemAdapter
        extends RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder> {

private List<Cadastro> cadastroList;
private RecyclerViewItem listener;

public RecyclerItemAdapter(List<Cadastro> cadastroList, RecyclerViewItem listener) {
        this.cadastroList = cadastroList;
        this.listener = listener;
        }


    @NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
// Pego a pessoa na posição enviada
final Cadastro cadastro = cadastroList.get(position);

        //Preenche os dados da pessoa nas views
        holder.bind(cadastro);

        //Seta a animação para cada item
        setAnimation(holder.itemView);

        //Vincula o evendo de click do item e repassa para o listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        // Viculamos o click no item
        listener.onItemClick(cadastro);
        }
        });
        }

// Set a animação em uma view
private void setAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_left);
        view.startAnimation(animation);
        }

@Override
public int getItemCount() {
        return cadastroList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    //Atributos para usar no preenchimento de dados
    private TextView textViewApp;
    private TextView textViewEmail;
    private TextView textViewPassword;

    public ViewHolder(View itemView) {
        super(itemView);

        //Pega a refencia no XML
        textViewApp = itemView.findViewById(R.id.txt_App);
        textViewEmail = itemView.findViewById(R.id.txt_Email);
        textViewPassword = itemView.findViewById(R.id.txt_Password);
    }

    // Preenche os dados da pessoa
    public void bind(Cadastro cadastro) {
        textViewApp.setText(cadastro.getApp());
        textViewEmail.setText(cadastro.getEmail());
        textViewPassword.setText( cadastro.getPassword() );
    }
}

    public void update(List<Cadastro> newListCadastro) {
        this.cadastroList.clear();
        this.cadastroList = newListCadastro;
        notifyDataSetChanged();
    }
}
