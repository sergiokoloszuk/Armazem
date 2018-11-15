package br.com.sergiokoloszuk.armazem;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.sergiokoloszuk.armazem.DAO.CadastroDAO;
import br.com.sergiokoloszuk.armazem.adapters.RecyclerItemAdapter;
import br.com.sergiokoloszuk.armazem.database.DatabaseRoom;
import br.com.sergiokoloszuk.armazem.interfaces.RecyclerViewItem;
import br.com.sergiokoloszuk.armazem.model.Cadastro;

public class MainActivity extends AppCompatActivity implements RecyclerViewItem {

    private CadastroDAO cadastroDAO;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TextInputLayout textInputLayoutApp;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private ImageView imageViewDelete;
    private List<Cadastro> cadastroList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        fab = (FloatingActionButton) findViewById( R.id.fabAddPerson );
        recyclerView = findViewById( R.id.recycleView );
        textInputLayoutApp = findViewById( R.id.textInputLayoutApp );
        textInputLayoutEmail = findViewById( R.id.textInputLayoutEmail );
        textInputLayoutPassword = findViewById( R.id.textInputLayoutPassword );
        imageViewDelete = findViewById( R.id.imageViewDelete );

        //setSupportActionBar( toolbar );

        DatabaseRoom room = DatabaseRoom.getDatabase( this );
        cadastroDAO = room.cadastroDAO();

        final RecyclerItemAdapter adapter = new RecyclerItemAdapter( cadastroList, this );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( adapter );
        getAlldataFromDataBase( adapter );

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String app = textInputLayoutApp.getEditText().getText().toString();
                final String email = textInputLayoutEmail.getEditText().getText().toString();
                final String password = textInputLayoutEmail.getEditText().getText().toString();

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        Cadastro cadastro = cadastroDAO.getByApp( app );

                        if (cadastro != null) {

                            cadastro.setApp( app );
                            cadastro.setEmail( email );
                            cadastro.setPassword( password );

                            cadastroDAO.update( cadastro );

                            showToastAlert( "Aplicativo: " + app + " atualizado" );
                        } else {
                            cadastro = new Cadastro();
                            cadastro.setApp( app );
                            cadastro.setEmail( email );
                            cadastro.setPassword( password );
                            cadastroDAO.insert( cadastro );
                            showToastAlert( "Aplicativo: " + app + " inserido" );
                        }
                    }
                } ).start();
            }
        } );

        imageViewDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String app = textInputLayoutApp.getEditText().getText().toString();

                new Thread( new Runnable() {
                    @Override
                    public void run() {

                        final Cadastro cadastro = cadastroDAO.getByApp( app );

                        if (cadastro != null) {
                            cadastroDAO.delete( cadastro );
                            showToastAlert( "Aplicativo: " + app + " deletado" );
                        } else {
                            showToastAlert( "Aplicativo: " + app + " não existe na base de dados" );
                        }
                    }
                } ).start();
            }
        } );
    }

    private void getAlldataFromDataBase(final RecyclerItemAdapter adapter) {
        new Thread( new Runnable() {
            @Override
            public void run() {

                cadastroDAO.getAll().observe( MainActivity.this, new Observer<List<Cadastro>>() {
                    @Override
                    public void onChanged(@Nullable final List<Cadastro> personList) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                adapter.update( personList );
                            }
                        } );
                    }
                } );

            }
        } ).start();
    }

    public void showToastAlert(final String message) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( MainActivity.this, message, Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @Override
    public void onItemClick(Cadastro cadastro) {
        textInputLayoutApp.getEditText().setText( cadastro.getApp() );
        textInputLayoutEmail.getEditText().setText( cadastro.getEmail() );
        textInputLayoutPassword.getEditText().setText( cadastro.getPassword() );
    }
}
